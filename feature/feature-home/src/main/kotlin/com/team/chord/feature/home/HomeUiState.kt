package com.team.chord.feature.home

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val storeName: String,
        val heroTitle: String,
        val ctaTitle: String,
        val ctaCount: Int,
        val stats: List<HomeStatItem>,
        val strategyItems: List<HomeStrategyItem>,
    ) : HomeUiState

    data class Error(
        val message: String,
    ) : HomeUiState
}

data class HomeStrategyItem(
    val menuName: String,
    val subtitle: String,
)

data class HomeStatItem(
    val title: String,
    val statusLabel: String,
    val value: String,
)
