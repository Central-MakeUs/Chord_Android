package com.team.chord.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PrimaryBlue100
import com.team.chord.core.ui.theme.PrimaryBlue200
import com.team.chord.core.ui.theme.PrimaryBlue700
import com.team.chord.feature.home.HomeStatItem

@Composable
fun HomeGradientStatCard(
    title: String,
    value: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(16.dp)

    Column(
        modifier =
            modifier
                .shadow(
                    elevation = 4.dp,
                    shape = cardShape,
                    ambientColor = Color.Black.copy(alpha = 0.10f),
                    spotColor = Color.Black.copy(alpha = 0.10f),
                )
                .clip(cardShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Grayscale100, PrimaryBlue200),
                    ),
                )
                .padding(16.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = PrimaryBlue700,
            modifier =
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(PrimaryBlue100)
                    .padding(horizontal = 6.dp, vertical = 4.dp),
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineLarge,
            color = Grayscale900,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = Grayscale700,
        )
    }
}

@Composable
fun HomeStatsRow(
    stats: List<HomeStatItem>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        stats.take(2).forEach { item ->
            HomeGradientStatCard(
                title = item.title,
                value = item.value,
                description = item.description,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
