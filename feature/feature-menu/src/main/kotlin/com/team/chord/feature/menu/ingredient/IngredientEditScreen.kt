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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.domain.model.menu.MenuIngredient
import com.team.chord.core.ui.component.ChordCheckboxItem
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale600
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

@OptIn(ExperimentalMaterial3Api::class)
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        TopAppBar(
            title = {
                Text(
                    text = uiState.menuName,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Grayscale900,
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "뒤로 가기",
                        tint = Grayscale900,
                    )
                }
            },
            actions = {
                if (uiState.isDeleteMode && uiState.selectedIngredientIds.isNotEmpty()) {
                    TextButton(onClick = onDeleteSelected) {
                        Text(
                            text = "삭제",
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = StatusDanger,
                        )
                    }
                } else {
                    TextButton(onClick = onToggleDeleteMode) {
                        Text(
                            text = if (uiState.isDeleteMode) "취소" else "삭제",
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = if (uiState.isDeleteMode) Grayscale600 else Grayscale500,
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Grayscale100,
            ),
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

                OutlinedButton(
                    onClick = onShowAddBottomSheet,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(
                        text = "재료 추가",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Grayscale600,
                    )
                }
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

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = ingredient.name,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Grayscale900,
            )
            Text(
                text = formatQuantity(ingredient.quantity, ingredient.unit.displayName),
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Grayscale600,
            )
        }
        Text(
            text = "${numberFormat.format(ingredient.totalPrice)}원",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Grayscale900,
        )
    }
}

private fun formatQuantity(quantity: Double, unit: String): String {
    return if (quantity == quantity.toLong().toDouble()) {
        "${quantity.toLong()}$unit"
    } else {
        "$quantity$unit"
    }
}
