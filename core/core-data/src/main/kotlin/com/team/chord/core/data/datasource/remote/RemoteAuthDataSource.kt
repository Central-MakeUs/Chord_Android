package com.team.chord.core.data.datasource.remote

import com.team.chord.core.data.datasource.AuthDataSource
import com.team.chord.core.data.datasource.LoginResult
import com.team.chord.core.network.api.AuthApi
import com.team.chord.core.network.dto.auth.LoginRequest
import com.team.chord.core.network.dto.auth.SignUpRequest
import com.team.chord.core.network.dto.auth.TokenRefreshRequest
import com.team.chord.core.network.util.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteAuthDataSource @Inject constructor(
    private val authApi: AuthApi,
) : AuthDataSource {

    override suspend fun signUp(loginId: String, password: String) {
        safeApiCall { authApi.signUp(SignUpRequest(loginId, password)) }
    }

    override suspend fun login(loginId: String, password: String): LoginResult {
        val response = safeApiCall { authApi.login(LoginRequest(loginId, password)) }
        return LoginResult(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
            onboardingCompleted = response.onboardingCompleted,
        )
    }

    override suspend fun refreshToken(refreshToken: String): String {
        val response = safeApiCall { authApi.refreshToken(TokenRefreshRequest(refreshToken)) }
        return response.accessToken
    }
}
