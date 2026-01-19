package com.team.chord.feature.setup.menusuggestion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.R
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily

@Composable
fun MenuSuggestionScreen(
    onStartMenuRegistration: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MenuSuggestionScreenContent(
        onStartMenuRegistration = onStartMenuRegistration,
        modifier = modifier,
    )
}

@Composable
internal fun MenuSuggestionScreenContent(
    onStartMenuRegistration: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Calculator icon
            Image(
                painter = painterResource(id = R.drawable.ic_3d_calculator),
                contentDescription = "계산기 아이콘",
                modifier = Modifier.size(144.dp),
            )

            Spacer(modifier = Modifier.height(17.dp))

            // Main text
            Text(
                text = "메뉴를 등록해주시면\n원가와 마진을 계산해드릴게요",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Grayscale900,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp,
            )

            Spacer(modifier = Modifier.weight(1f))

            // Start menu registration button
            ChordLargeButton(
                text = "메뉴 등록 시작하기",
                onClick = onStartMenuRegistration,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MenuSuggestionScreenPreview() {
    MenuSuggestionScreen(
        onStartMenuRegistration = {},
    )
}
