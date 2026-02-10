package com.team.chord.core.network.dto.strategy

import kotlinx.serialization.Serializable

@Serializable
data class StrategyDto(
    val strategyId: Long,
    val summary: String? = null,
    val detail: String? = null,
    val weekLabel: String? = null,
    val state: String? = null,
    val type: String? = null,
    val year: Int? = null,
    val month: Int? = null,
    val weekOfMonth: Int? = null,
    val createdAt: String? = null,
    val completionDate: String? = null,
    val isSaved: Boolean? = null,
)
