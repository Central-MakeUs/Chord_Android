package com.team.chord.feature.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.R
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500

/**
 * A clickable menu row used in the Setting screen.
 *
 * Displays a title on the left and a chevron-right icon on the right.
 * Optionally shows supplementary text (e.g., subscription status) before the chevron.
 *
 * @param title The menu item title.
 * @param onClick Callback invoked when the row is clicked.
 * @param modifier Optional [Modifier] for this composable.
 * @param subText Optional text displayed before the chevron in [PrimaryBlue500] color.
 */
@Composable
fun SettingMenuRow(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subText: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Grayscale100)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Grayscale900,
            ),
        )

        Spacer(modifier = Modifier.weight(1f))

        if (subText != null) {
            Text(
                text = subText,
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = PrimaryBlue500,
                ),
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        Icon(
            painter = painterResource(R.drawable.ic_chevron_right),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Grayscale500,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingMenuRowPreview() {
    SettingMenuRow(
        title = "구독관리",
        onClick = {},
        subText = "요금제 구독중",
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingMenuRowNoSubTextPreview() {
    SettingMenuRow(
        title = "FAQ",
        onClick = {},
    )
}
