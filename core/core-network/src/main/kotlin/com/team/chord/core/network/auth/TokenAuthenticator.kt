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

    private val lock = Any()

    @Volatile
    private var lastRefreshTimeMs = 0L

    override fun authenticate(route: Route?, response: Response): Request? {
        val path = response.request.url.encodedPath
        if (isPublicEndpoint(path)) return null

        if (responseCount(response) >= MAX_RETRY) {
            runBlocking { tokenManager.clearTokens() }
            return null
        }

        synchronized(lock) {
            // If another thread already refreshed within the last 5 seconds,
            // just retry with the current access token instead of refreshing again
            val now = System.currentTimeMillis()
            if (now - lastRefreshTimeMs < REFRESH_WINDOW_MS) {
                val currentToken = runBlocking { tokenManager.getAccessToken() }
                return if (currentToken != null) {
                    response.request.newBuilder()
                        .header("Authorization", "Bearer $currentToken")
                        .build()
                } else {
                    null
                }
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
                            lastRefreshTimeMs = System.currentTimeMillis()
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

    private fun isPublicEndpoint(path: String): Boolean {
        return PUBLIC_PATHS.any { path.contains(it) }
    }

    private companion object {
        const val MAX_RETRY = 2
        const val REFRESH_WINDOW_MS = 5_000L
        val PUBLIC_PATHS = listOf(
            "/auth/sign-up",
            "/auth/login",
            "/auth/refresh",
        )
    }
}
