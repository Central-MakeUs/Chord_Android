package com.team.chord.feature.menuadd.shared.confirm

data class RegisteredMenuSummary(
    val name: String,
    val price: Int,
    val ingredients: List<IngredientSummary>,
)

data class IngredientSummary(
    val name: String,
    val amount: String,
    val price: Int,
)
