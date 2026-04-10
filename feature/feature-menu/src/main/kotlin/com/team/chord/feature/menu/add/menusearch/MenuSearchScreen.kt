package com.team.chord.feature.menu.add.menusearch

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.chord.core.domain.model.menu.MenuTemplate

@Composable
fun MenuSearchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetailWithTemplate: (MenuTemplate) -> Unit,
    onNavigateToDetailWithoutTemplate: (menuName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    com.team.chord.feature.menuadd.shared.menusearch.MenuSearchScreen(
        onNavigateBack = onNavigateBack,
        onNavigateToDetailWithTemplate = onNavigateToDetailWithTemplate,
        onNavigateToDetailWithoutTemplate = onNavigateToDetailWithoutTemplate,
        modifier = modifier,
    )
}
