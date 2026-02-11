package com.team.chord.feature.aicoach.strategy

import java.time.YearMonth

data class AiStrategyUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val recommendedStrategies: List<RecommendedStrategyUi> = emptyList(),
    val selectedMonth: YearMonth = YearMonth.now(),
    val selectedFilter: StrategyFilter = StrategyFilter.COMPLETED,
    val historyItems: List<StrategyHistoryItemUi> = emptyList(),
)

data class RecommendedStrategyUi(
    val id: Long,
    val title: String,
    val description: String,
    val status: StrategyStatus,
)

data class StrategyHistoryItemUi(
    val id: Long,
    val weekLabel: String,
    val title: String,
    val description: String,
)

enum class StrategyStatus {
    IN_PROGRESS,
    NOT_STARTED,
    COMPLETED,
}

enum class StrategyFilter(val displayName: String) {
    COMPLETED("실행 완료"),
    INCOMPLETE("미완료"),
}
