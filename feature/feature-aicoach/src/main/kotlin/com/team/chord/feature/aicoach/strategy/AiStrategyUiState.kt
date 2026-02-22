package com.team.chord.feature.aicoach.strategy

import java.time.YearMonth

data class AiStrategyUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val generatedAtMessage: String = "",
    val recommendedStrategies: List<RecommendedStrategyUi> = emptyList(),
    val selectedMonth: YearMonth = YearMonth.now(),
    val selectedFilter: StrategyFilter = StrategyFilter.COMPLETED,
    val historyItems: List<StrategyHistoryItemUi> = emptyList(),
    val recommendedEmptyTitle: String = "잘 하고 있어요!",
    val recommendedEmptyDescription: String = "현재 매장 운영이 안정적으로 유지되어 별도의 진단이 없어요.",
    val historyEmptyMessage: String = "아직 실행 완료된 전략이 없어요.\n전략을 실행해보세요!",
)

data class RecommendedStrategyUi(
    val id: Long,
    val state: StrategyState,
    val title: String,
    val description: String,
    val type: String,
)

data class StrategyHistoryItemUi(
    val id: Long,
    val weekLabel: String,
    val title: String,
    val description: String,
    val type: String,
)

enum class StrategyState {
    IN_PROGRESS,
    NOT_STARTED,
    COMPLETED,
}

enum class StrategyFilter(val displayName: String) {
    COMPLETED("실행 완료"),
    INCOMPLETE("미완료"),
}
