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
import com.team.chord.core.ui.component.ChordBottomSheet
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTextField
import com.team.chord.core.ui.component.ChordUnitSelector
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.StatusDanger
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIngredientBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: (name: String, unitPrice: Int, quantity: Double, unit: IngredientUnit) -> Unit,
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    var nameText by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var quantityText by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf(IngredientUnit.G) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var priceError by remember { mutableStateOf<String?>(null) }
    var quantityError by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        var isValid = true

        if (nameText.isBlank()) {
            nameError = "재료명을 입력해주세요"
            isValid = false
        } else {
            nameError = null
        }

        if (priceText.isBlank()) {
            priceError = "가격을 입력해주세요"
            isValid = false
        } else {
            priceError = null
        }

        if (quantityText.isBlank()) {
            quantityError = "사용량을 입력해주세요"
            isValid = false
        } else if (quantityText.toDoubleOrNull() == null) {
            quantityError = "숫자만 입력할 수 있어요"
            isValid = false
        } else {
            quantityError = null
        }

        return isValid
    }

    val isFormValid = nameText.isNotBlank() &&
        priceText.isNotBlank() &&
        quantityText.isNotBlank() &&
        quantityText.toDoubleOrNull() != null

    ChordBottomSheet(
        onDismissRequest = onDismiss,
        title = "재료 추가",
        content = {
            Column {
                // 재료명
                Text(
                    text = "재료명",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = if (nameError != null) StatusDanger else Grayscale500,
                )
                Spacer(modifier = Modifier.height(8.dp))
                ChordTextField(
                    value = nameText,
                    onValueChange = {
                        nameText = it
                        nameError = null
                    },
                    placeholder = "재료명을 입력해주세요",
                    isError = nameError != null,
                    errorMessage = nameError,
                    onClear = { nameText = "" },
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 가격
                Text(
                    text = "가격",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = if (priceError != null) StatusDanger else Grayscale500,
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
                        priceError = null
                    },
                    placeholder = "가격을 입력해주세요",
                    unitText = "원",
                    keyboardType = KeyboardType.Number,
                    isError = priceError != null,
                    errorMessage = priceError,
                    onClear = { priceText = "" },
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 사용량
                Text(
                    text = "사용량",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = if (quantityError != null) StatusDanger else Grayscale500,
                )
                Spacer(modifier = Modifier.height(8.dp))
                ChordTextField(
                    value = quantityText,
                    onValueChange = {
                        quantityText = it
                        quantityError = if (it.isNotBlank() && it.toDoubleOrNull() == null) {
                            "숫자만 입력할 수 있어요"
                        } else {
                            null
                        }
                    },
                    keyboardType = KeyboardType.Decimal,
                    isError = quantityError != null,
                    errorMessage = quantityError,
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
                    enabled = quantityError == null,
                )
            }
        },
        confirmButton = {
            ChordLargeButton(
                text = "추가하기",
                onClick = {
                    if (validate()) {
                        val price = priceText.filter { it.isDigit() }.toIntOrNull() ?: 0
                        val quantity = quantityText.toDoubleOrNull() ?: 0.0
                        onConfirm(nameText, price, quantity, selectedUnit)
                    }
                },
                enabled = isFormValid,
            )
        },
    )
}
