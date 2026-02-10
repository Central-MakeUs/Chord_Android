package com.team.chord.core.domain.model.ingredient

data class IngredientSearchResult(
    val isTemplate: Boolean,
    val templateId: Long? = null,
    val ingredientId: Long? = null,
    val ingredientName: String,
    val categoryCode: String? = null,
    val unitPrice: Int? = null,
    val unitCode: String? = null,
    val baseQuantity: Int? = null,
    val supplier: String? = null,
)
