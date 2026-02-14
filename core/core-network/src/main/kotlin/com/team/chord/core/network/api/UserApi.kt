package com.team.chord.core.network.api

import com.team.chord.core.network.dto.user.OnboardingRequestDto
import com.team.chord.core.network.dto.user.StoreResponseDto
import com.team.chord.core.network.dto.user.UpdateStoreRequestDto
import com.team.chord.core.network.model.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApi {
    @DELETE("users/me")
    suspend fun deleteMe(): Response<ApiResponse<Unit>>

    @PATCH("users/onboarding")
    suspend fun completeOnboarding(@Body request: OnboardingRequestDto): Response<ApiResponse<Unit>>

    @GET("users/stores")
    suspend fun getStore(): Response<ApiResponse<StoreResponseDto>>

    @PATCH("users/stores")
    suspend fun updateStore(@Body request: UpdateStoreRequestDto): Response<ApiResponse<Unit>>
}
