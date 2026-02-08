package com.team.chord.feature.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500

/**
 * A toggle row used in the Setting screen.
 *
 * Displays a title on the left and a Material3 Switch on the right.
 *
 * @param title The setting item title.
 * @param checked Whether the switch is currently checked.
 * @param onCheckedChange Callback invoked when the switch state changes.
 * @param modifier Optional [Modifier] for this composable.
 */
@Composable
fun SettingToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Grayscale100)
            .padding(horizontal = 20.dp, vertical = 12.dp),
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

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Grayscale100,
                checkedTrackColor = PrimaryBlue500,
                checkedBorderColor = PrimaryBlue500,
                uncheckedThumbColor = Grayscale100,
                uncheckedTrackColor = Grayscale300,
                uncheckedBorderColor = Grayscale300,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingToggleRowCheckedPreview() {
    SettingToggleRow(
        title = "알림",
        checked = true,
        onCheckedChange = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingToggleRowUncheckedPreview() {
    SettingToggleRow(
        title = "알림",
        checked = false,
        onCheckedChange = {},
    )
}
