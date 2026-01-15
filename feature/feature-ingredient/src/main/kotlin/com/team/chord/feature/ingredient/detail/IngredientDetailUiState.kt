package com.team.chord.feature.ingredient.detail

import com.team.chord.core.domain.model.menu.IngredientUnit

sealed interface IngredientDetailUiState {
    data object Loading : IngredientDetailUiState

    data class Success(
        val ingredientDetail: IngredientDetailUi,
    ) : IngredientDetailUiState

    data class Error(
        val message: String,
    ) : IngredientDetailUiState
}

data class IngredientDetailUi(
    val id: Long,
    val name: String,
    val price: Int,
    val unitAmount: Int,
    val unit: IngredientUnit,
    val supplier: String,
    val isFavorite: Boolean,
    val usedMenus: List<UsedMenuUi>,
    val priceHistory: List<PriceHistoryUi>,
    val isDeleted: Boolean = false,
)

data class UsedMenuUi(
    val id: Long,
    val name: String,
)

data class PriceHistoryUi(
    val id: Long,
    val date: String,
    val price: Int,
    val unitAmount: Int,
    val unitDisplayName: String,
)
