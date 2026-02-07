package com.team.chord.core.network.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        if (isPublicEndpoint(path)) {
            return chain.proceed(request)
        }

        val token = runBlocking { tokenManager.getAccessToken() }

        val authenticatedRequest = if (token != null) {
            request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }

        return chain.proceed(authenticatedRequest)
    }

    private fun isPublicEndpoint(path: String): Boolean {
        return PUBLIC_PATHS.any { path.contains(it) }
    }

    private companion object {
        val PUBLIC_PATHS = listOf(
            "/auth/sign-up",
            "/auth/login",
            "/auth/refresh",
        )
    }
}
