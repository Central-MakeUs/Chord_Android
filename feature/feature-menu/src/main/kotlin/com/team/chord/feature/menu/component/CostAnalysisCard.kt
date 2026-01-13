package com.team.chord.feature.menu.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import com.team.chord.core.ui.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.feature.menu.detail.MenuDetailUi
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CostAnalysisCard(
    menuDetail: MenuDetailUi,
    modifier: Modifier = Modifier,
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
            )
            .background(
                color = Grayscale200,
                shape = RoundedCornerShape(16.dp),
            )
            .padding(20.dp),
    ) {
        // 마진율 row
        CostAnalysisRow(
            label = "마진율",
            value = formatRatio(menuDetail.marginRatio),
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 총 원가(원가율) row with info icon
        CostAnalysisRow(
            label = "총 원가(원가율)",
            value = "${numberFormat.format(menuDetail.totalCost)}원 (${formatRatio(menuDetail.costRatio)})",
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 공헌이익 row
        CostAnalysisRowWithIcon(
            label = "공헌이익",
            value = "${numberFormat.format(menuDetail.contributionProfit)}원",
        )
    }
}

@Composable
private fun CostAnalysisRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = Modifier.widthIn(min = 100.dp),
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
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

@Composable
private fun CostAnalysisRowWithIcon(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.widthIn(min = 100.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = label,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Grayscale500,
            )
            Icon(
                painter = painterResource(R.drawable.ic_tooltip),
                contentDescription = "정보",
                modifier = Modifier.size(10.dp),
                tint = Grayscale500,
            )
        }
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
