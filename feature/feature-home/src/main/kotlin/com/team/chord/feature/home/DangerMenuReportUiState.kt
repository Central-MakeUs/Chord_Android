package com.team.chord.feature.home

data class DangerMenuReportUiState(
    val isLoading: Boolean = true,
    val dateLabel: String = "",
    val isStable: Boolean = false,
    val menus: List<DangerMenuReportMenuUi> = emptyList(),
    val errorMessage: String? = null,
)

data class DangerMenuReportMenuUi(
    val strategyId: Long,
    val menuName: String,
    val costRateText: String,
    val marginRateText: String,
)
