package com.team.chord.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.home.HomeMenus
import com.team.chord.core.domain.model.home.HomeStrategyBrief
import com.team.chord.core.domain.usecase.home.GetHomeMenusUseCase
import com.team.chord.core.domain.usecase.home.GetHomeStrategiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getHomeMenusUseCase: GetHomeMenusUseCase,
        private val getHomeStrategiesUseCase: GetHomeStrategiesUseCase,
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
            loadHome()
        }

        private fun loadHome() {
            viewModelScope.launch {
                val current = _uiState.value as? HomeUiState.Success ?: return@launch
                val now = LocalDate.now()
                val weekOfMonth = now.get(WeekFields.of(Locale.getDefault()).weekOfMonth()).coerceAtLeast(1)

                val nextState = supervisorScope {
                    val homeMenusResultDeferred = async {
                        runCatching { getHomeMenusUseCase() }
                    }
                    val homeStrategiesResultDeferred = async {
                        runCatching {
                            getHomeStrategiesUseCase(
                                year = now.year,
                                month = now.monthValue,
                                weekOfMonth = weekOfMonth,
                            )
                        }
                    }

                    val homeMenus = homeMenusResultDeferred.await().getOrNull()
                    val homeStrategies = homeStrategiesResultDeferred.await().getOrNull()

                    current.copy(
                        ctaCount = homeMenus?.numOfDangerMenus ?: current.ctaCount,
                        stats = homeMenus?.let(::buildStats) ?: current.stats,
                        strategyItems = homeStrategies
                            ?.take(3)
                            ?.map(::mapToHomeStrategyItem)
                            ?: current.strategyItems,
                    )
                }

                _uiState.update { nextState }
            }
        }

        private fun buildStats(homeMenus: HomeMenus): List<HomeStatItem> {
            val statusLabel = resolveStatusLabel(homeMenus.avgCostRateGradeCode)

            return listOf(
                HomeStatItem(
                    title = "평균 원가율",
                    statusLabel = statusLabel,
                    value = formatPercent(homeMenus.avgCostRate),
                ),
                HomeStatItem(
                    title = "평균마진율",
                    statusLabel = statusLabel,
                    value = formatPercent(homeMenus.avgMarginRate),
                ),
            )
        }

        private fun mapToHomeStrategyItem(strategy: HomeStrategyBrief): HomeStrategyItem =
            HomeStrategyItem(
                menuName = strategy.menuName.ifBlank { "메뉴" },
                subtitle = strategy.summary.ifBlank { "전략 상세" },
            )

        private fun resolveStatusLabel(gradeCode: String): String =
            when (gradeCode.trim().uppercase(Locale.ROOT)) {
                "SAFE" -> "안정"
                "MID" -> "보통"
                "WARNING" -> "주의"
                "DANGER" -> "위험"
                else -> "보통"
            }

        private fun formatPercent(value: Double): String =
            String.format(Locale.KOREA, "%.2f%%", value)
    }
