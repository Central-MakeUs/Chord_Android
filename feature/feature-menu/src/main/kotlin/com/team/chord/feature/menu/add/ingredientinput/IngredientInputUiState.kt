package com.team.chord.feature.menu.add.ingredientinput

import com.team.chord.core.domain.model.menu.IngredientUnit
import java.text.NumberFormat
import java.util.Locale

enum class IngredientSourceType {
    NEW,
    TEMPLATE,
    SAVED,
}

data class IngredientInputUiState(
    val searchQuery: String = "",
    val searchResults: List<IngredientSuggestion> = emptyList(),
    val selectedIngredients: List<SelectedIngredient> = emptyList(),
    val isDeleteMode: Boolean = false,
    val selectedIngredientIdsForDeletion: Set<Long> = emptySet(),
    val showDeleteConfirmDialog: Boolean = false,
    val showBottomSheet: Boolean = false,
    val bottomSheetIngredient: IngredientBottomSheetState? = null,
    val isNextEnabled: Boolean = true,
    val isSearching: Boolean = false,
    val isTemplateApplied: Boolean = false,
    val showCompletionToast: Boolean = false,
    val completionToastMessage: String = "",
) {
    val deleteSelectionCount: Int
        get() = selectedIngredientIdsForDeletion.size
}

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
    val unitPrice: Int = 0,
    val suggestedPrice: Int? = null,
    val suggestedPurchaseAmount: Int? = null,
    val suggestedAmount: Int? = null,
    val suggestedUnit: IngredientUnit? = null,
    val isEditMode: Boolean = false,
    val editingIngredientId: Long? = null,
) {
    val isExistingIngredientLayout: Boolean
        get() = sourceType != IngredientSourceType.NEW

    val isPriceEditable: Boolean
        get() = !isExistingIngredientLayout

    val isPurchaseAmountEditable: Boolean
        get() = !isExistingIngredientLayout

    val isUnitEditable: Boolean
        get() = !isExistingIngredientLayout

    val isCategoryEditable: Boolean
        get() = !isExistingIngredientLayout

    val isSupplierEditable: Boolean
        get() = !isExistingIngredientLayout

    val usageLabel: String
        get() = if (isExistingIngredientLayout) "사용량" else "재료 사용량"

    val unitPriceText: String
        get() = purchaseAmount.toIntOrNull()
            ?.let { "${purchaseAmount}${unit.displayName}당 ${formatPrice(unitPrice)}원" }
            ?: "-"

    val supplierText: String
        get() = supplier.ifEmpty { "-" }

    val isAddEnabled: Boolean
        get() = if (isExistingIngredientLayout) {
            amount.isNotBlank() && amount.toIntOrNull() != null
        } else {
            price.isNotBlank() &&
                purchaseAmount.isNotBlank() &&
                purchaseAmount.toIntOrNull() != null &&
                amount.isNotBlank() &&
                amount.toIntOrNull() != null
        }

    val confirmButtonText: String
        get() = if (isEditMode) "저장하기" else "재료 추가"
}

private fun formatPrice(value: Int): String =
    NumberFormat.getNumberInstance(Locale.KOREA).format(value)
