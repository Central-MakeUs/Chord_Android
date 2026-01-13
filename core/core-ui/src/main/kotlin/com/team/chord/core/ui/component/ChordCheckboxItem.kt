package com.team.chord.core.ui.component

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
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily

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
        Icon(
            painter = painterResource(
                if (checked) R.drawable.ic_checkbox else R.drawable.ic_un_checkbox
            ),
            contentDescription = if (checked) "선택됨" else "선택 안됨",
            modifier = Modifier.size(32.dp),
            tint = androidx.compose.ui.graphics.Color.Unspecified,
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
