package com.team.chord.feature.menu.add.menudetail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MenuDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToIngredientInput: (MenuDetailData) -> Unit,
    modifier: Modifier = Modifier,
) {
    com.team.chord.feature.menuadd.shared.menudetail.MenuDetailScreen(
        onNavigateBack = onNavigateBack,
        onNavigateToIngredientInput = onNavigateToIngredientInput,
        modifier = modifier,
    )
}
