package com.team.chord.feature.menu.add.menudetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.R
import com.team.chord.core.ui.component.ChordBottomSheet
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTimeWheelPicker
import com.team.chord.core.ui.component.ChordToast
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale800
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue100
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.menu.add.component.StepIndicator
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MenuDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToIngredientInput: (MenuDetailData) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MenuDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MenuDetailScreenContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onMenuNameChanged = viewModel::onMenuNameChanged,
        onPriceChanged = viewModel::onPriceChanged,
        onCategorySelected = viewModel::onCategorySelected,
        onPreparationTimeClick = { viewModel.onShowTimePicker() },
        showTimePicker = uiState.showTimePicker,
        onTimePickerDismiss = { viewModel.onDismissTimePicker() },
        onTimePickerConfirm = { minutes, seconds ->
            viewModel.onPreparationTimeChanged(minutes, seconds)
            viewModel.onDismissTimePicker()
        },
        onCategoryPickerClick = { viewModel.onShowCategoryPicker() },
        onCategoryPickerDismiss = { viewModel.onDismissCategoryPicker() },
        onNextClick = {
            onNavigateToIngredientInput(viewModel.createMenuDetailData())
        },
        modifier = modifier,
    )
}

@Composable
internal fun MenuDetailScreenContent(
    uiState: MenuDetailUiState,
    onNavigateBack: () -> Unit,
    onMenuNameChanged: (String) -> Unit,
    onPriceChanged: (String) -> Unit,
    onCategorySelected: (MenuCategory) -> Unit,
    onPreparationTimeClick: () -> Unit,
    showTimePicker: Boolean = false,
    onTimePickerDismiss: () -> Unit = {},
    onTimePickerConfirm: (Int, Int) -> Unit = { _, _ -> },
    onCategoryPickerClick: () -> Unit,
    onCategoryPickerDismiss: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isTemplateApplied) {
        if (uiState.isTemplateApplied) {
            snackbarHostState.showSnackbar(
                message = "템플릿이 적용됐어요",
            )
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Grayscale100,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                ChordToast(
                    text = it.visuals.message,
                    leadingIcon = R.drawable.ic_check,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            MenuDetailTopBar(
                onBackClick = onNavigateBack,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
            ) {
                StepIndicator(
                    currentStep = 1,
                    totalSteps = 2,
                    modifier = Modifier.padding(start = 20.dp, top = 16.dp),
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    FieldLabel(text = "메뉴명")
                    Spacer(modifier = Modifier.height(8.dp))
                    MenuNameTextField(
                        value = uiState.menuName,
                        onValueChange = onMenuNameChanged,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    FieldLabel(text = "가격")
                    Spacer(modifier = Modifier.height(8.dp))
                    PriceTextField(
                        value = uiState.price,
                        onValueChange = onPriceChanged,
                        placeholder = "메뉴 가격 입력",
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (uiState.price.isNotBlank()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Grayscale200)
                                .clickable(onClick = onCategoryPickerClick)
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                        ) {
                            Column {
                                Text(
                                    text = "카테고리",
                                    fontFamily = PretendardFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = Grayscale700,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = uiState.selectedCategory.displayName,
                                        fontFamily = PretendardFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 16.sp,
                                        color = Grayscale900,
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        painter = painterResource(R.drawable.ic_chevron_right),
                                        contentDescription = "카테고리 선택",
                                        modifier = Modifier.size(16.dp),
                                        tint = Grayscale500,
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Grayscale200)
                                .clickable(onClick = onPreparationTimeClick)
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                        ) {
                            Column {
                                Text(
                                    text = "제조시간",
                                    fontFamily = PretendardFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = Grayscale700,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${uiState.preparationMinutes}분 ${uiState.preparationSeconds}초",
                                        fontFamily = PretendardFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 16.sp,
                                        color = Grayscale900,
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        painter = painterResource(R.drawable.ic_chevron_right),
                                        contentDescription = "시간 선택",
                                        modifier = Modifier.size(16.dp),
                                        tint = Grayscale500,
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            ChordLargeButton(
                text = "메뉴 등록",
                onClick = onNextClick,
                enabled = uiState.isNextEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            )
        }
    }

    if (showTimePicker) {
        TimePickerBottomSheet(
            initialMinutes = uiState.preparationMinutes,
            initialSeconds = uiState.preparationSeconds,
            onDismiss = onTimePickerDismiss,
            onConfirm = onTimePickerConfirm,
        )
    }

    if (uiState.showCategoryPicker) {
        CategoryPickerBottomSheet(
            selectedCategory = uiState.selectedCategory,
            onDismiss = onCategoryPickerDismiss,
            onConfirm = { category ->
                onCategorySelected(category)
                onCategoryPickerDismiss()
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerBottomSheet(
    initialMinutes: Int,
    initialSeconds: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedMinutes by remember { mutableIntStateOf(initialMinutes) }
    var selectedSeconds by remember { mutableIntStateOf(initialSeconds) }

    ChordBottomSheet(
        onDismissRequest = onDismiss,
        title = "제조 시간",
        modifier = modifier,
        content = {
            ChordTimeWheelPicker(
                selectedMinutes = selectedMinutes,
                selectedSeconds = selectedSeconds,
                onTimeSelected = { minutes, seconds ->
                    selectedMinutes = minutes
                    selectedSeconds = seconds
                },
            )
        },
        confirmButton = {
            ChordLargeButton(
                text = "완료",
                onClick = { onConfirm(selectedMinutes, selectedSeconds) },
            )
        },
    )
}

@Composable
private fun MenuDetailTopBar(
    onBackClick: () -> Unit,
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
    }
}

@Composable
private fun MenuNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
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
                                text = "메뉴명 입력",
                                style = TextStyle(
                                    fontFamily = PretendardFontFamily,
                                    fontWeight = FontWeight.Normal,
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
                        .size(16.dp)
                        .clickable { onValueChange("") },
                    tint = Grayscale500,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Grayscale300, thickness = 1.dp)
    }
}

@Composable
private fun PriceTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    val numberFormat = remember { NumberFormat.getNumberInstance(Locale.KOREA) }
    val formattedValue = value.toLongOrNull()?.let { numberFormat.format(it) } ?: value
    val displayValue = if (value.isEmpty()) "" else "${formattedValue} 원"

    Column(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = displayValue,
            onValueChange = { newValue ->
                val numericValue = newValue.filter { it.isDigit() }
                onValueChange(numericValue)
            },
            textStyle = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = Grayscale900,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = SolidColor(Grayscale800),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = TextStyle(
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                color = PrimaryBlue500,
                            ),
                        )
                    }
                    innerTextField()
                }
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            color = if (value.isEmpty()) PrimaryBlue500 else Grayscale300,
            thickness = 1.dp,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryPickerBottomSheet(
    selectedCategory: MenuCategory,
    onDismiss: () -> Unit,
    onConfirm: (MenuCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    var tempSelection by remember { mutableStateOf(selectedCategory) }

    ChordBottomSheet(
        onDismissRequest = onDismiss,
        title = "메뉴 카테고리",
        modifier = modifier,
        content = {
            Column {
                MenuCategory.entries.forEachIndexed { index, category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { tempSelection = category }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = category.displayName,
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = if (category == tempSelection) PrimaryBlue500 else Grayscale900,
                        )
                        if (category == tempSelection) {
                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = "선택됨",
                                modifier = Modifier.size(20.dp),
                                tint = PrimaryBlue500,
                            )
                        }
                    }
                    if (index < MenuCategory.entries.lastIndex) {
                        HorizontalDivider(color = Grayscale200, thickness = 1.dp)
                    }
                }
            }
        },
        confirmButton = {
            ChordLargeButton(
                text = "확인",
                onClick = { onConfirm(tempSelection) },
            )
        },
    )
}

@Composable
private fun FieldLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = Grayscale900,
        modifier = modifier,
    )
}
