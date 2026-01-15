package com.team.chord.core.domain.model.ingredient

import com.team.chord.core.domain.model.menu.IngredientUnit

data class Ingredient(
    val id: Long,
    val name: String,
    val price: Int,
    val unitAmount: Int,
    val unit: IngredientUnit,
    val supplier: String,
    val isFavorite: Boolean,
    val category: IngredientCategory = IngredientCategory.FOOD_MATERIAL,
)
