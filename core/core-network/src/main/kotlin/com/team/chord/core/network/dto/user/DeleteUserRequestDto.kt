package com.team.chord.core.network.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class DeleteUserRequestDto(
    val accessToken: String? = null,
)
