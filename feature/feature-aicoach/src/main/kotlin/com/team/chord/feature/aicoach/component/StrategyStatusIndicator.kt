package com.team.chord.feature.aicoach.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.core.ui.theme.StatusSafe
import com.team.chord.feature.aicoach.strategy.StrategyState

@Composable
fun StrategyStatusIndicator(
    state: StrategyState,
    modifier: Modifier = Modifier,
) {
    val (dotColor, labelText) = when (state) {
        StrategyState.IN_PROGRESS -> PrimaryBlue500 to "진행중"
        StrategyState.NOT_STARTED -> Grayscale500 to "진행전"
        StrategyState.COMPLETED -> StatusSafe to "완료"
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color = dotColor, shape = CircleShape),
        )
        Text(
            text = labelText,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = dotColor,
        )
    }
}
