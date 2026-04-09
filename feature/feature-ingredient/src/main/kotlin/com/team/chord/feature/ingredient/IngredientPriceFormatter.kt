package com.team.chord.feature.ingredient

import com.team.chord.core.domain.model.menu.IngredientUnit
import java.text.NumberFormat
import java.util.Locale

internal fun formatIngredientUnitLabel(
    unitAmount: Int,
    unit: IngredientUnit,
): String = "${unitAmount}${unit.displayName}당"

internal fun formatIngredientPriceText(
    price: Int,
    unitAmount: Int,
    unit: IngredientUnit,
): String = "${numberFormatter.format(price)}원 / ${formatIngredientUnitLabel(unitAmount, unit)}"

private val numberFormatter: NumberFormat
    get() = NumberFormat.getNumberInstance(Locale.KOREA)
