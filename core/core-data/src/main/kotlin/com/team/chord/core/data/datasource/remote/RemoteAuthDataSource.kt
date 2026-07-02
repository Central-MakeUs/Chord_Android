package com.team.chord.core.data.datasource.remote

import com.team.chord.core.data.datasource.AuthDataSource
import com.team.chord.core.data.datasource.LoginResult
import com.team.chord.core.network.api.AuthApi
import com.team.chord.core.network.dto.auth.KakaoLoginRequest
import com.team.chord.core.network.dto.auth.LoginRequest
import com.team.chord.core.network.dto.auth.LoginResponse
import com.team.chord.core.network.dto.auth.NaverLoginRequest
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
        return response.toLoginResult()
    }

    override suspend fun kakaoLogin(accessToken: String): LoginResult {
        val response = safeApiCall { authApi.kakaoLogin(KakaoLoginRequest(accessToken = accessToken)) }
        return response.toLoginResult()
    }

    override suspend fun naverLogin(accessToken: String): LoginResult {
        val response = safeApiCall { authApi.naverLogin(NaverLoginRequest(accessToken = accessToken)) }
        return response.toLoginResult()
    }

    private fun LoginResponse.toLoginResult(): LoginResult {
        return LoginResult(
            accessToken = accessToken,
            refreshToken = refreshToken,
            onboardingCompleted = onboardingCompleted,
        )
    }

    override suspend fun refreshToken(refreshToken: String): String {
        val response = safeApiCall { authApi.refreshToken(TokenRefreshRequest(refreshToken)) }
        return response.accessToken
    }
}
