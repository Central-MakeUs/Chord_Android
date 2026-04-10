package com.team.chord.feature.menu.add.confirm

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.chord.core.domain.model.Result

@Composable
fun MenuAddConfirmScreen(
    registeredMenu: RegisteredMenuSummary?,
    onNavigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onRegisterMenus: suspend () -> Result<Unit>,
    modifier: Modifier = Modifier,
) {
    com.team.chord.feature.menuadd.shared.confirm.MenuConfirmScreen(
        registeredMenu = registeredMenu,
        onNavigateBack = onNavigateBack,
        onRegisterSuccess = onRegisterSuccess,
        onRegisterMenus = onRegisterMenus,
        modifier = modifier,
    )
}
