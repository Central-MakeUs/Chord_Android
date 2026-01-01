package com.team.chord.feature.menu.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.StatusDanger
import com.team.chord.core.ui.theme.StatusSafe
import com.team.chord.core.ui.theme.StatusWarning
import com.team.chord.feature.menu.list.MenuStatus

@Composable
fun MenuStatusBadge(
    status: MenuStatus,
    modifier: Modifier = Modifier,
) {
    val (text, color) =
        when (status) {
            MenuStatus.SAFE -> "안전" to StatusSafe
            MenuStatus.WARNING -> "주의" to StatusWarning
            MenuStatus.DANGER -> "위험" to StatusDanger
        }

    Text(
        text = text,
        modifier =
            modifier
                .border(
                    width = 1.dp,
                    color = color,
                    shape = RoundedCornerShape(4.dp),
                ).padding(horizontal = 8.dp, vertical = 4.dp),
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        color = color,
    )
}

@Composable
fun getStatusColor(status: MenuStatus): Color =
    when (status) {
        MenuStatus.SAFE -> StatusSafe
        MenuStatus.WARNING -> StatusWarning
        MenuStatus.DANGER -> StatusDanger
    }
