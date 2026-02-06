package com.team.chord.core.network.api

import com.team.chord.core.network.dto.auth.LoginRequest
import com.team.chord.core.network.dto.auth.LoginResponse
import com.team.chord.core.network.dto.auth.SignUpRequest
import com.team.chord.core.network.dto.auth.TokenRefreshRequest
import com.team.chord.core.network.dto.auth.TokenRefreshResponse
import com.team.chord.core.network.model.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/sign-up")
    suspend fun signUp(@Body request: SignUpRequest): Response<ApiResponse<Unit>>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: TokenRefreshRequest): Response<ApiResponse<TokenRefreshResponse>>
}
