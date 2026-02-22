package com.team.chord.feature.aicoach.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily

@Composable
fun StrategyHistoryItem(
    weekLabel: String,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = weekLabel,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = Grayscale600,
        )
        Text(
            text = title,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Grayscale900,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = description,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Grayscale600,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
