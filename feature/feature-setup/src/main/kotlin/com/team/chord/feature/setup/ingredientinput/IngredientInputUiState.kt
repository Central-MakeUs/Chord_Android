package com.team.chord.feature.setup.ingredientinput

import com.team.chord.core.domain.model.menu.IngredientUnit

/**
 * UI State for IngredientInputScreen
 */
data class IngredientInputUiState(
    val searchQuery: String = "",
    val searchResults: List<IngredientSuggestion> = emptyList(),
    val selectedIngredients: List<SelectedIngredient> = emptyList(),
    val showAddBottomSheet: Boolean = false,
    val bottomSheetIngredient: IngredientBottomSheetState? = null,
    val isNextEnabled: Boolean = true,
    val isSearching: Boolean = false,
)

/**
 * Suggestion item from search results
 */
data class IngredientSuggestion(
    val ingredientId: Long? = null,
    val templateId: Long? = null,
    val name: String,
    val isTemplate: Boolean,
)

/**
 * Selected ingredient with all required information
 */
data class SelectedIngredient(
    val id: Long,
    val name: String,
    val amount: Int,
    val unit: IngredientUnit,
    val price: Int,
    val categoryCode: String = "FOOD_MATERIAL",
    val supplier: String = "",
)

/**
 * State for the add ingredient bottom sheet
 */
data class IngredientBottomSheetState(
    val id: Long? = null,
    val name: String,
    val categoryCode: String = "FOOD_MATERIAL",
    val price: String = "",
    val amount: String = "",
    val unit: IngredientUnit = IngredientUnit.G,
    val supplier: String = "",
    val isTemplate: Boolean = false,
    val suggestedPrice: Int? = null,
    val suggestedAmount: Int? = null,
    val suggestedUnit: IngredientUnit? = null,
) {
    val isAddEnabled: Boolean
        get() = price.isNotBlank() && amount.isNotBlank() && amount.toIntOrNull() != null
}
