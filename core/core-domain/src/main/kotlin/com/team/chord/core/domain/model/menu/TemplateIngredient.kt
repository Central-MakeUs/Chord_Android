package com.team.chord.core.domain.model.menu

data class TemplateIngredient(
    val ingredientId: Long? = null,
    val ingredientName: String,
    val usageAmount: Double,
    val defaultCost: Int,
    val unitPrice: Int,
    val baseQuantity: Int,
    val unitCode: String,
    val ingredientCategoryCode: String,
)
