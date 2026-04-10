package com.team.chord.feature.setup.menuconfirm

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.chord.core.domain.model.Result

@Composable
fun MenuConfirmScreen(
    registeredMenu: RegisteredMenuSummary?,
    onNavigateBack: () -> Unit,
    onComplete: () -> Unit,
    onRegisterMenus: suspend () -> Result<Unit>,
    modifier: Modifier = Modifier,
) {
    com.team.chord.feature.menuadd.shared.confirm.MenuConfirmScreen(
        registeredMenu = registeredMenu,
        onNavigateBack = onNavigateBack,
        onRegisterSuccess = onComplete,
        onRegisterMenus = onRegisterMenus,
        modifier = modifier,
    )
}
