package com.team.chord.feature.menu.list

data class MenuListUiState(
    val isLoading: Boolean = false,
    val selectedCategory: MenuCategory = MenuCategory.ALL,
    val menuItems: List<MenuItemUi> = emptyList(),
    val errorMessage: String? = null,
)

enum class MenuCategory(
    val displayName: String,
) {
    ALL("전체"),
    BEVERAGE("음료"),
    DESSERT("디저트"),
}

data class MenuItemUi(
    val id: Long,
    val name: String,
    val sellingPrice: Int,
    val costRatio: Float,
    val marginRatio: Float,
    val status: MenuStatus,
)

enum class MenuStatus {
    SAFE,
    WARNING,
    DANGER,
}
