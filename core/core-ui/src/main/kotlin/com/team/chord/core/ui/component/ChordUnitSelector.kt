package com.team.chord.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue100
import com.team.chord.core.ui.theme.PrimaryBlue500

@Composable
fun ChordUnitSelector(
    selectedUnit: IngredientUnit,
    onUnitSelected: (IngredientUnit) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        IngredientUnit.entries.forEach { unit ->
            val isSelected = unit == selectedUnit
            Button(
                onClick = { onUnitSelected(unit) },
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                enabled = enabled,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) PrimaryBlue100 else Grayscale100,
                    contentColor = if (isSelected) Grayscale900 else Grayscale500,
                    disabledContainerColor = Grayscale100,
                    disabledContentColor = Grayscale300,
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = when {
                        !enabled -> Grayscale400
                        isSelected -> PrimaryBlue500
                        else -> Grayscale400
                    },
                ),
            ) {
                Text(
                    text = unit.displayName,
                    style = TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                    ),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChordUnitSelectorPreview() {
    ChordUnitSelector(
        selectedUnit = IngredientUnit.G,
        onUnitSelected = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordUnitSelectorDisabledPreview() {
    ChordUnitSelector(
        selectedUnit = IngredientUnit.G,
        onUnitSelected = {},
        enabled = false,
    )
}
