package com.team.chord.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500

data class RadioOption(
    val id: String,
    val label: String,
)

@Composable
fun ChordRadioGroup(
    options: List<RadioOption>,
    selectedOptionId: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        options.forEachIndexed { index, option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(option.id) },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = option.id == selectedOptionId,
                    onClick = { onOptionSelected(option.id) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = PrimaryBlue500,
                        unselectedColor = Grayscale500,
                    ),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = option.label,
                    style = TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        color = if (option.id == selectedOptionId) PrimaryBlue500 else Grayscale900,
                    ),
                )
            }
            if (index < options.lastIndex) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChordRadioGroupPreview() {
    ChordRadioGroup(
        options = listOf(
            RadioOption("1", "음료"),
            RadioOption("2", "디저트"),
            RadioOption("3", "푸드"),
        ),
        selectedOptionId = "1",
        onOptionSelected = {},
    )
}
