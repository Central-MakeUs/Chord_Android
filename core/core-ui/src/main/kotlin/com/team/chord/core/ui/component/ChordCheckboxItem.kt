package com.team.chord.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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

@Composable
fun ChordCheckboxItem(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onCheckedChange(!checked) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = PrimaryBlue500,
                uncheckedColor = Grayscale300,
                checkmarkColor = Grayscale100,
            ),
        )
        Spacer(modifier = Modifier.width(8.dp))
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun ChordCheckboxItemUncheckedPreview() {
    ChordCheckboxItem(
        checked = false,
        onCheckedChange = {},
    ) {
        Text(
            text = "원두",
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Grayscale900,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChordCheckboxItemCheckedPreview() {
    ChordCheckboxItem(
        checked = true,
        onCheckedChange = {},
    ) {
        Text(
            text = "원두",
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Grayscale900,
            ),
        )
    }
}
