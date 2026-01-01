package com.team.chord.feature.menu.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.StatusDangerBackground
import com.team.chord.core.ui.theme.StatusSafeBackground
import com.team.chord.core.ui.theme.StatusWarningBackground
import com.team.chord.feature.menu.detail.MenuDetailUi
import com.team.chord.feature.menu.list.MenuStatus
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CostAnalysisCard(
    menuDetail: MenuDetailUi,
    modifier: Modifier = Modifier,
) {
    val backgroundColor =
        when (menuDetail.status) {
            MenuStatus.SAFE -> StatusSafeBackground
            MenuStatus.WARNING -> StatusWarningBackground
            MenuStatus.DANGER -> StatusDangerBackground
        }
    val statusColor = getStatusColor(menuDetail.status)
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(16.dp),
                ).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "마진율:",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Grayscale700,
                )
                Text(
                    text = "${(menuDetail.marginRatio * 100).toInt()}%",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = statusColor,
                )
            }
            MenuStatusBadge(status = menuDetail.status)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "총 원가 (원가율)",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Grayscale500,
                )
                Text(
                    text = "${numberFormat.format(menuDetail.totalCost)}원 (${(menuDetail.costRatio * 100).toInt()}%)",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Grayscale900,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "공헌이익",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Grayscale500,
                    )
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "공헌이익 정보",
                        modifier = Modifier.size(16.dp),
                        tint = Grayscale500,
                    )
                }
                Text(
                    text = "${numberFormat.format(menuDetail.contributionProfit)}원",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Grayscale900,
                )
            }
        }
    }
}
