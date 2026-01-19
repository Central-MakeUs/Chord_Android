package com.team.chord.core.domain.model.menu

/**
 * Menu template for creating new menus with pre-filled data.
 * Templates provide suggested values for menu configuration.
 */
data class MenuTemplate(
    val id: Long,
    val name: String,
    val suggestedPrice: Int,
    val category: Category,
    val preparationTimeSeconds: Int,
    val ingredients: List<MenuIngredient>,
)
