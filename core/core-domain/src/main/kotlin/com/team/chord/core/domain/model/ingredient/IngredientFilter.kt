package com.team.chord.core.domain.model.ingredient

enum class IngredientFilter(val displayName: String, val categoryCode: String) {
    FAVORITE("즐겨찾기", "FAVORITE"),
    FOOD_INGREDIENT("식재료", "INGREDIENTS"),
    OPERATIONAL_SUPPLY("운영 재료", "MATERIALS"),
}
