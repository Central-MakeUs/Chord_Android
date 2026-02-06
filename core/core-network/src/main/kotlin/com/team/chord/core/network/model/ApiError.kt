package com.team.chord.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val success: Boolean,
    val code: String,
    val message: String,
    val errors: Map<String, String>? = null,
    val timestamp: String = "",
)
