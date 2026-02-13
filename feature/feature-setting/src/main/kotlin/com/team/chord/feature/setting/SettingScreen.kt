package com.team.chord.feature.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.component.ChordTopAppBar
import com.team.chord.core.ui.component.ChordTwoButtonDialog
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.feature.setting.component.SettingMenuRow
import com.team.chord.feature.setting.component.StoreInfoCard

@Composable
fun SettingScreen(
    onNavigateBack: () -> Unit,
    onNavigateToStoreEdit: () -> Unit,
    onNavigateToFaq: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.logoutSuccess) {
        if (uiState.logoutSuccess) {
            onLogout()
        }
    }

    SettingScreenContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onNavigateToStoreEdit = onNavigateToStoreEdit,
        onNavigateToFaq = onNavigateToFaq,
        onNavigateToTerms = onNavigateToTerms,
        onNavigateToWithdraw = onNavigateToWithdraw,
        onShowLogoutDialog = viewModel::onShowLogoutDialog,
        onDismissLogoutDialog = viewModel::onDismissLogoutDialog,
        onConfirmLogout = viewModel::onLogout,
        modifier = modifier,
    )
}

@Composable
internal fun SettingScreenContent(
    uiState: SettingUiState,
    onNavigateBack: () -> Unit,
    onNavigateToStoreEdit: () -> Unit,
    onNavigateToFaq: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onShowLogoutDialog: () -> Unit,
    onDismissLogoutDialog: () -> Unit,
    onConfirmLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale200),
    ) {
        ChordTopAppBar(
            title = "설정",
            onBackClick = onNavigateBack,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            StoreInfoCard(
                storeName = uiState.storeName,
                employeeCount = uiState.employeeCount,
                laborCost = uiState.laborCost,
                onEditClick = onNavigateToStoreEdit,
            )

            SettingMenuRow(
                title = "FAQ",
                onClick = onNavigateToFaq,
            )

            SettingMenuRow(
                title = "이용약관",
                onClick = onNavigateToTerms,
            )

            SettingMenuRow(
                title = "회원탈퇴",
                onClick = onNavigateToWithdraw,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "로그아웃",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Grayscale500,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onShowLogoutDialog() }
                    .padding(vertical = 16.dp),
            )
        }
    }

    if (uiState.showLogoutDialog) {
        ChordTwoButtonDialog(
            title = "로그아웃 하시겠어요?",
            onDismiss = onDismissLogoutDialog,
            onConfirm = onConfirmLogout,
            dismissText = "취소하기",
            confirmText = "로그아웃",
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SettingScreenContentPreview() {
    SettingScreenContent(
        uiState = SettingUiState(),
        onNavigateBack = {},
        onNavigateToStoreEdit = {},
        onNavigateToFaq = {},
        onNavigateToTerms = {},
        onNavigateToWithdraw = {},
        onShowLogoutDialog = {},
        onDismissLogoutDialog = {},
        onConfirmLogout = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingScreenContentWithDialogPreview() {
    SettingScreenContent(
        uiState = SettingUiState(showLogoutDialog = true),
        onNavigateBack = {},
        onNavigateToStoreEdit = {},
        onNavigateToFaq = {},
        onNavigateToTerms = {},
        onNavigateToWithdraw = {},
        onShowLogoutDialog = {},
        onDismissLogoutDialog = {},
        onConfirmLogout = {},
    )
}
