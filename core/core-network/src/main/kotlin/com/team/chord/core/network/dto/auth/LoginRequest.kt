package com.team.chord.core.network.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val loginId: String,
    val password: String,
)
