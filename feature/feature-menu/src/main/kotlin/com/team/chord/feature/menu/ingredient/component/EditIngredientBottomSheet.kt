package com.team.chord.feature.menu.ingredient.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.domain.model.menu.MenuIngredient
import com.team.chord.core.ui.component.ChordBottomSheet
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTextField
import com.team.chord.core.ui.component.ChordUnitSelector
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.PretendardFontFamily
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditIngredientBottomSheet(
    ingredient: MenuIngredient,
    onDismiss: () -> Unit,
    onConfirm: (name: String, unitPrice: Int, quantity: Double, unit: IngredientUnit) -> Unit,
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    var priceText by remember { mutableStateOf(numberFormat.format(ingredient.unitPrice)) }
    var quantityText by remember { mutableStateOf(ingredient.quantity.toString()) }
    var selectedUnit by remember { mutableStateOf(ingredient.unit) }

    val isValid = priceText.isNotBlank() &&
        quantityText.isNotBlank() &&
        quantityText.toDoubleOrNull() != null

    ChordBottomSheet(
        onDismissRequest = onDismiss,
        title = ingredient.name,
        content = {
            Column {
                // 가격
                Text(
                    text = "가격",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Grayscale500,
                )
                Spacer(modifier = Modifier.height(8.dp))
                ChordTextField(
                    value = priceText,
                    onValueChange = { input ->
                        val digitsOnly = input.filter { it.isDigit() }
                        priceText = if (digitsOnly.isNotEmpty()) {
                            numberFormat.format(digitsOnly.toLong())
                        } else {
                            ""
                        }
                    },
                    unitText = "원",
                    keyboardType = KeyboardType.Number,
                    onClear = { priceText = "" },
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 사용량
                Text(
                    text = "사용량",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Grayscale500,
                )
                Spacer(modifier = Modifier.height(8.dp))
                ChordTextField(
                    value = quantityText,
                    onValueChange = { quantityText = it },
                    keyboardType = KeyboardType.Decimal,
                    onClear = { quantityText = "" },
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 단위
                Text(
                    text = "단위",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Grayscale500,
                )
                Spacer(modifier = Modifier.height(8.dp))
                ChordUnitSelector(
                    selectedUnit = selectedUnit,
                    onUnitSelected = { selectedUnit = it },
                )
            }
        },
        confirmButton = {
            ChordLargeButton(
                text = "수정",
                onClick = {
                    val price = priceText.filter { it.isDigit() }.toIntOrNull() ?: 0
                    val quantity = quantityText.toDoubleOrNull() ?: 0.0
                    onConfirm(ingredient.name, price, quantity, selectedUnit)
                },
                enabled = isValid,
            )
        },
    )
}
