package com.team.chord.core.domain.model.ingredient

import com.team.chord.core.domain.model.menu.IngredientUnit

data class Ingredient(
    val id: Long,
    val name: String,
    val categoryCode: String,
    val unit: IngredientUnit,
    val baseQuantity: Int,
    val currentUnitPrice: Int,
    val supplier: String? = null,
    val isFavorite: Boolean = false,
    val originalAmount: Int? = null,
    val originalPrice: Int? = null,
    val usedMenus: List<UsedMenu> = emptyList(),
)
