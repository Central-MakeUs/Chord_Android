package com.team.chord.feature.setup.menuentry

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale800
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.feature.setup.component.SetupButton
import com.team.chord.feature.setup.component.SetupTextField
import com.team.chord.feature.setup.component.SetupTopBar
import com.team.chord.feature.setup.component.SmallDropdown

@Composable
fun MenuEntryScreen(
    onNavigateBack: () -> Unit,
    onMenuRegistered: (String, Int, MenuCategory) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MenuEntryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val registeredMenu by viewModel.registeredMenu.collectAsStateWithLifecycle()

    LaunchedEffect(registeredMenu) {
        registeredMenu?.let { menu ->
            onMenuRegistered(menu.name, menu.price, menu.category)
            viewModel.consumeRegisteredMenu()
        }
    }

    MenuEntryScreenContent(
        uiState = uiState,
        onBackClick = onNavigateBack,
        onCategorySelected = viewModel::onCategorySelected,
        onMenuNameChanged = viewModel::onMenuNameChanged,
        onMenuPriceChanged = viewModel::onMenuPriceChanged,
        onNewIngredientNameChanged = viewModel::onNewIngredientNameChanged,
        onAddIngredient = viewModel::onAddIngredient,
        onIngredientToggle = viewModel::onIngredientToggle,
        onRemoveIngredient = viewModel::onRemoveIngredient,
        onIngredientPriceChanged = viewModel::onIngredientPriceChanged,
        onWeightChanged = viewModel::onWeightChanged,
        onWeightUnitSelected = viewModel::onWeightUnitSelected,
        onWeightUnitDropdownToggle = viewModel::onWeightUnitDropdownToggle,
        onWeightUnitDropdownDismiss = viewModel::onWeightUnitDropdownDismiss,
        onPreparationMinutesChanged = viewModel::onPreparationMinutesChanged,
        onPreparationSecondsChanged = viewModel::onPreparationSecondsChanged,
        onRegisterClicked = viewModel::onRegisterClicked,
        modifier = modifier,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MenuEntryScreenContent(
    uiState: MenuEntryUiState,
    onBackClick: () -> Unit,
    onCategorySelected: (MenuCategory) -> Unit,
    onMenuNameChanged: (String) -> Unit,
    onMenuPriceChanged: (String) -> Unit,
    onNewIngredientNameChanged: (String) -> Unit,
    onAddIngredient: () -> Unit,
    onIngredientToggle: (String) -> Unit,
    onRemoveIngredient: (String) -> Unit,
    onIngredientPriceChanged: (String) -> Unit,
    onWeightChanged: (String) -> Unit,
    onWeightUnitSelected: (WeightUnit) -> Unit,
    onWeightUnitDropdownToggle: () -> Unit,
    onWeightUnitDropdownDismiss: () -> Unit,
    onPreparationMinutesChanged: (String) -> Unit,
    onPreparationSecondsChanged: (String) -> Unit,
    onRegisterClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(Grayscale100),
    ) {
        SetupTopBar(
            title = "메뉴 입력",
            onBackClick = onBackClick,
        )

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            CategoryToggle(
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = onCategorySelected,
            )

            Spacer(modifier = Modifier.height(24.dp))

            FieldLabel(text = "메뉴명")
            Spacer(modifier = Modifier.height(8.dp))
            SetupTextField(
                value = uiState.menuName,
                onValueChange = onMenuNameChanged,
                placeholder = "메뉴명을 입력해주세요",
                imeAction = ImeAction.Next,
            )

            Spacer(modifier = Modifier.height(24.dp))

            FieldLabel(text = "메뉴 가격")
            Spacer(modifier = Modifier.height(8.dp))
            SetupTextField(
                value = uiState.menuPrice,
                onValueChange = onMenuPriceChanged,
                placeholder = "가격을 입력해주세요",
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                suffix = "원",
            )

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(color = Grayscale300, thickness = 1.dp)

            Spacer(modifier = Modifier.height(24.dp))

            FieldLabel(text = "재료")
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                uiState.ingredients.forEach { ingredient ->
                    IngredientChip(
                        ingredient = ingredient,
                        onClick = { onIngredientToggle(ingredient.id) },
                        onRemove = { onRemoveIngredient(ingredient.id) },
                    )
                }
                AddIngredientChip(
                    value = uiState.newIngredientName,
                    onValueChange = onNewIngredientNameChanged,
                    onAdd = onAddIngredient,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            FieldLabel(text = "재료 가격")
            Spacer(modifier = Modifier.height(8.dp))
            SetupTextField(
                value = uiState.ingredientPrice,
                onValueChange = onIngredientPriceChanged,
                placeholder = "재료 가격을 입력해주세요",
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                suffix = "원",
            )

            Spacer(modifier = Modifier.height(24.dp))

            FieldLabel(text = "중량")
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SetupTextField(
                    value = uiState.weight,
                    onValueChange = onWeightChanged,
                    placeholder = "중량",
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(12.dp))
                SmallDropdown(
                    selectedValue = uiState.weightUnit.displayName,
                    options = WeightUnit.entries.map { it.displayName },
                    expanded = uiState.isWeightUnitDropdownExpanded,
                    onExpandedChange = onWeightUnitDropdownToggle,
                    onOptionSelected = { displayName ->
                        WeightUnit.entries.find { it.displayName == displayName }?.let {
                            onWeightUnitSelected(it)
                        }
                    },
                    onDismiss = onWeightUnitDropdownDismiss,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FieldLabel(text = "제조시간", modifier = Modifier.weight(1f))
                TooltipBox(text = "음료의 평균 제조시간은 1분 30초예요")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SetupTextField(
                    value = uiState.preparationMinutes,
                    onValueChange = onPreparationMinutesChanged,
                    placeholder = "0",
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    suffix = "분",
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(12.dp))
                SetupTextField(
                    value = uiState.preparationSeconds,
                    onValueChange = onPreparationSecondsChanged,
                    placeholder = "0",
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                    suffix = "초",
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        SetupButton(
            text = "이대로 등록",
            onClick = onRegisterClicked,
            enabled = uiState.isRegisterEnabled,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        )
    }
}

@Composable
private fun CategoryToggle(
    selectedCategory: MenuCategory,
    onCategorySelected: (MenuCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Grayscale200),
    ) {
        MenuCategory.entries.forEach { category ->
            val isSelected = category == selectedCategory
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(4.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) Grayscale100 else Grayscale200)
                        .clickable { onCategorySelected(category) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = category.displayName,
                    fontFamily = PretendardFontFamily,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = 14.sp,
                    color = if (isSelected) Grayscale900 else Grayscale500,
                )
            }
        }
    }
}

@Composable
private fun IngredientChip(
    ingredient: Ingredient,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = if (ingredient.isSelected) Grayscale800 else Grayscale100,
        border =
            if (!ingredient.isSelected) {
                androidx.compose.foundation.BorderStroke(1.dp, Grayscale300)
            } else {
                null
            },
    ) {
        Row(
            modifier =
                Modifier
                    .clickable(onClick = onClick)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = ingredient.name,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = if (ingredient.isSelected) Grayscale100 else Grayscale900,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "재료 삭제",
                modifier =
                    Modifier
                        .size(16.dp)
                        .clickable(onClick = onRemove),
                tint = if (ingredient.isSelected) Grayscale100 else Grayscale500,
            )
        }
    }
}

@Composable
private fun AddIngredientChip(
    value: String,
    onValueChange: (String) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = Grayscale100,
        border = androidx.compose.foundation.BorderStroke(1.dp, Grayscale300),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            androidx.compose.foundation.text.BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.width(60.dp),
                textStyle =
                    androidx.compose.ui.text.TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Grayscale900,
                    ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = "재료명",
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Grayscale500,
                            )
                        }
                        innerTextField()
                    }
                },
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "재료 추가",
                modifier =
                    Modifier
                        .size(16.dp)
                        .clickable(onClick = onAdd),
                tint = Grayscale500,
            )
        }
    }
}

@Composable
private fun TooltipBox(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .background(
                    color = Grayscale800,
                    shape = RoundedCornerShape(8.dp),
                ).padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Text(
            text = text,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = Grayscale100,
        )
    }
}

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
