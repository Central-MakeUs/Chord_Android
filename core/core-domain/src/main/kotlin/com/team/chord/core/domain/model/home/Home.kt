package com.team.chord.core.domain.model.home

data class HomeMenus(
    val numOfDangerMenus: Int,
    val avgCostRate: Double,
    val avgMarginRate: Double,
    val avgCostRateGradeCode: String,
)

data class HomeStrategyBrief(
    val menuId: Long,
    val menuName: String,
    val strategyId: Long,
    val summary: String,
)
