package com.team.chord.feature.menu.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.domain.model.menu.MarginGrade
import com.team.chord.core.ui.component.ChordStatusBadge
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProfitAnalysisCard(
    marginGrade: MarginGrade,
    marginRatio: Float,
    costRatio: Float,
    contributionProfit: Int,
    modifier: Modifier = Modifier,
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Grayscale100,
                shape = RoundedCornerShape(16.dp),
            )
            .border(
                width = 1.dp,
                color = Grayscale300,
                shape = RoundedCornerShape(16.dp),
            )
            .padding(20.dp),
    ) {
        // 상단: 수익등급 라벨 + 뱃지
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "수익등급",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Grayscale900,
            )
            ChordStatusBadge(grade = marginGrade)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 중단: 등급 설명 메시지
        Text(
            text = marginGrade.message,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Grayscale500,
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(color = Grayscale300, thickness = 1.dp)

        Spacer(modifier = Modifier.height(16.dp))

        // 하단: 3열 - 마진율 / 원가율 / 공헌이익
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ProfitMetricColumn(
                label = "마진율",
                value = formatRatio(marginRatio),
            )
            ProfitMetricColumn(
                label = "원가율",
                value = formatRatio(costRatio),
            )
            ProfitMetricColumn(
                label = "공헌이익",
                value = "${numberFormat.format(contributionProfit)}원",
            )
        }
    }
}

@Composable
private fun ProfitMetricColumn(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = label,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = Grayscale500,
        )
        Text(
            text = value,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Grayscale900,
        )
    }
}

private fun formatRatio(ratio: Float): String {
    val intPart = ratio.toInt()
    val decimalPart = ((ratio * 10) % 10).toInt()
    return "$intPart.$decimalPart%"
}
