package com.team.chord.feature.home

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val message: String,
    ) : HomeUiState

    data class Error(
        val message: String,
    ) : HomeUiState
}
