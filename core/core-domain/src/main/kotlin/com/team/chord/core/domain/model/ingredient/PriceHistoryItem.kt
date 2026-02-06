package com.team.chord.core.domain.model.ingredient

import com.team.chord.core.domain.model.menu.IngredientUnit

data class PriceHistoryItem(
    val id: Long,
    val date: String,
    val price: Int,
    val unitAmount: Int,
    val unit: IngredientUnit,
)
