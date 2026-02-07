package com.team.chord.core.network.api

import com.team.chord.core.network.dto.user.OnboardingRequestDto
import com.team.chord.core.network.model.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH

interface UserApi {
    @PATCH("users/onboarding")
    suspend fun completeOnboarding(@Body request: OnboardingRequestDto): Response<ApiResponse<Unit>>
}
