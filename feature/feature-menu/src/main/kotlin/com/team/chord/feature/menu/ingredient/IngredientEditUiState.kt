package com.team.chord.feature.menu.ingredient

import com.team.chord.core.domain.model.menu.IngredientUnit
import kotlin.math.abs

data class IngredientEditUiState(
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val menuId: Long = 0L,
    val menuName: String = "",
    val recipes: List<EditableRecipeUi> = emptyList(),
    val sheetState: EditIngredientSheetUi? = null,
    val toastMessage: String? = null,
    val errorMessage: String? = null,
)

data class EditableRecipeUi(
    val recipeId: Long,
    val ingredientId: Long,
    val name: String,
    val amount: Double,
    val unit: IngredientUnit,
    val price: Int,
)

data class EditIngredientSheetUi(
    val recipe: EditableRecipeUi,
    val amountInput: String,
    val unitPriceText: String = "-",
    val supplierText: String = "-",
    val isIngredientInfoLoading: Boolean = false,
) {
    val parsedAmount: Double?
        get() = amountInput.toDoubleOrNull()

    val isAmountChanged: Boolean
        get() = parsedAmount?.let { abs(it - recipe.amount) > 0.000001 } == true

    val isSubmitEnabled: Boolean
        get() {
            val value = parsedAmount ?: return false
            return value > 0 && isAmountChanged
        }
}
