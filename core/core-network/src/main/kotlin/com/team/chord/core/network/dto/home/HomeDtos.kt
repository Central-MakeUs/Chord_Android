package com.team.chord.core.network.dto.home

import kotlinx.serialization.Serializable

@Serializable
data class HomeMenusResponseDto(
    val numOfDangerMenus: Int? = null,
    val avgCostRate: AvgCostRateDto? = null,
    val avgMarginRate: Double? = null,
)

@Serializable
data class AvgCostRateDto(
    val avgCostRate: Double? = null,
    val marginGradeCode: String? = null,
)

@Serializable
data class HomeStrategiesResponseDto(
    val strategies: List<HomeStrategyBriefDto>? = null,
)

@Serializable
data class HomeStrategyBriefDto(
    val menuId: Long? = null,
    val menuName: String? = null,
    val strategyId: Long? = null,
    val state: String? = null,
    val type: String? = null,
    val summary: String? = null,
    val createdAt: String? = null,
)
