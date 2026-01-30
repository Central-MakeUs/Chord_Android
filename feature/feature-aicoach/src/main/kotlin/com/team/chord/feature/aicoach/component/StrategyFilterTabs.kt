package com.team.chord.feature.aicoach.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue200
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
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        StrategyFilter.entries.forEach { filter ->
            val isSelected = filter == selectedFilter
            Text(
                text = filter.displayName,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = if (isSelected) PrimaryBlue500 else Grayscale600,
                modifier = Modifier
                    .background(
                        color = if (isSelected) PrimaryBlue200 else Grayscale300,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .clickable { onFilterSelect(filter) }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
            )
        }
    }
}
