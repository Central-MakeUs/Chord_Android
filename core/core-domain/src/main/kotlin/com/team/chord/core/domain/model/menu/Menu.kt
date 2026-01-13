package com.team.chord.core.domain.model.menu

data class Menu(
    val id: Long,
    val name: String,
    val price: Int,
    val category: Category,
    val preparationTimeSeconds: Int,
    val ingredients: List<MenuIngredient>,
    val totalCost: Int,
    val costRatio: Float,
    val marginRatio: Float,
    val contributionProfit: Int,
    val marginGrade: MarginGrade,
    val recommendedPrice: Int?,
    val recommendedPriceMessage: String?,
)
