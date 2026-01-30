package com.team.chord.feature.aicoach.strategy

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class AiStrategyViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AiStrategyUiState())
    val uiState: StateFlow<AiStrategyUiState> = _uiState.asStateFlow()

    init {
        loadMockData()
    }

    fun onMonthChange(yearMonth: YearMonth) {
        _uiState.value = _uiState.value.copy(selectedMonth = yearMonth)
        loadHistoryForMonth(yearMonth)
    }

    fun onFilterChange(filter: StrategyFilter) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
    }

    private fun loadMockData() {
        _uiState.value = _uiState.value.copy(
            recommendedStrategies = listOf(
                RecommendedStrategyUi(
                    id = 1,
                    title = "원가 절감 전략",
                    description = "재료 대량구매로 15% 절감",
                    status = StrategyStatus.IN_PROGRESS,
                ),
                RecommendedStrategyUi(
                    id = 2,
                    title = "메뉴 최적화",
                    description = "인기 메뉴 중심 구성",
                    status = StrategyStatus.NOT_STARTED,
                ),
                RecommendedStrategyUi(
                    id = 3,
                    title = "재고 관리",
                    description = "폐기율 5% 이하 달성",
                    status = StrategyStatus.NOT_STARTED,
                ),
            ),
            historyItems = listOf(
                StrategyHistoryItemUi(
                    id = 4,
                    weekLabel = "1월 4주차",
                    title = "재고 관리 최적화",
                    description = "폐기율 5% 이하 달성",
                ),
                StrategyHistoryItemUi(
                    id = 3,
                    weekLabel = "1월 3주차",
                    title = "고객 분석",
                    description = "단골 고객 패턴 분석 완료",
                ),
                StrategyHistoryItemUi(
                    id = 2,
                    weekLabel = "1월 2주차",
                    title = "메뉴 최적화",
                    description = "인기 메뉴 중심으로 재구성",
                ),
                StrategyHistoryItemUi(
                    id = 1,
                    weekLabel = "1월 1주차",
                    title = "원가 절감 전략",
                    description = "재료 대량구매로 15% 절감 달성",
                ),
            ),
        )
    }

    private fun loadHistoryForMonth(yearMonth: YearMonth) {
        // In real implementation, this would fetch data from repository
    }
}
