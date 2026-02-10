package com.team.chord.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.usecase.strategy.GetWeeklyStrategiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getWeeklyStrategiesUseCase: GetWeeklyStrategiesUseCase,
    ) : ViewModel() {
        private val _uiState =
            MutableStateFlow<HomeUiState>(
                HomeUiState.Success(
                    storeName = "코치카페",
                    heroTitle = "코치카페의 수익 상황을\n확인하세요",
                    ctaTitle = "진단이 필요한 메뉴",
                    ctaCount = 3,
                    stats =
                        listOf(
                            HomeStatItem("평균 원가율", "안정", "27.23%"),
                            HomeStatItem("평균마진율", "안정", "27.23%"),
                        ),
                    strategyItems = emptyList(),
                ),
            )
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

        init {
            loadStrategies()
        }

        private fun loadStrategies() {
            viewModelScope.launch {
                val current = _uiState.value as? HomeUiState.Success ?: return@launch
                try {
                    val now = LocalDate.now()
                    val weekOfMonth = now.get(WeekFields.of(Locale.getDefault()).weekOfMonth()).coerceAtLeast(1)
                    val strategyItems = getWeeklyStrategiesUseCase(
                        year = now.year,
                        month = now.monthValue,
                        weekOfMonth = weekOfMonth,
                    )
                        .take(3)
                        .map { strategy ->
                            HomeStrategyItem(
                                menuName = strategy.title,
                                subtitle = strategy.description.ifBlank { "전략 상세" },
                            )
                        }
                    _uiState.update {
                        current.copy(strategyItems = strategyItems)
                    }
                } catch (_: Exception) {
                    // Keep default UI when strategy fetch fails.
                }
            }
        }
    }
