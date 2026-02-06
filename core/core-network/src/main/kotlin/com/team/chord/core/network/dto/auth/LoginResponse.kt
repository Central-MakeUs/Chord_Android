package com.team.chord.core.network.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val onboardingCompleted: Boolean,
)
