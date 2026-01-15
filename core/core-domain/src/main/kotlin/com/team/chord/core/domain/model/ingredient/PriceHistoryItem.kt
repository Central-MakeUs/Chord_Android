package com.team.chord.core.domain.model.ingredient

data class PriceHistoryItem(
    val id: Long,
    val date: String,
    val price: Int,
    val unitAmount: Int,
    val unitType: IngredientUnitType,
)
