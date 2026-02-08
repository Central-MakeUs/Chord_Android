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
                    storeName = "코치카페",
                    heroTitle = "코치카페의 수익 상황을\n확인하세요",
                    ctaTitle = "진단이 필요한 메뉴",
                    ctaCount = 3,
                    stats =
                        listOf(
                            HomeStatItem("평균 원가율", "안정", "27.23%"),
                            HomeStatItem("평균마진율", "안정", "27.23%"),
                        ),
                    strategyItems =
                        listOf(
                            HomeStrategyItem(menuName = "바닐라 라떼", subtitle = "전략 상세"),
                            HomeStrategyItem(menuName = "바닐라 라떼", subtitle = "전략 상세"),
                            HomeStrategyItem(menuName = "바닐라 라떼", subtitle = "전략 상세"),
                        ),
                ),
            )
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    }
