package com.team.chord.feature.aicoach.strategy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.strategy.Strategy
import com.team.chord.core.domain.model.strategy.StrategyProgressStatus
import com.team.chord.core.domain.usecase.strategy.GetSavedStrategiesUseCase
import com.team.chord.core.domain.usecase.strategy.GetWeeklyStrategiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiStrategyViewModel @Inject constructor(
    private val getWeeklyStrategiesUseCase: GetWeeklyStrategiesUseCase,
    private val getSavedStrategiesUseCase: GetSavedStrategiesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AiStrategyUiState())
    val uiState: StateFlow<AiStrategyUiState> = _uiState.asStateFlow()

    init {
        loadStrategies()
    }

    fun refresh() {
        loadStrategies(isRefresh = true)
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onMonthChange(yearMonth: YearMonth) {
        _uiState.update { it.copy(selectedMonth = yearMonth) }
        loadStrategies()
    }

    fun onFilterChange(filter: StrategyFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
        loadSavedStrategies()
    }

    private fun loadStrategies(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update {
                if (isRefresh) it.copy(isRefreshing = true)
                else it.copy(isLoading = true)
            }

            try {
                val month = _uiState.value.selectedMonth
                val weekOfMonth = resolveWeekOfMonth(month)
                val isCompleted = _uiState.value.selectedFilter == StrategyFilter.COMPLETED

                val weeklyStrategies = getWeeklyStrategiesUseCase(
                    year = month.year,
                    month = month.monthValue,
                    weekOfMonth = weekOfMonth,
                )
                val savedStrategies = getSavedStrategiesUseCase(
                    year = month.year,
                    month = month.monthValue,
                    isCompleted = isCompleted,
                )

                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        isRefreshing = false,
                        generatedAtMessage = formatGeneratedAtMessage(weeklyStrategies),
                        recommendedStrategies = weeklyStrategies.map { it.toRecommendedUi() },
                        historyItems = savedStrategies.map { it.toHistoryUi() },
                        errorMessage = null,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = e.message ?: "전략을 불러오지 못했어요.",
                    )
                }
            }
        }
    }

    private fun loadSavedStrategies(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update {
                if (isRefresh) it.copy(isRefreshing = true)
                else it.copy(isLoading = true)
            }

            try {
                val month = _uiState.value.selectedMonth
                val savedStrategies = getSavedStrategiesUseCase(
                    year = month.year,
                    month = month.monthValue,
                    isCompleted = _uiState.value.selectedFilter == StrategyFilter.COMPLETED,
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        historyItems = savedStrategies.map { strategy -> strategy.toHistoryUi() },
                        errorMessage = null,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = e.message ?: "저장된 전략을 불러오지 못했어요.",
                    )
                }
            }
        }
    }

    private fun resolveWeekOfMonth(yearMonth: YearMonth): Int {
        val now = LocalDate.now()
        val baseDate = if (now.year == yearMonth.year && now.monthValue == yearMonth.monthValue) {
            now
        } else {
            yearMonth.atDay(1)
        }

        val week = baseDate.get(WeekFields.of(Locale.getDefault()).weekOfMonth())
        return week.coerceAtLeast(1)
    }

    private fun formatGeneratedAtMessage(strategies: List<Strategy>): String {
        val createdAt = strategies.maxByOrNull { it.createdAt ?: LocalDateTime.MIN }?.createdAt ?: LocalDateTime.now()
        return "${createdAt.monthValue}월 ${createdAt.dayOfMonth}일 ${createdAt.hour.toString().padStart(2, '0')}시 기준으로 생성된 전략이에요!"
    }
}

private fun Strategy.toRecommendedUi(): RecommendedStrategyUi =
    RecommendedStrategyUi(
        id = id,
        state = when (status) {
            StrategyProgressStatus.IN_PROGRESS -> StrategyState.IN_PROGRESS
            StrategyProgressStatus.COMPLETED -> StrategyState.COMPLETED
            StrategyProgressStatus.NOT_STARTED -> StrategyState.NOT_STARTED
        },
        title = title,
        description = type.toStrategyDescription(),
        type = type,
    )

private fun Strategy.toHistoryUi(): StrategyHistoryItemUi =
    StrategyHistoryItemUi(
        id = id,
        weekLabel = weekLabel ?: "전략 히스토리",
        title = title,
        description = type.toStrategyDescription(),
        type = type,
    )

private fun String.toStrategyDescription(): String =
    when (uppercase(Locale.ROOT)) {
        "DANGER" -> "원가를 위험 메뉴 확인"
        "HIGH_MARGIN" -> "우리 카페 고마진 메뉴 확인"
        else -> "원가율 주의 메뉴 확인"
    }
