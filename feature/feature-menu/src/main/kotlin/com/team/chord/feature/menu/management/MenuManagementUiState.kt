package com.team.chord.feature.menu.management

import com.team.chord.core.domain.model.menu.Category

data class MenuManagementUiState(
    val isLoading: Boolean = false,
    val menuId: Long = 0L,
    val menuName: String = "",
    val price: Int = 0,
    val preparationTimeSeconds: Int = 0,
    val categories: List<Category> = emptyList(),
    val selectedCategoryCode: String = "",
    val showNameBottomSheet: Boolean = false,
    val showPriceBottomSheet: Boolean = false,
    val showTimeBottomSheet: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showDeleteSuccessDialog: Boolean = false,
    val errorMessage: String? = null,
)
