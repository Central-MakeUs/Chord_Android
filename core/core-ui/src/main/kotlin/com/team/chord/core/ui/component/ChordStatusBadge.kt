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
    val (text, textColor, backgroundColor) = when (grade.code) {
        "SAFE" -> Triple(grade.name, StatusSafe, StatusSafeBackground)
        "MID" -> Triple(grade.name, StatusMid, StatusMidBackground)
        "WARNING" -> Triple(grade.name, StatusWarning, StatusWarningBackground)
        "DANGER" -> Triple(grade.name, StatusDanger, StatusDangerBackground)
        else -> Triple(grade.name, StatusMid, StatusMidBackground)
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

fun MarginGrade.toStatusColor(): Color = when (code) {
    "SAFE" -> StatusSafe
    "MID" -> StatusMid
    "WARNING" -> StatusWarning
    "DANGER" -> StatusDanger
    else -> StatusMid
}

fun MarginGrade.toStatusBackgroundColor(): Color = when (code) {
    "SAFE" -> StatusSafeBackground
    "MID" -> StatusMidBackground
    "WARNING" -> StatusWarningBackground
    "DANGER" -> StatusDangerBackground
    else -> StatusMidBackground
}

@Preview(showBackground = true)
@Composable
private fun ChordStatusBadgeSafePreview() {
    ChordStatusBadge(grade = MarginGrade(code = "SAFE", name = "안정", message = ""))
}

@Preview(showBackground = true)
@Composable
private fun ChordStatusBadgeMidPreview() {
    ChordStatusBadge(grade = MarginGrade(code = "MID", name = "보통", message = ""))
}

@Preview(showBackground = true)
@Composable
private fun ChordStatusBadgeWarningPreview() {
    ChordStatusBadge(grade = MarginGrade(code = "WARNING", name = "주의", message = ""))
}

@Preview(showBackground = true)
@Composable
private fun ChordStatusBadgeDangerPreview() {
    ChordStatusBadge(grade = MarginGrade(code = "DANGER", name = "위험", message = ""))
}
