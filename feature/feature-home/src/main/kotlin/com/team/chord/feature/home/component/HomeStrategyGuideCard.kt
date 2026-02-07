package com.team.chord.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.home.HomeStrategyItem

@Composable
fun HomeStrategyGuideCard(
    items: List<HomeStrategyItem>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Grayscale100)
                .padding(16.dp),
    ) {
        items.forEachIndexed { index, item ->
            HomeStrategyRow(item = item)
            if (index != items.lastIndex) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Grayscale300)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun HomeStrategyRow(
    item: HomeStrategyItem,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = item.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Grayscale600,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = item.menuName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Grayscale900,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = item.actionLabel,
                style = MaterialTheme.typography.bodyLarge,
                color = PrimaryBlue500,
            )
        }
    }
}
