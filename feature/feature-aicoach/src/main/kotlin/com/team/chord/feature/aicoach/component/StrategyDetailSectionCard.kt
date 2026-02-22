package com.team.chord.feature.aicoach.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue100
import com.team.chord.core.ui.theme.PrimaryBlue500

@Composable
fun StrategyDetailSectionCard(
    title: String,
    headline: String? = null,
    body: String,
    menuNames: List<String> = emptyList(),
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Grayscale100,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = title,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Grayscale900,
            )

            if (!headline.isNullOrBlank()) {
                Text(
                    text = headline,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Grayscale900,
                )
            }

            if (menuNames.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    menuNames.forEach { menuName ->
                        StrategyMenuChip(name = menuName)
                    }
                }
            }

            Text(
                text = body,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Grayscale700,
            )
        }
    }
}

@Composable
private fun StrategyMenuChip(
    name: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(
                color = PrimaryBlue100,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = PrimaryBlue500,
        )
    }
}
