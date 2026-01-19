package com.team.chord.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.R
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500

private val ToastBackgroundColor = Color(0xB2151B26)

@Composable
fun ChordToast(
    text: String,
    modifier: Modifier = Modifier,
    @DrawableRes leadingIcon: Int? = null,
    backgroundColor: Color = ToastBackgroundColor,
    contentColor: Color = Grayscale100,
    iconBackgroundColor: Color = PrimaryBlue500,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 8.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leadingIcon != null) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            color = iconBackgroundColor,
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(leadingIcon),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = contentColor,
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = contentColor,
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChordToastPreview() {
    ChordToast(
        text = "템플릿이 적용됐어요",
        leadingIcon = R.drawable.ic_check,
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordToastWithoutIconPreview() {
    ChordToast(
        text = "저장되었습니다",
    )
}
