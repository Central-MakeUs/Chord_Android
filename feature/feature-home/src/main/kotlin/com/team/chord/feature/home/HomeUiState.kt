package com.team.chord.feature.home

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val ctaTitle: String,
        val todoSectionTitle: String,
        val todoCardTitle: String,
        val todoItems: List<HomeTodoItem>,
        val statsSectionTitle: String,
        val stats: List<HomeStatItem>,
    ) : HomeUiState

    data class Error(
        val message: String,
    ) : HomeUiState
}

data class HomeTodoItem(
    val title: String,
    val subtitle: String,
)

data class HomeStatItem(
    val title: String,
    val value: String,
    val description: String,
)
