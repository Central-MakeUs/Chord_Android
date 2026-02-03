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
                    ctaTitle = "AI 진단 시작하기",
                    todoSectionTitle = "오늘의 할 일",
                    todoCardTitle = "진단 전 체크리스트",
                    todoItems =
                        listOf(
                            HomeTodoItem("메뉴 원가 입력", "마지막 업데이트 3일 전"),
                            HomeTodoItem("재료 단가 확인", "오늘 14:00까지"),
                            HomeTodoItem("판매 데이터 업로드", "어제 기준"),
                        ),
                    statsSectionTitle = "이번 주 요약",
                    stats =
                        listOf(
                            HomeStatItem("원가율", "26%", "목표 30%"),
                            HomeStatItem("진단 점수", "82점", "지난주 +6"),
                        ),
                ),
            )
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    }
