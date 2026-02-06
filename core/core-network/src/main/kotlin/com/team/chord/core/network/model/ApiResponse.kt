package com.team.chord.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T,
    val timestamp: String = "",
    val message: String? = null,
)
