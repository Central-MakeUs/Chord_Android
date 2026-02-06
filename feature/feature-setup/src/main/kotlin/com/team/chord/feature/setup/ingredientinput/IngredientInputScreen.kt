package com.team.chord.feature.setup.ingredientinput

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.StarBorder
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.ui.R
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTooltipBubble
import com.team.chord.core.ui.component.TooltipDirection
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale800
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.setup.component.StepIndicator
import java.text.NumberFormat
import java.util.Locale

@Composable
fun IngredientInputScreen(
    onNavigateBack: () -> Unit,
    onNavigateToConfirm: (List<SelectedIngredient>) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IngredientInputViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    IngredientInputScreenContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSuggestionClicked = viewModel::onSuggestionClicked,
        onAddNewIngredient = viewModel::onAddNewIngredient,
        onRemoveIngredient = viewModel::onRemoveIngredient,
        onBottomSheetDismissed = viewModel::onBottomSheetDismissed,
        onBottomSheetCategoryChanged = viewModel::onBottomSheetCategoryChanged,
        onBottomSheetPriceChanged = viewModel::onBottomSheetPriceChanged,
        onBottomSheetAmountChanged = viewModel::onBottomSheetAmountChanged,
        onBottomSheetUnitChanged = viewModel::onBottomSheetUnitChanged,
        onBottomSheetSupplierChanged = viewModel::onBottomSheetSupplierChanged,
        onAddIngredient = viewModel::onAddIngredient,
        onPreviousClick = onNavigateBack,
        onNextClick = { onNavigateToConfirm(viewModel.getSelectedIngredients()) },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun IngredientInputScreenContent(
    uiState: IngredientInputUiState,
    onNavigateBack: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSuggestionClicked: (IngredientSuggestion) -> Unit,
    onAddNewIngredient: () -> Unit,
    onRemoveIngredient: (Long) -> Unit,
    onBottomSheetDismissed: () -> Unit,
    onBottomSheetCategoryChanged: (String) -> Unit,
    onBottomSheetPriceChanged: (String) -> Unit,
    onBottomSheetAmountChanged: (String) -> Unit,
    onBottomSheetUnitChanged: (IngredientUnit) -> Unit,
    onBottomSheetSupplierChanged: (String) -> Unit,
    onAddIngredient: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        // Top Bar
        IngredientInputTopBar(
            onBackClick = onNavigateBack,
            onFavoriteClick = { /* TODO: Toggle favorite */ },
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
        ) {
            // Step Indicator
            StepIndicator(
                currentStep = 2,
                totalSteps = 2,
                modifier = Modifier.padding(start = 24.dp, top = 16.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Search Section
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                FieldLabel(text = "재료명")
                Spacer(modifier = Modifier.height(8.dp))
                IngredientSearchField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchQueryChanged,
                    placeholder = "추가할 재료명을 입력해주세요",
                )

                // Tooltip
                if (uiState.searchQuery.isEmpty() && uiState.selectedIngredients.isEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        ChordTooltipBubble(
                            text = "필요한 재료가 더 있으신가요? 직접 입력해서 추가해보세요",
                            direction = TooltipDirection.UpRight,
                        )
                    }
                }

                // Search Results
                if (uiState.searchQuery.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    SearchResultsList(
                        suggestions = uiState.searchResults,
                        query = uiState.searchQuery,
                        onSuggestionClicked = onSuggestionClicked,
                        onAddNewIngredient = onAddNewIngredient,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Ingredient List Section
            if (uiState.selectedIngredients.isNotEmpty()) {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        FieldLabel(text = "재료 리스트")
                        Text(
                            text = "선택",
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Grayscale500,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, Grayscale300, RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .clickable { /* TODO: Multi-select mode */ },
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    uiState.selectedIngredients.forEach { ingredient ->
                        IngredientListItem(
                            ingredient = ingredient,
                            onRemoveClick = { onRemoveIngredient(ingredient.id) },
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // Bottom Buttons
        BottomNavigationButtons(
            isNextEnabled = uiState.isNextEnabled,
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
        )
    }

    // Bottom Sheet
    if (uiState.showAddBottomSheet && uiState.bottomSheetIngredient != null) {
        AddIngredientBottomSheet(
            state = uiState.bottomSheetIngredient,
            onDismiss = onBottomSheetDismissed,
            onCategoryChanged = onBottomSheetCategoryChanged,
            onPriceChanged = onBottomSheetPriceChanged,
            onAmountChanged = onBottomSheetAmountChanged,
            onUnitChanged = onBottomSheetUnitChanged,
            onSupplierChanged = onBottomSheetSupplierChanged,
            onConfirm = onAddIngredient,
        )
    }
}

@Composable
private fun IngredientInputTopBar(
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "뒤로가기",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(24.dp)
                .clickable(onClick = onBackClick),
            tint = Grayscale900,
        )

        Icon(
            imageVector = Icons.Outlined.StarBorder,
            contentDescription = "즐겨찾기",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(24.dp)
                .clickable(onClick = onFavoriteClick),
            tint = Grayscale900,
        )
    }
}

@Composable
private fun IngredientSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = Grayscale900,
                ),
                singleLine = true,
                cursorBrush = SolidColor(Grayscale800),
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = TextStyle(
                                    fontFamily = PretendardFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 20.sp,
                                    color = Grayscale500,
                                ),
                            )
                        }
                        innerTextField()
                    }
                },
            )
            if (value.isNotEmpty()) {
                Icon(
                    painter = painterResource(R.drawable.ic_close_circle),
                    contentDescription = "지우기",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onValueChange("") },
                    tint = Grayscale500,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            color = if (value.isEmpty()) PrimaryBlue500 else Grayscale300,
            thickness = 1.dp,
        )
    }
}

@Composable
private fun SearchResultsList(
    suggestions: List<IngredientSuggestion>,
    query: String,
    onSuggestionClicked: (IngredientSuggestion) -> Unit,
    onAddNewIngredient: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        suggestions.forEach { suggestion ->
            SearchResultItem(
                suggestion = suggestion,
                onClick = { onSuggestionClicked(suggestion) },
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Add new ingredient option when no results or always show
        if (suggestions.isEmpty() || suggestions.none { it.name.equals(query, ignoreCase = true) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onAddNewIngredient)
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = PrimaryBlue500,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "\"$query\" 직접 추가하기",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = PrimaryBlue500,
                )
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    suggestion: IngredientSuggestion,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = suggestion.name,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Grayscale900,
            )
            if (suggestion.isTemplate) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "자동완성",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = PrimaryBlue500,
                    modifier = Modifier
                        .background(
                            color = PrimaryBlue500.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp),
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                )
            }
        }
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "추가",
            modifier = Modifier.size(20.dp),
            tint = Grayscale500,
        )
    }
}

@Composable
private fun IngredientListItem(
    ingredient: SelectedIngredient,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = ingredient.name,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Grayscale900,
        )
        Text(
            text = "${ingredient.amount}${ingredient.unit.displayName}  ${numberFormat.format(ingredient.price)}원",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Grayscale500,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddIngredientBottomSheet(
    state: IngredientBottomSheetState,
    onDismiss: () -> Unit,
    onCategoryChanged: (String) -> Unit,
    onPriceChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onUnitChanged: (IngredientUnit) -> Unit,
    onSupplierChanged: (String) -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

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
            // Title (Ingredient Name)
            Text(
                text = state.name,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Grayscale900,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ingredientCategoryOptions.forEach { (code, displayName) ->
                    CategoryChip(
                        text = displayName,
                        isSelected = state.categoryCode == code,
                        onClick = { onCategoryChanged(code) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Price Field
            FieldLabel(text = if (state.isTemplate) "단가" else "가격")
            Spacer(modifier = Modifier.height(8.dp))
            BottomSheetTextField(
                value = if (state.price.isNotEmpty()) {
                    numberFormat.format(state.price.toLongOrNull() ?: 0)
                } else {
                    ""
                },
                onValueChange = onPriceChanged,
                placeholder = state.suggestedPrice?.let {
                    "${numberFormat.format(it)}원"
                } ?: "가격을 입력해주세요",
                suffix = "원",
                keyboardType = KeyboardType.Number,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Amount Field with Unit Dropdown
            FieldLabel(text = "사용량")
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    BottomSheetTextField(
                        value = state.amount,
                        onValueChange = onAmountChanged,
                        placeholder = state.suggestedAmount?.toString() ?: "사용량을 입력해주세요",
                        keyboardType = KeyboardType.Number,
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                UnitDropdown(
                    selectedUnit = state.unit,
                    onUnitSelected = onUnitChanged,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Supplier Field
            FieldLabel(text = "공급업체")
            Spacer(modifier = Modifier.height(8.dp))
            BottomSheetTextField(
                value = state.supplier,
                onValueChange = onSupplierChanged,
                placeholder = "공급업체명을 알려주세요",
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Confirm Button
            ChordLargeButton(
                text = "추가하기",
                onClick = onConfirm,
                enabled = state.isAddEnabled,
            )
        }
    }
}

@Composable
private fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = if (isSelected) Grayscale900 else Grayscale300,
                shape = RoundedCornerShape(8.dp),
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Text(
            text = text,
            fontFamily = PretendardFontFamily,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            fontSize = 14.sp,
            color = if (isSelected) Grayscale900 else Grayscale500,
        )
    }
}

@Composable
private fun BottomSheetTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    suffix: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
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
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = TextStyle(
                                    fontFamily = PretendardFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    color = PrimaryBlue500,
                                ),
                            )
                        }
                        innerTextField()
                    }
                },
            )
            if (suffix != null && value.isNotEmpty()) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = suffix,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Grayscale900,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Grayscale300, thickness = 1.dp)
    }
}

@Composable
private fun UnitDropdown(
    selectedUnit: IngredientUnit,
    onUnitSelected: (IngredientUnit) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .clickable { expanded = true }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = selectedUnit.displayName,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Grayscale900,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "단위 선택",
                modifier = Modifier.size(20.dp),
                tint = Grayscale500,
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            listOf(IngredientUnit.G, IngredientUnit.ML, IngredientUnit.EA).forEach { unit ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = unit.displayName,
                            fontFamily = PretendardFontFamily,
                            fontWeight = if (unit == selectedUnit) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 16.sp,
                            color = Grayscale900,
                        )
                    },
                    onClick = {
                        onUnitSelected(unit)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun BottomNavigationButtons(
    isNextEnabled: Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Previous Button
        Box(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(PrimaryBlue500)
                .clickable(onClick = onPreviousClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "이전",
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Grayscale100,
                ),
            )
        }

        // Next Button
        Box(
            modifier = Modifier
                .weight(2f)
                .height(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (isNextEnabled) Grayscale200 else Grayscale200)
                .clickable(enabled = isNextEnabled, onClick = onNextClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "다음",
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = if (isNextEnabled) Grayscale900 else Grayscale500,
                ),
            )
        }
    }
}

private val ingredientCategoryOptions = listOf(
    "FOOD_MATERIAL" to "식재료",
    "OPERATIONAL" to "운영 재료",
)

@Composable
private fun FieldLabel(
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

@Preview(showBackground = true)
@Composable
private fun IngredientInputScreenContentPreview() {
    IngredientInputScreenContent(
        uiState = IngredientInputUiState(
            selectedIngredients = listOf(
                SelectedIngredient(
                    id = 1,
                    name = "원두",
                    amount = 30,
                    unit = IngredientUnit.G,
                    price = 800,
                ),
                SelectedIngredient(
                    id = 2,
                    name = "물",
                    amount = 250,
                    unit = IngredientUnit.ML,
                    price = 150,
                ),
            ),
            isNextEnabled = true,
        ),
        onNavigateBack = {},
        onSearchQueryChanged = {},
        onSuggestionClicked = {},
        onAddNewIngredient = {},
        onRemoveIngredient = {},
        onBottomSheetDismissed = {},
        onBottomSheetCategoryChanged = {},
        onBottomSheetPriceChanged = {},
        onBottomSheetAmountChanged = {},
        onBottomSheetUnitChanged = {},
        onBottomSheetSupplierChanged = {},
        onAddIngredient = {},
        onPreviousClick = {},
        onNextClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun IngredientInputScreenContentSearchPreview() {
    IngredientInputScreenContent(
        uiState = IngredientInputUiState(
            searchQuery = "흑임자",
            searchResults = listOf(
                IngredientSuggestion(
                    ingredientId = 1,
                    name = "흑임자 토핑",
                    isTemplate = true,
                ),
            ),
        ),
        onNavigateBack = {},
        onSearchQueryChanged = {},
        onSuggestionClicked = {},
        onAddNewIngredient = {},
        onRemoveIngredient = {},
        onBottomSheetDismissed = {},
        onBottomSheetCategoryChanged = {},
        onBottomSheetPriceChanged = {},
        onBottomSheetAmountChanged = {},
        onBottomSheetUnitChanged = {},
        onBottomSheetSupplierChanged = {},
        onAddIngredient = {},
        onPreviousClick = {},
        onNextClick = {},
    )
}
