package com.team.chord.feature.menu.list

import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.domain.model.menu.MarginGrade

data class MenuListUiState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val selectedCategoryCode: String? = null,
    val menuItems: List<MenuItemUi> = emptyList(),
    val errorMessage: String? = null,
)

data class MenuItemUi(
    val id: Long,
    val name: String,
    val sellingPrice: Int,
    val costRatio: Float,
    val marginRatio: Float,
    val marginGrade: MarginGrade,
)
