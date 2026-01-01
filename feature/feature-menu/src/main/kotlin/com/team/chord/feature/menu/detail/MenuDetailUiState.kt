package com.team.chord.feature.menu.detail

import com.team.chord.feature.menu.list.MenuStatus

sealed interface MenuDetailUiState {
    data object Loading : MenuDetailUiState

    data class Success(
        val menuDetail: MenuDetailUi,
    ) : MenuDetailUiState

    data class Error(
        val message: String,
    ) : MenuDetailUiState
}

data class MenuDetailUi(
    val id: Long,
    val name: String,
    val sellingPrice: Int,
    val totalCost: Int,
    val costRatio: Float,
    val marginRatio: Float,
    val contributionProfit: Int,
    val status: MenuStatus,
    val ingredients: List<IngredientUi>,
)

data class IngredientUi(
    val name: String,
    val quantity: String,
    val price: Int,
)
