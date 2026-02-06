package com.team.chord.core.domain.model.menu

data class CheckDupResult(
    val menuNameDuplicate: Boolean,
    val dupIngredientNames: List<String>,
)
