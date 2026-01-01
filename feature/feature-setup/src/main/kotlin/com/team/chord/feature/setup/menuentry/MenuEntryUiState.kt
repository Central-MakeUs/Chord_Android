package com.team.chord.feature.setup.menuentry

data class MenuEntryUiState(
    val selectedCategory: MenuCategory = MenuCategory.BEVERAGE,
    val menuName: String = "",
    val menuPrice: String = "",
    val ingredients: List<Ingredient> = emptyList(),
    val newIngredientName: String = "",
    val ingredientPrice: String = "",
    val weight: String = "",
    val weightUnit: WeightUnit = WeightUnit.GRAM,
    val isWeightUnitDropdownExpanded: Boolean = false,
    val preparationMinutes: String = "",
    val preparationSeconds: String = "",
    val isRegisterEnabled: Boolean = false,
)

enum class MenuCategory(
    val displayName: String,
) {
    BEVERAGE("음료"),
    FOOD("푸드"),
}

enum class WeightUnit(
    val displayName: String,
) {
    GRAM("g"),
    MILLILITER("ml"),
    PIECE("개"),
}

data class Ingredient(
    val id: String,
    val name: String,
    val isSelected: Boolean = true,
)
