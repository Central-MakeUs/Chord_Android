package com.team.chord.feature.setup.ingredientinput

import com.team.chord.core.domain.model.menu.IngredientUnit

/**
 * Source type of an ingredient in the setup flow.
 */
enum class IngredientSourceType {
    /** Brand new ingredient - all fields editable */
    NEW,
    /** From menu template - only amount editable, unit/price/category locked */
    TEMPLATE,
    /** Already saved ingredient reuse - only amount editable, everything else locked */
    SAVED,
}

/**
 * UI State for IngredientInputScreen
 */
data class IngredientInputUiState(
    val searchQuery: String = "",
    val searchResults: List<IngredientSuggestion> = emptyList(),
    val selectedIngredients: List<SelectedIngredient> = emptyList(),
    val showBottomSheet: Boolean = false,
    val bottomSheetIngredient: IngredientBottomSheetState? = null,
    val isNextEnabled: Boolean = true,
    val isSearching: Boolean = false,
    val isTemplateApplied: Boolean = false,
    val showCompletionToast: Boolean = false,
    val completionToastMessage: String = "",
)

/**
 * Suggestion item from search results
 */
data class IngredientSuggestion(
    val ingredientId: Long? = null,
    val templateId: Long? = null,
    val name: String,
    val sourceType: IngredientSourceType,
    val categoryCode: String? = null,
    val unitPrice: Int? = null,
    val unitCode: String? = null,
    val baseQuantity: Int? = null,
    val supplier: String? = null,
)

/**
 * Selected ingredient with all required information
 */
data class SelectedIngredient(
    val id: Long,
    val serverIngredientId: Long? = null,
    val name: String,
    val amount: Int,
    val unit: IngredientUnit,
    val price: Int,
    val categoryCode: String = "INGREDIENTS",
    val supplier: String = "",
    val sourceType: IngredientSourceType = IngredientSourceType.NEW,
    val baseQuantity: Int = 0,
    val unitPrice: Int = 0,
    val templateRecipeId: Long? = null,
)

/**
 * State for the add/edit ingredient bottom sheet
 */
data class IngredientBottomSheetState(
    val serverIngredientId: Long? = null,
    val name: String,
    val categoryCode: String = "INGREDIENTS",
    val price: String = "",
    val purchaseAmount: String = "",
    val amount: String = "",
    val unit: IngredientUnit = IngredientUnit.G,
    val supplier: String = "",
    val sourceType: IngredientSourceType = IngredientSourceType.NEW,
    val suggestedPrice: Int? = null,
    val suggestedPurchaseAmount: Int? = null,
    val suggestedAmount: Int? = null,
    val suggestedUnit: IngredientUnit? = null,
    val isEditMode: Boolean = false,
    val editingIngredientId: Long? = null,
) {
    val isPriceEditable: Boolean
        get() = true

    val isPurchaseAmountEditable: Boolean
        get() = true

    val isUnitEditable: Boolean
        get() = true

    val isCategoryEditable: Boolean
        get() = true

    val isSupplierEditable: Boolean
        get() = true

    val isAddEnabled: Boolean
        get() = price.isNotBlank() &&
            purchaseAmount.isNotBlank() &&
            purchaseAmount.toIntOrNull() != null &&
            amount.isNotBlank() &&
            amount.toIntOrNull() != null

    val confirmButtonText: String
        get() = if (isEditMode) "저장하기" else "재료 추가"
}
