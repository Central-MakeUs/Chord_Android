package com.team.chord.feature.menu.ingredient.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.component.ChordBottomSheet
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTextField
import com.team.chord.core.ui.component.ChordTextFieldStyle
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.feature.menu.ingredient.EditIngredientSheetUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditIngredientBottomSheet(
    sheetState: EditIngredientSheetUi,
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onAmountInputChange: (String) -> Unit,
    onConfirm: () -> Unit,
) {
    ChordBottomSheet(
        onDismissRequest = onDismiss,
        title = sheetState.recipe.name,
        content = {
            Column {
                Text(
                    text = "사용량",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Grayscale900,
                )
                Spacer(modifier = Modifier.height(8.dp))

                ChordTextField(
                    value = sheetState.amountInput,
                    onValueChange = onAmountInputChange,
                    style = ChordTextFieldStyle.Underline,
                    unitText = sheetState.recipe.unit.displayName,
                    keyboardType = KeyboardType.Decimal,
                    onClear = { onAmountInputChange("") },
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Grayscale200,
                            shape = RoundedCornerShape(12.dp),
                        )
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = "재료 정보",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Grayscale700,
                    )
                    InfoRow(
                        label = "단가",
                        value = if (sheetState.isIngredientInfoLoading) "불러오는 중..." else sheetState.unitPriceText,
                    )
                    InfoRow(
                        label = "공급업체",
                        value = if (sheetState.isIngredientInfoLoading) "불러오는 중..." else sheetState.supplierText,
                    )
                }
            }
        },
        confirmButton = {
            ChordLargeButton(
                text = "수정",
                onClick = onConfirm,
                enabled = sheetState.isSubmitEnabled && !isSubmitting,
            )
        },
    )
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Grayscale500,
        )
        Text(
            text = value,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Grayscale700,
        )
    }
}
