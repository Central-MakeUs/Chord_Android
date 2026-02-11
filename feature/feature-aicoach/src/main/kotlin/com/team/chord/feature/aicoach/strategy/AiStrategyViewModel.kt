package com.team.chord.feature.aicoach.strategy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.strategy.Strategy
import com.team.chord.core.domain.model.strategy.StrategyProgressStatus
import com.team.chord.core.domain.usecase.strategy.GetSavedStrategiesUseCase
import com.team.chord.core.domain.usecase.strategy.GetWeeklyStrategiesUseCase
import com.team.chord.core.domain.usecase.strategy.SaveStrategyUseCase
import com.team.chord.core.domain.usecase.strategy.StartStrategyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AiStrategyViewModel @Inject constructor(
    private val getWeeklyStrategiesUseCase: GetWeeklyStrategiesUseCase,
    private val getSavedStrategiesUseCase: GetSavedStrategiesUseCase,
    private val startStrategyUseCase: StartStrategyUseCase,
    private val saveStrategyUseCase: SaveStrategyUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AiStrategyUiState())
    val uiState: StateFlow<AiStrategyUiState> = _uiState.asStateFlow()

    private var weeklyStrategies: List<Strategy> = emptyList()
    private var savedStrategies: List<Strategy> = emptyList()

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
        _uiState.value = _uiState.value.copy(selectedMonth = yearMonth)
        loadStrategies()
    }

    fun onFilterChange(filter: StrategyFilter) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
        loadSavedStrategies()
    }

    fun onStartStrategy(strategyId: Long) {
        val strategy = weeklyStrategies.find { it.id == strategyId } ?: return
        viewModelScope.launch {
            when (startStrategyUseCase(strategyId, strategy.type)) {
                is Result.Success -> loadStrategies()
                is Result.Error,
                is Result.Loading -> Unit
            }
        }
    }

    fun onSaveStrategy(strategyId: Long, isSaved: Boolean) {
        val strategy = weeklyStrategies.find { it.id == strategyId } ?: return
        viewModelScope.launch {
            when (saveStrategyUseCase(strategyId, strategy.type, isSaved)) {
                is Result.Success -> loadStrategies()
                is Result.Error,
                is Result.Loading -> Unit
            }
        }
    }

    private fun loadStrategies(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update {
                if (isRefresh) it.copy(isRefreshing = true)
                else it.copy(isLoading = true)
            }
            try {
                val month = _uiState.value.selectedMonth
                val isCompleted = _uiState.value.selectedFilter == StrategyFilter.COMPLETED
                val weekOfMonth = resolveWeekOfMonth(month)

                weeklyStrategies = getWeeklyStrategiesUseCase(
                    year = month.year,
                    month = month.monthValue,
                    weekOfMonth = weekOfMonth,
                )
                savedStrategies = getSavedStrategiesUseCase(
                    year = month.year,
                    month = month.monthValue,
                    isCompleted = isCompleted,
                )

                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        isRefreshing = false,
                        recommendedStrategies = weeklyStrategies.map { it.toRecommendedUi() },
                        historyItems = savedStrategies.map { strategy -> strategy.toHistoryUi() },
                        errorMessage = null,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = e.message ?: "전략을 불러오는데 실패했습니다",
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
                savedStrategies = getSavedStrategiesUseCase(
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
                        errorMessage = e.message ?: "저장된 전략을 불러오는데 실패했습니다",
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
}

private fun Strategy.toRecommendedUi(): RecommendedStrategyUi =
    RecommendedStrategyUi(
        id = id,
        title = title,
        description = description,
        status = when (status) {
            StrategyProgressStatus.IN_PROGRESS -> StrategyStatus.IN_PROGRESS
            StrategyProgressStatus.COMPLETED -> StrategyStatus.COMPLETED
            StrategyProgressStatus.NOT_STARTED -> StrategyStatus.NOT_STARTED
        },
    )

private fun Strategy.toHistoryUi(): StrategyHistoryItemUi =
    StrategyHistoryItemUi(
        id = id,
        weekLabel = weekLabel ?: "전략 히스토리",
        title = title,
        description = description,
    )
