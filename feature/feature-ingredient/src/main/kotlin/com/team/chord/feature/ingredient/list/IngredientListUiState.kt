package com.team.chord.feature.ingredient.list

import com.team.chord.core.domain.model.ingredient.IngredientFilter

data class IngredientListUiState(
    val isLoading: Boolean = false,
    val ingredients: List<IngredientListItemUi> = emptyList(),
    val activeFilters: Set<IngredientFilter> = emptySet(),
)

data class IngredientListItemUi(
    val id: Long,
    val name: String,
    val price: Int,
    val usage: String,
)
