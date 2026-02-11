package com.team.chord.feature.ingredient.list

import com.team.chord.core.domain.model.ingredient.IngredientFilter

data class IngredientListUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val ingredients: List<IngredientListItemUi> = emptyList(),
    val activeFilters: Set<IngredientFilter> = emptySet(),
    val errorMessage: String? = null,
    // 삭제 모드
    val isDeleteMode: Boolean = false,
    val selectedIds: Set<Long> = emptySet(),
    // 추가 플로우
    val showMoreMenu: Boolean = false,
    val showAddNameSheet: Boolean = false,
    val showAddDetailSheet: Boolean = false,
    val addIngredientName: String = "",
    // Toast
    val showToast: Boolean = false,
    val toastMessage: String = "",
)

data class IngredientListItemUi(
    val id: Long,
    val name: String,
    val price: Int,
    val usage: String,
)
