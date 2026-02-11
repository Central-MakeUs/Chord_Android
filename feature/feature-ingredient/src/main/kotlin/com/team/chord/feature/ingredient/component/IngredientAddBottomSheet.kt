package com.team.chord.feature.ingredient.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.domain.model.ingredient.IngredientFilter
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTextField
import com.team.chord.core.ui.component.ChordTextFieldStyle
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.R as CoreUiR

/**
 * 재료 추가 Bottom Sheet
 *
 * 새로운 재료의 카테고리(식재료/운영재료), 가격, 용량, 공급업체를 입력할 수 있는 바텀시트 컴포넌트
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientAddBottomSheet(
    ingredientName: String,
    selectedFilter: IngredientFilter,
    price: String,
    amount: String,
    selectedUnit: IngredientUnit,
    supplier: String,
    onFilterSelect: (IngredientFilter) -> Unit,
    onPriceChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onUnitSelect: (IngredientUnit) -> Unit,
    onSupplierChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var unitDropdownExpanded by remember { mutableStateOf(false) }

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
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .windowInsetsPadding(WindowInsets.navigationBars),
        ) {
            // Ingredient Name Title
            Text(
                text = ingredientName,
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Grayscale900,
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filter Chips (식재료 / 운영 재료)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf(
                    IngredientFilter.FOOD_INGREDIENT,
                    IngredientFilter.OPERATIONAL_SUPPLY,
                ).forEach { filter ->
                    IngredientFilterChip(
                        text = filter.displayName,
                        isSelected = selectedFilter == filter,
                        onClick = { onFilterSelect(filter) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Price Section
            Text(
                text = "가격",
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Grayscale600,
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            ChordTextField(
                value = price,
                onValueChange = onPriceChange,
                style = ChordTextFieldStyle.Underline,
                unitText = "원",
                keyboardType = KeyboardType.Number,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Amount Section
            Text(
                text = "용량",
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Grayscale600,
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                // Amount input with underline style
                ChordTextField(
                    value = amount,
                    onValueChange = onAmountChange,
                    style = ChordTextFieldStyle.Underline,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f),
                )

                // Unit dropdown selector
                Box {
                    Row(
                        modifier = Modifier
                            .height(48.dp)
                            .border(
                                width = 1.dp,
                                color = Grayscale300,
                                shape = RoundedCornerShape(8.dp),
                            )
                            .clickable { unitDropdownExpanded = true }
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = selectedUnit.displayName,
                            style = TextStyle(
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = Grayscale900,
                            ),
                        )
                        Icon(
                            painter = painterResource(CoreUiR.drawable.ic_chevron_right),
                            contentDescription = "단위 선택",
                            modifier = Modifier
                                .size(16.dp)
                                .rotate(90f),
                            tint = Grayscale600,
                        )
                    }

                    DropdownMenu(
                        expanded = unitDropdownExpanded,
                        onDismissRequest = { unitDropdownExpanded = false },
                    ) {
                        IngredientUnit.entries.forEach { unit ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = unit.displayName,
                                        style = TextStyle(
                                            fontFamily = PretendardFontFamily,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 16.sp,
                                            color = Grayscale900,
                                        ),
                                    )
                                },
                                onClick = {
                                    onUnitSelect(unit)
                                    unitDropdownExpanded = false
                                },
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Supplier Section
            Text(
                text = "공급업체",
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Grayscale600,
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            ChordTextField(
                value = supplier,
                onValueChange = onSupplierChange,
                style = ChordTextFieldStyle.Underline,
                placeholder = "선택",
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Confirm Button
            ChordLargeButton(
                text = "재료 추가",
                onClick = onConfirm,
                enabled = price.isNotBlank() && amount.isNotBlank(),
            )
        }
    }
}
