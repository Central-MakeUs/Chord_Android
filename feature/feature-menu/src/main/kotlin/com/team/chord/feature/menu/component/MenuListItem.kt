package com.team.chord.feature.menu.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.domain.model.menu.MarginGrade
import com.team.chord.core.ui.component.ChordStatusBadge
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.StatusDanger
import com.team.chord.core.ui.theme.StatusMid
import com.team.chord.core.ui.theme.StatusSafe
import com.team.chord.core.ui.theme.StatusWarning
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MenuListItem(
    name: String,
    price: Int,
    costRatio: Float,
    marginRatio: Float,
    marginGrade: MarginGrade,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    val marginColor = when (marginGrade) {
        MarginGrade.SAFE -> StatusSafe
        MarginGrade.MID -> StatusMid
        MarginGrade.WARNING -> StatusWarning
        MarginGrade.DANGER -> StatusDanger
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Grayscale100)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = name,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Grayscale900,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${numberFormat.format(price)}원",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Grayscale900,
                )
                Text(
                    text = " 원가율 ${costRatio.toInt()}%",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Grayscale500,
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            ChordStatusBadge(grade = marginGrade)
            Text(
                text = "${marginRatio.toInt()}.${((marginRatio * 10) % 10).toInt()}%",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = marginColor,
            )
        }
    }
}
