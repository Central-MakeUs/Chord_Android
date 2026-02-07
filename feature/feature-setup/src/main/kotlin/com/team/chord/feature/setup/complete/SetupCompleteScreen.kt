package com.team.chord.feature.setup.complete

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.R
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily

@Composable
fun SetupCompleteScreen(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler(enabled = true) {
        // 뒤로가기 비활성화 - 아무 동작 없음
    }

    SetupCompleteScreenContent(
        onStartAnalysisClicked = onNavigateToHome,
        modifier = modifier,
    )
}

@Composable
internal fun SetupCompleteScreenContent(
    onStartAnalysisClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(R.drawable.ic_blue_linear_check),
            contentDescription = "완료",
            modifier = Modifier.size(80.dp),
            tint = Color.Unspecified,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "원가 분석을 위한 준비가\n완료됐어요",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Grayscale900,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp,
        )

        Spacer(modifier = Modifier.weight(1f))

        ChordLargeButton(
            text = "수익 분석 시작하기",
            onClick = onStartAnalysisClicked,
            modifier = Modifier.padding(bottom = 32.dp),
        )
    }
}
