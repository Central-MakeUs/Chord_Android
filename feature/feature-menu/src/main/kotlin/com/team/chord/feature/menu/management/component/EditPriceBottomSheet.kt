package com.team.chord.feature.menu.management.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import com.team.chord.core.ui.component.ChordBottomSheet
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTextField
import com.team.chord.core.ui.theme.Grayscale700
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPriceBottomSheet(
    currentPrice: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)
    var priceText by remember { mutableStateOf(numberFormat.format(currentPrice)) }

    ChordBottomSheet(
        onDismissRequest = onDismiss,
        title = "가격을 입력해주세요",
        content = {
            ChordTextField(
                value = priceText,
                onValueChange = { input ->
                    val digitsOnly = input.filter { it.isDigit() }
                    if (digitsOnly.isNotEmpty()) {
                        priceText = numberFormat.format(digitsOnly.toLong())
                    } else {
                        priceText = ""
                    }
                },
                placeholder = "가격을 입력해주세요",
                unitText = "원",
                keyboardType = KeyboardType.Number,
                onClear = { priceText = "" },
                cornerRadius = 24,
                borderColor = Grayscale700,
            )
        },
        confirmButton = {
            ChordLargeButton(
                text = "완료",
                onClick = {
                    val price = priceText.filter { it.isDigit() }.toIntOrNull() ?: 0
                    onConfirm(price)
                },
                enabled = priceText.isNotBlank(),
            )
        },
    )
}
