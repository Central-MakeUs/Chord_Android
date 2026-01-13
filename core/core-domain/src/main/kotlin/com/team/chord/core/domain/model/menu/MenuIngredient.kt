package com.team.chord.core.domain.model.menu

data class MenuIngredient(
    val id: Long,
    val name: String,
    val quantity: Double,
    val unit: IngredientUnit,
    val unitPrice: Int,
    val totalPrice: Int,
)
