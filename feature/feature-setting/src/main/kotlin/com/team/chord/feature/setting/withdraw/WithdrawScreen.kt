package com.team.chord.feature.setting.withdraw

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordOneButtonDialog
import com.team.chord.core.ui.component.ChordTopAppBarWithIconAction
import com.team.chord.core.ui.component.ChordTwoButtonDialog
import com.team.chord.core.ui.component.TopAppBarActionIcon
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily

@Composable
fun WithdrawScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showNotReadyDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        ChordTopAppBarWithIconAction(
            title = "회원탈퇴",
            onBackClick = onNavigateBack,
            actionIcon = TopAppBarActionIcon.Menu,
            onActionClick = { },
        )

        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
        ) {
            Text(
                text = "탈퇴안내 내용",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Grayscale900,
            )

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = "정말 탈퇴하시겠어요?",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Grayscale600,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ChordLargeButton(
            text = "탈퇴하기",
            onClick = { showConfirmDialog = true },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            enabled = true,
            backgroundColor = Grayscale400,
            textColor = Grayscale600,
        )
    }

    if (showConfirmDialog) {
        ChordTwoButtonDialog(
            title = "정말 탈퇴하시겠어요?",
            onDismiss = { showConfirmDialog = false },
            onConfirm = {
                showConfirmDialog = false
                showNotReadyDialog = true
            },
            dismissText = "취소하기",
            confirmText = "탈퇴하기",
        )
    }

    if (showNotReadyDialog) {
        ChordOneButtonDialog(
            title = "현재 준비 중인 기능이에요",
            onConfirm = { showNotReadyDialog = false },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WithdrawScreenPreview() {
    WithdrawScreen(
        onNavigateBack = {},
    )
}
