package com.team.chord.feature.menu.detail

import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.domain.model.menu.MarginGrade

sealed interface MenuDetailUiState {
    data object Loading : MenuDetailUiState

    data class Success(
        val menuDetail: MenuDetailUi,
        val showDropdownMenu: Boolean = false,
        val showDeleteDialog: Boolean = false,
        val showDeleteSuccessDialog: Boolean = false,
    ) : MenuDetailUiState

    data class Error(
        val message: String,
    ) : MenuDetailUiState
}

data class MenuDetailUi(
    val id: Long,
    val name: String,
    val sellingPrice: Int,
    val preparationTimeSeconds: Int,
    val totalCost: Int,
    val costRatio: Float,
    val marginRatio: Float,
    val contributionProfit: Int,
    val marginGrade: MarginGrade,
    val recommendedPrice: Int?,
    val recommendedPriceMessage: String?,
    val ingredients: List<IngredientUi>,
)

data class IngredientUi(
    val id: Long,
    val name: String,
    val quantity: Double,
    val unit: IngredientUnit,
    val price: Int,
)
