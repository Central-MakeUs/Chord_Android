package com.team.chord.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale800
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue200
import com.team.chord.core.ui.theme.PrimaryBlue500
import kotlin.math.min

data class IngredientEditorCategoryOption(
    val code: String,
    val label: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientEditorBottomSheet(
    title: String,
    categoryCode: String,
    categoryOptions: List<IngredientEditorCategoryOption>,
    onCategoryChanged: (String) -> Unit,
    purchaseAmountLabel: String = "용량",
    price: String,
    onPriceChanged: (String) -> Unit,
    pricePlaceholder: String,
    purchaseAmount: String,
    onPurchaseAmountChanged: (String) -> Unit,
    purchaseAmountPlaceholder: String,
    showUsageField: Boolean = true,
    usageLabel: String = "사용량",
    amount: String,
    onAmountChanged: (String) -> Unit,
    amountPlaceholder: String,
    unit: IngredientUnit,
    onUnitChanged: (IngredientUnit) -> Unit,
    supplierLabel: String = "공급업체 (선택)",
    supplier: String,
    onSupplierChanged: (String) -> Unit,
    confirmText: String,
    confirmEnabled: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    contentHeightFraction: Float? = null,
    isCategoryEditable: Boolean = true,
    isPriceEditable: Boolean = true,
    isPurchaseAmountEditable: Boolean = true,
    isUnitEditable: Boolean = true,
    isSupplierEditable: Boolean = true,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
        containerColor = Grayscale100,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        dragHandle = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(
                            color = Grayscale400,
                            shape = RoundedCornerShape(2.dp),
                        ),
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        },
    ) {
        Column(
            modifier = Modifier
                .then(
                    if (contentHeightFraction != null) {
                        Modifier.fillMaxHeight(contentHeightFraction)
                    } else {
                        Modifier
                    },
                )
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .windowInsetsPadding(WindowInsets.navigationBars),
            verticalArrangement = if (contentHeightFraction != null) Arrangement.Bottom else Arrangement.Top,
        ) {
            Text(
                text = title,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Grayscale900,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categoryOptions.forEach { option ->
                    IngredientCategoryChip(
                        text = option.label,
                        isSelected = categoryCode == option.code,
                        enabled = isCategoryEditable,
                        onClick = { onCategoryChanged(option.code) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            IngredientEditorFieldLabel(text = "가격")
            Spacer(modifier = Modifier.height(8.dp))
            IngredientEditorUnderlineField(
                value = price,
                onValueChange = onPriceChanged,
                placeholder = pricePlaceholder,
                keyboardType = KeyboardType.Number,
                visualTransformation = SuffixAppendingVisualTransformation(
                    base = DigitGroupingVisualTransformation(),
                    suffix = "원",
                ),
                enabled = isPriceEditable,
                readOnlyValue = price.takeIf { it.isNotEmpty() }?.let { "${formatDigits(it)}원" } ?: "-",
            )

            Spacer(modifier = Modifier.height(24.dp))

            IngredientEditorFieldLabel(text = purchaseAmountLabel)
            Spacer(modifier = Modifier.height(8.dp))
            IngredientEditorUnderlineField(
                value = purchaseAmount,
                onValueChange = onPurchaseAmountChanged,
                placeholder = purchaseAmountPlaceholder,
                keyboardType = KeyboardType.Number,
                enabled = isPurchaseAmountEditable,
                readOnlyValue = purchaseAmount.ifEmpty { "-" },
                trailingContent = {
                    PurchaseUnitSelector(
                        selectedUnit = unit,
                        onUnitSelected = onUnitChanged,
                        enabled = isUnitEditable,
                    )
                },
            )

            if (showUsageField) {
                Spacer(modifier = Modifier.height(24.dp))

                IngredientEditorFieldLabel(text = usageLabel)
                Spacer(modifier = Modifier.height(8.dp))
                IngredientEditorUnderlineField(
                    value = amount,
                    onValueChange = onAmountChanged,
                    placeholder = amountPlaceholder,
                    keyboardType = KeyboardType.Number,
                    visualTransformation = SuffixAppendingVisualTransformation(
                        base = VisualTransformation.None,
                        suffix = unit.displayName,
                    ),
                    readOnlyValue = amount.takeIf { it.isNotEmpty() }?.let { "$it${unit.displayName}" } ?: "-",
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            IngredientEditorFieldLabel(text = supplierLabel)
            Spacer(modifier = Modifier.height(8.dp))
            IngredientEditorUnderlineField(
                value = supplier,
                onValueChange = onSupplierChanged,
                placeholder = "공급업체명 입력",
                enabled = isSupplierEditable,
                readOnlyValue = supplier.ifEmpty { "-" },
            )

            Spacer(modifier = Modifier.height(24.dp))

            ChordLargeButton(
                text = confirmText,
                onClick = onConfirm,
                enabled = confirmEnabled,
            )
        }
    }
}

@Composable
private fun IngredientEditorFieldLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = Grayscale900,
        modifier = modifier,
    )
}

@Composable
private fun IngredientEditorUnderlineField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
    readOnlyValue: String = "-",
    suffixText: String? = null,
    trailingContent: @Composable (() -> Unit)? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (enabled) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = Grayscale900,
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    cursorBrush = SolidColor(Grayscale800),
                    visualTransformation = visualTransformation,
                    decorationBox = { innerTextField ->
                        Box {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = TextStyle(
                                        fontFamily = PretendardFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp,
                                        color = Grayscale500,
                                    ),
                                )
                            }
                            innerTextField()
                        }
                    },
                )
            } else {
                Text(
                    text = readOnlyValue,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Grayscale800,
                    modifier = Modifier.weight(1f),
                )
            }

            if (suffixText != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = suffixText,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = if (enabled) Grayscale900 else Grayscale800,
                )
            }

            if (trailingContent != null) {
                Spacer(modifier = Modifier.width(12.dp))
                trailingContent()
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Grayscale300, thickness = 1.dp)
    }
}

@Composable
private fun IngredientCategoryChip(
    text: String,
    isSelected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = if (isSelected) PrimaryBlue200 else Grayscale200,
                shape = RoundedCornerShape(8.dp),
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = if (isSelected) PrimaryBlue500 else Grayscale600,
        )
    }
}

@Composable
private fun PurchaseUnitSelector(
    selectedUnit: IngredientUnit,
    onUnitSelected: (IngredientUnit) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val availableUnits = selectableUnits.filterNot { it == selectedUnit }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable(enabled = enabled) { expanded = true }
                .padding(start = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(28.dp)
                    .background(Grayscale300),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "단위",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Grayscale500,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = selectedUnit.displayName,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Grayscale900,
            )
            if (enabled) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "단위 선택",
                    tint = Grayscale500,
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        DropdownMenu(
            expanded = expanded && availableUnits.isNotEmpty(),
            onDismissRequest = { expanded = false },
            offset = DpOffset(0.dp, 4.dp),
            modifier = Modifier
                .width(92.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Color(0x1A000000),
                    spotColor = Color(0x1A000000),
                ),
            shape = RoundedCornerShape(16.dp),
            containerColor = Grayscale100,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
        ) {
            availableUnits.forEachIndexed { index, unit ->
                DropdownMenuItem(
                    text = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = unit.displayName,
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp,
                                color = Grayscale900,
                            )
                        }
                    },
                    onClick = {
                        expanded = false
                        onUnitSelected(unit)
                    },
                )
                if (index < availableUnits.lastIndex) {
                    HorizontalDivider(color = Grayscale300, thickness = 1.dp)
                }
            }
        }
    }
}

private val selectableUnits = listOf(
    IngredientUnit.G,
    IngredientUnit.ML,
    IngredientUnit.EA,
)

private fun formatDigits(value: String): String = value.toLongOrNull()?.let(::formatDigits) ?: value

private fun formatDigits(value: Long): String = java.text.NumberFormat
    .getNumberInstance(java.util.Locale.KOREA)
    .format(value)

private class SuffixAppendingVisualTransformation(
    private val base: VisualTransformation,
    private val suffix: String,
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformed = base.filter(text)
        if (text.text.isEmpty()) return transformed

        val transformedText = transformed.text.text
        val suffixLength = suffix.length

        return TransformedText(
            text = AnnotatedString(transformedText + suffix),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int =
                    transformed.offsetMapping.originalToTransformed(offset)

                override fun transformedToOriginal(offset: Int): Int {
                    val clampedOffset = min(offset, transformedText.length)
                    return transformed.offsetMapping.transformedToOriginal(clampedOffset)
                }
            },
        )
    }
}
