package com.team.chord.feature.menu.add.ingredientinput

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.ui.R
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordToast
import com.team.chord.core.ui.component.ChordTwoButtonDialog
import com.team.chord.core.ui.component.ChordTooltipBubble
import com.team.chord.core.ui.component.IngredientEditorBottomSheet
import com.team.chord.core.ui.component.IngredientEditorCategoryOption
import com.team.chord.core.ui.component.TooltipDirection
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale800
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.menu.add.component.StepIndicator
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
        onEditIngredient = viewModel::onEditIngredient,
        onEnterDeleteMode = viewModel::enterDeleteMode,
        onCancelDeleteMode = viewModel::cancelDeleteMode,
        onToggleIngredientSelectionForDeletion = viewModel::toggleIngredientSelectionForDeletion,
        onShowDeleteConfirmDialog = viewModel::showDeleteConfirmDialog,
        onDismissDeleteConfirmDialog = viewModel::dismissDeleteConfirmDialog,
        onConfirmDeleteSelectedIngredients = viewModel::confirmDeleteSelectedIngredients,
        onBottomSheetDismissed = viewModel::onBottomSheetDismissed,
        onBottomSheetCategoryChanged = viewModel::onBottomSheetCategoryChanged,
        onBottomSheetPriceChanged = viewModel::onBottomSheetPriceChanged,
        onBottomSheetPurchaseAmountChanged = viewModel::onBottomSheetPurchaseAmountChanged,
        onBottomSheetAmountChanged = viewModel::onBottomSheetAmountChanged,
        onBottomSheetUnitChanged = viewModel::onBottomSheetUnitChanged,
        onBottomSheetSupplierChanged = viewModel::onBottomSheetSupplierChanged,
        onConfirmIngredient = viewModel::onConfirmIngredient,
        onToastDismissed = viewModel::onToastDismissed,
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
    onEditIngredient: (SelectedIngredient) -> Unit,
    onEnterDeleteMode: () -> Unit,
    onCancelDeleteMode: () -> Unit,
    onToggleIngredientSelectionForDeletion: (Long) -> Unit,
    onShowDeleteConfirmDialog: () -> Unit,
    onDismissDeleteConfirmDialog: () -> Unit,
    onConfirmDeleteSelectedIngredients: () -> Unit,
    onBottomSheetDismissed: () -> Unit,
    onBottomSheetCategoryChanged: (String) -> Unit,
    onBottomSheetPriceChanged: (String) -> Unit,
    onBottomSheetPurchaseAmountChanged: (String) -> Unit,
    onBottomSheetAmountChanged: (String) -> Unit,
    onBottomSheetUnitChanged: (IngredientUnit) -> Unit,
    onBottomSheetSupplierChanged: (String) -> Unit,
    onConfirmIngredient: () -> Unit,
    onToastDismissed: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.showCompletionToast) {
        if (uiState.showCompletionToast) {
            snackbarHostState.showSnackbar(uiState.completionToastMessage)
            onToastDismissed()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Grayscale100,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
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
                .padding(paddingValues)
        ) {
            IngredientInputTopBar(
                onBackClick = onNavigateBack,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
            ) {
                StepIndicator(
                    currentStep = 2,
                    totalSteps = 2,
                    modifier = Modifier.padding(start = 24.dp, top = 8.dp),
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    FieldLabel(text = "재료명")
                    Spacer(modifier = Modifier.height(8.dp))
                    IngredientSearchField(
                        value = uiState.searchQuery,
                        onValueChange = onSearchQueryChanged,
                        onAddClick = onAddNewIngredient,
                        placeholder = "추가할 재료명을 입력해주세요",
                    )

                    if (uiState.searchQuery.isEmpty() && uiState.selectedIngredients.isEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            ChordTooltipBubble(
                                text = "필요한 재료가 더 있으신가요?\n직접 입력에서 추가해보세요",
                                direction = TooltipDirection.UpRight,
                            )
                        }
                    }

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

                if (uiState.selectedIngredients.isNotEmpty()) {
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            FieldLabel(text = "재료 리스트")
                            Text(
                                text = if (uiState.isDeleteMode) "취소" else "삭제",
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Grayscale500,
                                modifier = Modifier.clickable {
                                    if (uiState.isDeleteMode) {
                                        onCancelDeleteMode()
                                    } else {
                                        onEnterDeleteMode()
                                    }
                                },
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        uiState.selectedIngredients.forEach { ingredient ->
                            if (uiState.isDeleteMode) {
                                IngredientSelectableListItem(
                                    ingredient = ingredient,
                                    checked = ingredient.id in uiState.selectedIngredientIdsForDeletion,
                                    onCheckedChange = {
                                        onToggleIngredientSelectionForDeletion(ingredient.id)
                                    },
                                )
                            } else {
                                IngredientListItem(
                                    ingredient = ingredient,
                                    onClick = { onEditIngredient(ingredient) },
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            BottomNavigationButtons(
                primaryButtonText = if (uiState.isDeleteMode) "삭제하기" else "재료 추가 완료",
                isPrimaryEnabled = if (uiState.isDeleteMode) {
                    uiState.deleteSelectionCount > 0
                } else {
                    uiState.isNextEnabled
                },
                showPreviousButton = !uiState.isTemplateApplied,
                onPreviousClick = onPreviousClick,
                onPrimaryClick = {
                    if (uiState.isDeleteMode) {
                        onShowDeleteConfirmDialog()
                    } else {
                        onNextClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp),
            )
        }

        if (uiState.showBottomSheet && uiState.bottomSheetIngredient != null) {
            IngredientEditorBottomSheet(
                title = uiState.bottomSheetIngredient.name,
                categoryCode = uiState.bottomSheetIngredient.categoryCode,
                categoryOptions = ingredientCategoryOptions,
                onCategoryChanged = onBottomSheetCategoryChanged,
                price = uiState.bottomSheetIngredient.price,
                onPriceChanged = onBottomSheetPriceChanged,
                pricePlaceholder = uiState.bottomSheetIngredient.suggestedPrice?.let(::formatPricePlaceholder)
                    ?: "구매한 가격 입력",
                purchaseAmount = uiState.bottomSheetIngredient.purchaseAmount,
                onPurchaseAmountChanged = onBottomSheetPurchaseAmountChanged,
                purchaseAmountPlaceholder = uiState.bottomSheetIngredient.suggestedPurchaseAmount?.toString()
                    ?: "구매한 용량 입력",
                amount = uiState.bottomSheetIngredient.amount,
                onAmountChanged = onBottomSheetAmountChanged,
                amountPlaceholder = uiState.bottomSheetIngredient.suggestedAmount?.toString()
                    ?: "제조시 사용되는 용량 입력",
                unit = uiState.bottomSheetIngredient.unit,
                onUnitChanged = onBottomSheetUnitChanged,
                supplier = uiState.bottomSheetIngredient.supplier,
                onSupplierChanged = onBottomSheetSupplierChanged,
                confirmText = uiState.bottomSheetIngredient.confirmButtonText,
                confirmEnabled = uiState.bottomSheetIngredient.isAddEnabled,
                onDismiss = onBottomSheetDismissed,
                onConfirm = onConfirmIngredient,
                isCategoryEditable = uiState.bottomSheetIngredient.isCategoryEditable,
                isPriceEditable = uiState.bottomSheetIngredient.isPriceEditable,
                isPurchaseAmountEditable = uiState.bottomSheetIngredient.isPurchaseAmountEditable,
                isUnitEditable = uiState.bottomSheetIngredient.isUnitEditable,
                isSupplierEditable = uiState.bottomSheetIngredient.isSupplierEditable,
            )
        }

        if (uiState.showDeleteConfirmDialog) {
            ChordTwoButtonDialog(
                title = "선택한 재료를 삭제할까요?",
                onDismiss = onDismissDeleteConfirmDialog,
                onConfirm = onConfirmDeleteSelectedIngredients,
                dismissText = "취소하기",
                confirmText = "삭제하기",
            )
        }
    }
}

@Composable
private fun IngredientInputTopBar(
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
private fun IngredientSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onAddClick: () -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    val isAddEnabled = value.isNotBlank()

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Grayscale200)
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
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
                                    fontSize = 16.sp,
                                    color = Grayscale500,
                                ),
                            )
                        }
                        innerTextField()
                    }
                },
            )
        }

        Text(
            text = "추가",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = if (isAddEnabled) Grayscale700 else Grayscale400,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(
                    enabled = isAddEnabled,
                    onClick = onAddClick,
                )
                .padding(horizontal = 8.dp, vertical = 10.dp),
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
                fontWeight = if (suggestion.sourceType == IngredientSourceType.SAVED) FontWeight.Bold else FontWeight.Medium,
                fontSize = 16.sp,
                color = Grayscale900,
            )
            when (suggestion.sourceType) {
                IngredientSourceType.SAVED -> Unit
                IngredientSourceType.TEMPLATE -> {
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

                IngredientSourceType.NEW -> Unit
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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

@Composable
private fun IngredientSelectableListItem(
    ingredient: SelectedIngredient,
    checked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onCheckedChange),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(
                id = if (checked) R.drawable.ic_checkbox else R.drawable.ic_un_checkbox,
            ),
            contentDescription = if (checked) "선택됨" else "선택 안됨",
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
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
}

@Composable
private fun BottomNavigationButtons(
    primaryButtonText: String,
    isPrimaryEnabled: Boolean,
    showPreviousButton: Boolean,
    onPreviousClick: () -> Unit,
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (showPreviousButton) {
            ChordLargeButton(
                text = "이전",
                onClick = onPreviousClick,
                modifier = Modifier.weight(1f),
                backgroundColor = PrimaryBlue500,
                textColor = Grayscale100,
            )
        }

        ChordLargeButton(
            text = primaryButtonText,
            onClick = onPrimaryClick,
            modifier = Modifier.weight(if (showPreviousButton) 2f else 1f),
            enabled = isPrimaryEnabled,
        )
    }
}

private val ingredientCategoryOptions = listOf(
    IngredientEditorCategoryOption(code = "INGREDIENTS", label = "식재료"),
    IngredientEditorCategoryOption(code = "MATERIALS", label = "운영 재료"),
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

private fun formatPricePlaceholder(price: Int): String =
    "${NumberFormat.getNumberInstance(Locale.KOREA).format(price)}원"
