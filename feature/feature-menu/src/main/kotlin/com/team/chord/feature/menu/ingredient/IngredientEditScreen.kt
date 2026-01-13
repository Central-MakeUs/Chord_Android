package com.team.chord.feature.menu.ingredient

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.domain.model.menu.MenuIngredient
import com.team.chord.core.ui.component.ChordCheckboxItem
import com.team.chord.core.ui.component.ChordOneButtonDialog
import com.team.chord.core.ui.component.ChordOutlinedButton
import com.team.chord.core.ui.component.ChordTopAppBar
import com.team.chord.core.ui.component.ChordTwoButtonDialog
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.core.ui.theme.StatusDanger
import com.team.chord.feature.menu.ingredient.component.AddIngredientBottomSheet
import com.team.chord.feature.menu.ingredient.component.EditIngredientBottomSheet
import java.text.NumberFormat
import java.util.Locale

@Composable
fun IngredientEditScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IngredientEditViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    IngredientEditScreenContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onToggleDeleteMode = viewModel::toggleDeleteMode,
        onToggleSelection = viewModel::toggleIngredientSelection,
        onDeleteSelected = viewModel::deleteSelectedIngredients,
        onIngredientClick = viewModel::showEditBottomSheet,
        onHideEditBottomSheet = viewModel::hideEditBottomSheet,
        onUpdateIngredient = viewModel::updateIngredient,
        onShowAddBottomSheet = viewModel::showAddBottomSheet,
        onHideAddBottomSheet = viewModel::hideAddBottomSheet,
        onAddIngredient = viewModel::addIngredient,
        modifier = modifier,
    )
}

@Composable
internal fun IngredientEditScreenContent(
    uiState: IngredientEditUiState,
    onNavigateBack: () -> Unit,
    onToggleDeleteMode: () -> Unit,
    onToggleSelection: (Long) -> Unit,
    onDeleteSelected: () -> Unit,
    onIngredientClick: (MenuIngredient) -> Unit,
    onHideEditBottomSheet: () -> Unit,
    onUpdateIngredient: (Long, String, Int, Double, com.team.chord.core.domain.model.menu.IngredientUnit) -> Unit,
    onShowAddBottomSheet: () -> Unit,
    onHideAddBottomSheet: () -> Unit,
    onAddIngredient: (String, Int, Double, com.team.chord.core.domain.model.menu.IngredientUnit) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showDeleteSuccessDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        ChordTopAppBar(
            title = uiState.menuName,
            onBackClick = onNavigateBack,
            titleStyle = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Grayscale900
            ),
            actionContent = {
                when {
                    uiState.isDeleteMode && uiState.selectedIngredientIds.isNotEmpty() -> {
                        Text(
                            text = "삭제",
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = StatusDanger,
                        )
                    }
                    uiState.isDeleteMode -> {
                        Text(
                            text = "취소",
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Grayscale600,
                        )
                    }
                    else -> {
                        Text(
                            text = "삭제",
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = Grayscale700,
                        )
                    }
                }
            },
            onActionClick = {
                if (uiState.isDeleteMode && uiState.selectedIngredientIds.isNotEmpty()) {
                    showDeleteConfirmDialog = true
                } else {
                    onToggleDeleteMode()
                }
            },
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = PrimaryBlue500)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                ) {
                    items(
                        items = uiState.ingredients,
                        key = { it.id },
                    ) { ingredient ->
                        if (uiState.isDeleteMode) {
                            ChordCheckboxItem(
                                checked = ingredient.id in uiState.selectedIngredientIds,
                                onCheckedChange = { onToggleSelection(ingredient.id) },
                            ) {
                                IngredientItemContent(ingredient = ingredient)
                            }
                        } else {
                            IngredientItem(
                                ingredient = ingredient,
                                onClick = { onIngredientClick(ingredient) },
                            )
                        }
                        HorizontalDivider(color = Grayscale300)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ChordOutlinedButton(
                    text = "재료 추가",
                    onClick = onShowAddBottomSheet,
                    modifier = Modifier.padding(bottom = 24.dp),
                )
            }
        }
    }

    // Bottom Sheets
    if (uiState.showEditBottomSheet && uiState.editingIngredient != null) {
        EditIngredientBottomSheet(
            ingredient = uiState.editingIngredient,
            onDismiss = onHideEditBottomSheet,
            onConfirm = { name, price, quantity, unit ->
                onUpdateIngredient(uiState.editingIngredient.id, name, price, quantity, unit)
            },
        )
    }

    if (uiState.showAddBottomSheet) {
        AddIngredientBottomSheet(
            onDismiss = onHideAddBottomSheet,
            onConfirm = onAddIngredient,
        )
    }

    // Delete Dialogs
    if (showDeleteConfirmDialog) {
        ChordTwoButtonDialog(
            title = "선택하신 재료를 삭제하시겠어요?",
            onDismiss = { showDeleteConfirmDialog = false },
            onConfirm = {
                showDeleteConfirmDialog = false
                onDeleteSelected()
                showDeleteSuccessDialog = true
            },
            dismissText = "아니요",
            confirmText = "삭제하기",
        )
    }

    if (showDeleteSuccessDialog) {
        ChordOneButtonDialog(
            title = "재료가 삭제됐어요",
            onConfirm = {
                showDeleteSuccessDialog = false
                onToggleDeleteMode()
            },
            confirmText = "확인",
        )
    }
}

@Composable
private fun IngredientItem(
    ingredient: MenuIngredient,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IngredientItemContent(ingredient = ingredient)
    }
}

@Composable
private fun IngredientItemContent(
    ingredient: MenuIngredient,
    modifier: Modifier = Modifier,
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = ingredient.name,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            color = Grayscale900,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = formatQuantity(ingredient.quantity, ingredient.unit.displayName),
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Grayscale600,
            )
            Text(
                text = "${numberFormat.format(ingredient.totalPrice)}원",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Grayscale600,
            )
        }
    }
}

private fun formatQuantity(quantity: Double, unit: String): String {
    return if (quantity == quantity.toLong().toDouble()) {
        "${quantity.toLong()}$unit"
    } else {
        "$quantity$unit"
    }
}
