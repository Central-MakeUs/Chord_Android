package com.team.chord.feature.menu.add.ingredientinput

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun IngredientInputScreen(
    onNavigateBack: () -> Unit,
    onNavigateToConfirm: (List<SelectedIngredient>) -> Unit,
    modifier: Modifier = Modifier,
) {
    com.team.chord.feature.menuadd.shared.ingredientinput.IngredientInputScreen(
        onNavigateBack = onNavigateBack,
        onNavigateToConfirm = onNavigateToConfirm,
        modifier = modifier,
    )
}
