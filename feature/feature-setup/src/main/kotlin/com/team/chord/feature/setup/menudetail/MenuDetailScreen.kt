package com.team.chord.feature.setup.menudetail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MenuDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToIngredientInput: (MenuDetailData) -> Unit,
    modifier: Modifier = Modifier,
    defaultCategory: MenuCategory = MenuCategory.BEVERAGE,
) {
    com.team.chord.feature.menuadd.shared.menudetail.MenuDetailScreen(
        onNavigateBack = onNavigateBack,
        onNavigateToIngredientInput = onNavigateToIngredientInput,
        modifier = modifier,
        defaultCategory = defaultCategory,
    )
}
