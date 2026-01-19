package com.team.chord.feature.setup.menudetail

data class MenuDetailUiState(
    val menuName: String = "",
    val price: String = "",
    val selectedCategory: MenuCategory = MenuCategory.BEVERAGE,
    val preparationMinutes: Int = 1,
    val preparationSeconds: Int = 30,
    val isTemplateApplied: Boolean = false,
    val isNextEnabled: Boolean = false,
)

enum class MenuCategory(val displayName: String) {
    BEVERAGE("음료"),
    DESSERT("디저트"),
    FOOD("푸드"),
}

/**
 * Data class to pass menu detail information to the next screen.
 */
data class MenuDetailData(
    val menuName: String,
    val price: Int,
    val category: MenuCategory,
    val preparationMinutes: Int,
    val preparationSeconds: Int,
    val isTemplateApplied: Boolean,
)
