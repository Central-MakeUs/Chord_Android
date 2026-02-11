package com.team.chord.feature.aicoach.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.aicoach.strategy.StrategyFilter

@Composable
fun StrategyFilterTabs(
    selectedFilter: StrategyFilter,
    onFilterSelect: (StrategyFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        StrategyFilter.entries.forEach { filter ->
            val isSelected = filter == selectedFilter
            Text(
                text = filter.displayName,
                fontFamily = PretendardFontFamily,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 14.sp,
                color = if (isSelected) Grayscale900 else Grayscale500,
                modifier = Modifier
                    .clickable { onFilterSelect(filter) }
                    .drawBehind {
                        if (isSelected) {
                            val strokeWidth = 2.dp.toPx()
                            val y = size.height - strokeWidth / 2
                            drawLine(
                                color = PrimaryBlue500,
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = strokeWidth,
                            )
                        }
                    }
                    .padding(top = 4.dp, bottom = 6.dp),
            )
        }
    }
}
