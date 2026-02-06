package com.team.chord.core.network.auth

import com.team.chord.core.network.api.AuthApi
import com.team.chord.core.network.dto.auth.TokenRefreshRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val authApiProvider: dagger.Lazy<AuthApi>,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= MAX_RETRY) {
            runBlocking { tokenManager.clearTokens() }
            return null
        }

        val refreshToken = runBlocking { tokenManager.getRefreshToken() } ?: run {
            runBlocking { tokenManager.clearTokens() }
            return null
        }

        return runBlocking {
            try {
                val refreshResponse = authApiProvider.get()
                    .refreshToken(TokenRefreshRequest(refreshToken))

                if (refreshResponse.isSuccessful) {
                    val newAccessToken = refreshResponse.body()?.data?.accessToken
                    if (newAccessToken != null) {
                        tokenManager.saveTokens(newAccessToken, refreshToken)
                        response.request.newBuilder()
                            .header("Authorization", "Bearer $newAccessToken")
                            .build()
                    } else {
                        tokenManager.clearTokens()
                        null
                    }
                } else {
                    tokenManager.clearTokens()
                    null
                }
            } catch (e: Exception) {
                tokenManager.clearTokens()
                null
            }
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }
        return count
    }

    private companion object {
        const val MAX_RETRY = 2
    }
}
