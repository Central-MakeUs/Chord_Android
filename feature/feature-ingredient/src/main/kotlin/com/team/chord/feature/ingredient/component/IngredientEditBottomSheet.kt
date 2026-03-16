package com.team.chord.feature.ingredient.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.team.chord.core.domain.model.ingredient.IngredientFilter
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.ui.component.IngredientEditorBottomSheet
import com.team.chord.core.ui.component.IngredientEditorCategoryOption

@Composable
fun IngredientEditBottomSheet(
    ingredientName: String,
    selectedFilter: IngredientFilter,
    price: String,
    amount: String,
    selectedUnit: IngredientUnit,
    supplier: String,
    onFilterSelect: (IngredientFilter) -> Unit,
    onPriceChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onUnitSelect: (IngredientUnit) -> Unit,
    onSupplierChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IngredientEditorBottomSheet(
        title = ingredientName,
        categoryCode = selectedFilter.categoryCode,
        categoryOptions = ingredientCategoryOptions,
        onCategoryChanged = { categoryCode ->
            onFilterSelect(categoryCode.toIngredientFilter())
        },
        purchaseAmountLabel = "용량",
        price = price,
        onPriceChanged = { onPriceChange(it.filter(Char::isDigit)) },
        pricePlaceholder = "구매한 가격 입력",
        purchaseAmount = amount,
        onPurchaseAmountChanged = { onAmountChange(it.filter(Char::isDigit)) },
        purchaseAmountPlaceholder = "구매량 용량 입력",
        showUsageField = false,
        amount = "",
        onAmountChanged = {},
        amountPlaceholder = "",
        unit = selectedUnit,
        onUnitChanged = onUnitSelect,
        supplierLabel = "공급업체 (선택)",
        supplier = supplier,
        onSupplierChanged = onSupplierChange,
        confirmText = "저장하기",
        confirmEnabled = price.isNotBlank() && amount.isNotBlank(),
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        modifier = modifier,
    )
}

private val ingredientCategoryOptions = listOf(
    IngredientEditorCategoryOption(code = IngredientFilter.FOOD_INGREDIENT.categoryCode, label = IngredientFilter.FOOD_INGREDIENT.displayName),
    IngredientEditorCategoryOption(code = IngredientFilter.OPERATIONAL_SUPPLY.categoryCode, label = IngredientFilter.OPERATIONAL_SUPPLY.displayName),
)

private fun String.toIngredientFilter(): IngredientFilter =
    IngredientFilter.entries.find { it.categoryCode == this } ?: IngredientFilter.FOOD_INGREDIENT

@Preview(showBackground = true)
@Composable
private fun IngredientEditBottomSheetPreview() {
    IngredientEditBottomSheet(
        ingredientName = "흑임자 토핑",
        selectedFilter = IngredientFilter.FOOD_INGREDIENT,
        price = "5000",
        amount = "100",
        selectedUnit = IngredientUnit.G,
        supplier = "쿠팡",
        onFilterSelect = {},
        onPriceChange = {},
        onAmountChange = {},
        onUnitSelect = {},
        onSupplierChange = {},
        onConfirm = {},
        onDismiss = {},
    )
}
