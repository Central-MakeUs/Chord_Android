package com.team.chord.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.domain.model.menu.MarginGrade
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.StatusDanger
import com.team.chord.core.ui.theme.StatusDangerBackground
import com.team.chord.core.ui.theme.StatusMid
import com.team.chord.core.ui.theme.StatusMidBackground
import com.team.chord.core.ui.theme.StatusSafe
import com.team.chord.core.ui.theme.StatusSafeBackground
import com.team.chord.core.ui.theme.StatusWarning
import com.team.chord.core.ui.theme.StatusWarningBackground

@Composable
fun ChordStatusBadge(
    grade: MarginGrade,
    modifier: Modifier = Modifier,
) {
    val (text, textColor, backgroundColor) = when (grade) {
        MarginGrade.SAFE -> Triple("안정", StatusSafe, StatusSafeBackground)
        MarginGrade.MID -> Triple("보통", StatusMid, StatusMidBackground)
        MarginGrade.WARNING -> Triple("주의", StatusWarning, StatusWarningBackground)
        MarginGrade.DANGER -> Triple("위험", StatusDanger, StatusDangerBackground)
    }

    Text(
        text = text,
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        style = TextStyle(
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = textColor,
        ),
    )
}

fun MarginGrade.toStatusColor(): Color = when (this) {
    MarginGrade.SAFE -> StatusSafe
    MarginGrade.MID -> StatusMid
    MarginGrade.WARNING -> StatusWarning
    MarginGrade.DANGER -> StatusDanger
}

fun MarginGrade.toStatusBackgroundColor(): Color = when (this) {
    MarginGrade.SAFE -> StatusSafeBackground
    MarginGrade.MID -> StatusMidBackground
    MarginGrade.WARNING -> StatusWarningBackground
    MarginGrade.DANGER -> StatusDangerBackground
}

@Preview(showBackground = true)
@Composable
private fun ChordStatusBadgeSafePreview() {
    ChordStatusBadge(grade = MarginGrade.SAFE)
}

@Preview(showBackground = true)
@Composable
private fun ChordStatusBadgeMidPreview() {
    ChordStatusBadge(grade = MarginGrade.MID)
}

@Preview(showBackground = true)
@Composable
private fun ChordStatusBadgeWarningPreview() {
    ChordStatusBadge(grade = MarginGrade.WARNING)
}

@Preview(showBackground = true)
@Composable
private fun ChordStatusBadgeDangerPreview() {
    ChordStatusBadge(grade = MarginGrade.DANGER)
}
