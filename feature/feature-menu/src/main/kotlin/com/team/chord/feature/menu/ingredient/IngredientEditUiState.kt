package com.team.chord.feature.menu.ingredient

import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.domain.model.menu.MenuIngredient

data class IngredientEditUiState(
    val isLoading: Boolean = false,
    val menuId: Long = 0L,
    val menuName: String = "",
    val ingredients: List<MenuIngredient> = emptyList(),
    val selectedIngredientIds: Set<Long> = emptySet(),
    val isDeleteMode: Boolean = false,
    val showEditBottomSheet: Boolean = false,
    val editingIngredient: MenuIngredient? = null,
    val showAddBottomSheet: Boolean = false,
    val errorMessage: String? = null,
)

data class IngredientFormState(
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val unit: IngredientUnit = IngredientUnit.G,
    val nameError: String? = null,
    val priceError: String? = null,
    val quantityError: String? = null,
) {
    val isValid: Boolean
        get() = name.isNotBlank() &&
            price.isNotBlank() &&
            price.all { it.isDigit() } &&
            quantity.isNotBlank() &&
            quantity.toDoubleOrNull() != null
}
