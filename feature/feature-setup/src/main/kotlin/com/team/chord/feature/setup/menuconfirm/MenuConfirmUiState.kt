package com.team.chord.feature.setup.menuconfirm

/**
 * UI State for MenuConfirmScreen
 */
data class MenuConfirmUiState(
    val registeredMenus: List<RegisteredMenuSummary> = emptyList(),
)

/**
 * Summary of a registered menu for display
 */
data class RegisteredMenuSummary(
    val index: Int,
    val name: String,
    val price: Int,
    val ingredients: List<IngredientSummary>,
)

/**
 * Summary of an ingredient within a menu
 */
data class IngredientSummary(
    val name: String,
    val amount: String,
    val price: Int,
)
