package com.team.chord.feature.setup.menuconfirm

typealias RegisteredMenuSummary = com.team.chord.feature.menuadd.shared.confirm.RegisteredMenuSummary
typealias IngredientSummary = com.team.chord.feature.menuadd.shared.confirm.IngredientSummary

data class MenuConfirmUiState(
    val registeredMenu: RegisteredMenuSummary? = null,
    val isRegistering: Boolean = false,
)
