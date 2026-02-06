package com.team.chord.core.domain.model.menu

data class MenuRecipe(
    val recipeId: Long,
    val menuId: Long,
    val ingredientId: Long,
    val ingredientName: String,
    val amount: Int,
    val unitCode: String,
    val price: Int,
)
