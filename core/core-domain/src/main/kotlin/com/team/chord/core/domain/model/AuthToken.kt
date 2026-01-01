package com.team.chord.core.domain.model

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Long,
)
