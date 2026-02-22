package com.team.chord.feature.aicoach.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500

@Composable
fun StrategyDetailBadge(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = PrimaryBlue500,
                shape = RoundedCornerShape(999.dp),
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = text,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = Grayscale100,
        )
    }
}
