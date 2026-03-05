package com.team.chord.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.feature.home.HomeStrategyItem

@Composable
fun HomeStrategyGuideCard(
    items: List<HomeStrategyItem>,
    modifier: Modifier = Modifier,
    onHeaderClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Grayscale100)
            .padding(16.dp),
    ) {
        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (onHeaderClick != null) {
                        Modifier.clickable { onHeaderClick() }
                    } else {
                        Modifier
                    },
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "전략 가이드",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Grayscale900,
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = com.team.chord.core.ui.R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = Grayscale500,
                modifier = Modifier.size(16.dp),
            )
        }

        if (items.isEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(208.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Grayscale200)
                    .then(
                        if (onHeaderClick != null) {
                            Modifier.clickable { onHeaderClick() }
                        } else {
                            Modifier
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "아직 받아본 전략이 없어요",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Grayscale500,
                )
            }
        } else {
            items.forEachIndexed { index, item ->
                if (index > 0) {
                    HorizontalDivider(color = Grayscale300)
                    Spacer(modifier = Modifier.height(12.dp))
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                HomeStrategyRow(
                    item = item,
                    onClick = onHeaderClick,
                )
                if (index != items.lastIndex) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun HomeStrategyRow(
    item: HomeStrategyItem,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                },
            ),
    ) {
        Text(
            text = item.menuName,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Grayscale900,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.subtitle,
            fontFamily = PretendardFontFamily,
            fontSize = 14.sp,
            color = Grayscale600,
        )
    }
}
