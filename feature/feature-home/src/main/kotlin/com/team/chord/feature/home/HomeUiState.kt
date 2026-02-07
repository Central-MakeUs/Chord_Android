package com.team.chord.feature.home

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val ctaTitle: String,
        val ctaCount: Int,
        val strategySectionTitle: String,
        val strategyItems: List<HomeStrategyItem>,
        val statsSectionTitle: String,
        val stats: List<HomeStatItem>,
    ) : HomeUiState

    data class Error(
        val message: String,
    ) : HomeUiState
}

data class HomeStrategyItem(
    val description: String,
    val menuName: String,
    val actionLabel: String,
)

data class HomeStatItem(
    val title: String,
    val value: String,
    val description: String,
)
