package com.team.chord.core.domain.model.menu

data class NewRecipeInfo(
    val amount: Int,
    val price: Int,
    val unitCode: String,
    val ingredientCategoryCode: String,
    val ingredientName: String,
    val supplier: String? = null,
)
