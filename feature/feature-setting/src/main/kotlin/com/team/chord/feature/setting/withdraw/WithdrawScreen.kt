package com.team.chord.feature.setting.withdraw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordOneButtonDialog
import com.team.chord.core.ui.component.ChordTopAppBar
import com.team.chord.core.ui.component.ChordTwoButtonDialog
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily

@Composable
fun WithdrawScreen(
    onNavigateBack: () -> Unit,
    onWithdrawSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WithdrawViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.deleteSuccess) {
        if (uiState.deleteSuccess) {
            onWithdrawSuccess()
            viewModel.onDeleteSuccessConsumed()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        ChordTopAppBar(
            title = "회원탈퇴",
            onBackClick = onNavigateBack,
        )

        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
        ) {
            Text(
                text = "정말 탈퇴하시겠어요?",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Grayscale900,
            )

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = "회원 탈퇴를 진행하면 즉시 계정이 삭제되며 서비스 이용이 불가합니다.",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Grayscale600,
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Text(
                text = "- 탈퇴 후에는 기존 데이터가 복구되지 않습니다.",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Grayscale600,
            )

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = "- 탈퇴 즉시 계정 정보(아이디, 비밀번호 등)는 지체 없이 삭제됩니다.",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Grayscale600,
            )

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = "- 단, 관련 법령에 따라 결제·거래 기록 등 일부 정보는 일정 기간 보관될 수 있습니다.",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Grayscale600,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ChordLargeButton(
            text = "탈퇴하기",
            onClick = viewModel::onWithdrawClicked,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            enabled = !uiState.isSubmitting,
            backgroundColor = Grayscale400,
            textColor = Grayscale600,
        )
    }

    if (uiState.showConfirmDialog) {
        ChordTwoButtonDialog(
            title = "정말 탈퇴하시겠어요?",
            onDismiss = viewModel::onDismissConfirmDialog,
            onConfirm = viewModel::onConfirmWithdraw,
            dismissText = "취소하기",
            confirmText = "탈퇴하기",
        )
    }

    if (uiState.errorMessage != null) {
        ChordOneButtonDialog(
            title = uiState.errorMessage ?: "회원탈퇴에 실패했어요",
            onConfirm = viewModel::onErrorMessageConsumed,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WithdrawScreenPreview() {
    WithdrawScreen(
        onNavigateBack = {},
        onWithdrawSuccess = {},
    )
}
