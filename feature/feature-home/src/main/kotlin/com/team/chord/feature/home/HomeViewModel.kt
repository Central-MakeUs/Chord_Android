package com.team.chord.feature.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState =
            MutableStateFlow<HomeUiState>(
                HomeUiState.Success(
                    ctaTitle = "진단이 필요한 메뉴",
                    ctaCount = 3,
                    strategySectionTitle = "전략 가이드",
                    strategyItems =
                        listOf(
                            HomeStrategyItem(
                                description = "원가율 35% 유지 가능해요",
                                menuName = "바닐라 라떼",
                                actionLabel = "판매가 조정",
                            ),
                            HomeStrategyItem(
                                description = "공헌이익률이 낮아요",
                                menuName = "아메리카노",
                                actionLabel = "원가 분석",
                            ),
                            HomeStrategyItem(
                                description = "판매량 대비 수익이 적어요",
                                menuName = "카페 모카",
                                actionLabel = "가격 전략",
                            ),
                        ),
                    statsSectionTitle = "수익 진단",
                    stats =
                        listOf(
                            HomeStatItem("평균 원가율", "28.5%", "안정적"),
                            HomeStatItem("평균 공헌이익률", "+12%", "지난주 대비 상승"),
                        ),
                ),
            )
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    }
