package com.team.chord.feature.aicoach.strategy.complete

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.R as CoreUiR
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500

@Composable
fun StrategyCompleteScreen(
    completionPhrase: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bodyText = completionPhrase.ifBlank {
        "고마진 메뉴의 판매 비중이 높아지면\n더 빠르게 수익이 쌓이고,\n카페 전체 수익구조가 좋아져요"
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale200),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(120.dp))
            Text(
                text = "전략 실행이 완료됐어요",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = PrimaryBlue500,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = bodyText,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = Grayscale700,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = CoreUiR.drawable.ic_blue_linear_check),
                contentDescription = null,
                tint = PrimaryBlue500,
                modifier = Modifier.size(72.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
        ) {
            ChordLargeButton(
                text = "확인",
                onClick = onConfirm,
            )
        }
    }
}
