package com.team.chord.feature.menu.add.confirm

data class RegisteredMenuSummary(
    val index: Int,
    val name: String,
    val price: Int,
    val ingredients: List<IngredientSummary>,
)

data class IngredientSummary(
    val name: String,
    val amount: String,
    val price: Int,
)
