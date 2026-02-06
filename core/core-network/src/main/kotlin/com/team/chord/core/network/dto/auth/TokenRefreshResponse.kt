package com.team.chord.core.network.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class TokenRefreshResponse(
    val accessToken: String,
)
