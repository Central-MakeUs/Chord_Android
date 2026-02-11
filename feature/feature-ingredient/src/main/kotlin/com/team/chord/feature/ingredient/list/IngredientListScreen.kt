package com.team.chord.feature.ingredient.list

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.domain.model.ingredient.IngredientFilter
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.ui.component.ChordBottomSheet
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTextField
import com.team.chord.core.ui.component.ChordToast
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.ingredient.component.IngredientAddBottomSheet
import com.team.chord.feature.ingredient.component.IngredientFilterChip
import com.team.chord.feature.ingredient.component.IngredientListItem
import com.team.chord.core.ui.R as CoreUiR

@Composable
fun IngredientListScreen(
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IngredientListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.refresh()
        onPauseOrDispose { }
    }

    IngredientListScreenContent(
        uiState = uiState,
        onFilterToggle = viewModel::onFilterToggle,
        onFilterRemove = viewModel::onFilterRemove,
        onNavigateToDetail = onNavigateToDetail,
        onSearchClick = onNavigateToSearch,
        onMoreClick = viewModel::onMoreClick,
        onDismissMoreMenu = viewModel::onDismissMoreMenu,
        onRefresh = viewModel::refresh,
        onAddClick = viewModel::onAddClick,
        onAddNameChange = viewModel::onAddNameChange,
        onAddNameConfirm = viewModel::onAddNameConfirm,
        onDismissAddNameSheet = viewModel::onDismissAddNameSheet,
        onDismissAddDetailSheet = viewModel::onDismissAddDetailSheet,
        onAddIngredient = viewModel::onAddIngredient,
        enterDeleteMode = viewModel::enterDeleteMode,
        exitDeleteMode = viewModel::exitDeleteMode,
        toggleSelection = viewModel::toggleSelection,
        deleteSelected = viewModel::deleteSelected,
        onToastDismissed = viewModel::onToastDismissed,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun IngredientListScreenContent(
    uiState: IngredientListUiState,
    onFilterToggle: (IngredientFilter) -> Unit,
    onFilterRemove: (IngredientFilter) -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onSearchClick: () -> Unit,
    onMoreClick: () -> Unit,
    onDismissMoreMenu: () -> Unit,
    onRefresh: () -> Unit,
    onAddClick: () -> Unit,
    onAddNameChange: (String) -> Unit,
    onAddNameConfirm: () -> Unit,
    onDismissAddNameSheet: () -> Unit,
    onDismissAddDetailSheet: () -> Unit,
    onAddIngredient: (String, String, Int, Int, String?) -> Unit,
    enterDeleteMode: () -> Unit,
    exitDeleteMode: () -> Unit,
    toggleSelection: (Long) -> Unit,
    deleteSelected: () -> Unit,
    onToastDismissed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var addSelectedFilter by remember { mutableStateOf(IngredientFilter.FOOD_INGREDIENT) }
    var addPrice by remember { mutableStateOf("") }
    var addAmount by remember { mutableStateOf("") }
    var addSelectedUnit by remember { mutableStateOf(IngredientUnit.G) }
    var addSupplier by remember { mutableStateOf("") }

    LaunchedEffect(uiState.showToast) {
        if (uiState.showToast) {
            snackbarHostState.showSnackbar(
                message = uiState.toastMessage,
                duration = SnackbarDuration.Short,
            )
            onToastDismissed()
        }
    }

    LaunchedEffect(uiState.showAddDetailSheet) {
        if (!uiState.showAddDetailSheet) {
            addSelectedFilter = IngredientFilter.FOOD_INGREDIENT
            addPrice = ""
            addAmount = ""
            addSelectedUnit = IngredientUnit.G
            addSupplier = ""
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                ChordToast(
                    text = data.visuals.message,
                    leadingIcon = CoreUiR.drawable.ic_check,
                )
            }
        },
        containerColor = Grayscale200,
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            if (uiState.isDeleteMode) {
                DeleteModeHeader(
                    onBackClick = exitDeleteMode,
                )
            } else {
                IngredientListHeader(
                    ingredientCount = uiState.ingredients.size,
                    onSearchClick = onSearchClick,
                    onMoreClick = onMoreClick,
                    showMoreMenu = uiState.showMoreMenu,
                    onDismissMoreMenu = onDismissMoreMenu,
                    onAddClick = onAddClick,
                    onDeleteClick = enterDeleteMode,
                )
            }

            if (!uiState.isDeleteMode) {
                Spacer(modifier = Modifier.height(12.dp))

                IngredientFilterChipRow(
                    activeFilters = uiState.activeFilters,
                    onFilterToggle = onFilterToggle,
                    onFilterRemove = onFilterRemove,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(
                        color = Grayscale100,
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    )
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp),
            ) {
                PullToRefreshBox(
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = onRefresh,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(color = PrimaryBlue500)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            itemsIndexed(
                                items = uiState.ingredients,
                                key = { _, item -> item.id },
                            ) { index, ingredient ->
                                IngredientListItem(
                                    name = ingredient.name,
                                    price = ingredient.price,
                                    usage = ingredient.usage,
                                    onClick = { onNavigateToDetail(ingredient.id) },
                                    isDeleteMode = uiState.isDeleteMode,
                                    isSelected = uiState.selectedIds.contains(ingredient.id),
                                    onCheckedChange = { toggleSelection(ingredient.id) },
                                )
                                if (index < uiState.ingredients.lastIndex) {
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = Grayscale300,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (uiState.isDeleteMode) {
                val selectedCount = uiState.selectedIds.size
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Grayscale100)
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                ) {
                    ChordLargeButton(
                        text = if (selectedCount == 0) "삭제" else "${selectedCount}개 삭제",
                        onClick = deleteSelected,
                        enabled = selectedCount > 0,
                    )
                }
            }
        }

        if (uiState.showAddNameSheet) {
            ChordBottomSheet(
                onDismissRequest = onDismissAddNameSheet,
                title = "추가하실 재료명을 입력해주세요",
                content = {
                    ChordTextField(
                        value = uiState.addIngredientName,
                        onValueChange = onAddNameChange,
                        placeholder = "재료명",
                    )
                },
                confirmButton = {
                    ChordLargeButton(
                        text = "확인",
                        onClick = onAddNameConfirm,
                        enabled = uiState.addIngredientName.isNotBlank(),
                    )
                },
            )
        }

        if (uiState.showAddDetailSheet) {
            IngredientAddBottomSheet(
                ingredientName = uiState.addIngredientName,
                selectedFilter = addSelectedFilter,
                price = addPrice,
                amount = addAmount,
                selectedUnit = addSelectedUnit,
                supplier = addSupplier,
                onFilterSelect = { addSelectedFilter = it },
                onPriceChange = { addPrice = it },
                onAmountChange = { addAmount = it },
                onUnitSelect = { addSelectedUnit = it },
                onSupplierChange = { addSupplier = it },
                onConfirm = {
                    onAddIngredient(
                        addSelectedFilter.categoryCode,
                        addSelectedUnit.name,
                        addPrice.toIntOrNull() ?: 0,
                        addAmount.toIntOrNull() ?: 0,
                        addSupplier.ifBlank { null },
                    )
                },
                onDismiss = onDismissAddDetailSheet,
            )
        }
    }
}

@Composable
private fun IngredientListHeader(
    ingredientCount: Int,
    onSearchClick: () -> Unit,
    onMoreClick: () -> Unit,
    showMoreMenu: Boolean,
    onDismissMoreMenu: () -> Unit,
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Grayscale200)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "재료 ",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Grayscale900,
            )
            Text(
                text = "$ingredientCount",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = PrimaryBlue500,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                painter = painterResource(id = CoreUiR.drawable.ic_search),
                contentDescription = "검색",
                tint = Grayscale600,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onSearchClick),
            )
            Box {
                Icon(
                    painter = painterResource(id = CoreUiR.drawable.ic_ellipsis),
                    contentDescription = "더보기",
                    tint = Grayscale900,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onMoreClick),
                )
                DropdownMenu(
                    expanded = showMoreMenu,
                    onDismissRequest = onDismissMoreMenu,
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "추가",
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                            )
                        },
                        onClick = onAddClick,
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "삭제",
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                            )
                        },
                        onClick = onDeleteClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun DeleteModeHeader(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Grayscale200)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = CoreUiR.drawable.ic_chevron_left),
            contentDescription = "뒤로가기",
            tint = Grayscale900,
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onBackClick),
        )
    }
}

@Composable
private fun IngredientFilterChipRow(
    activeFilters: Set<IngredientFilter>,
    onFilterToggle: (IngredientFilter) -> Unit,
    onFilterRemove: (IngredientFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(IngredientFilter.entries.toList()) { filter ->
            val isSelected = activeFilters.contains(filter)
            IngredientFilterChip(
                text = filter.displayName,
                isSelected = isSelected,
                onClick = { onFilterToggle(filter) },
                onRemove = if (isSelected) {
                    { onFilterRemove(filter) }
                } else {
                    null
                },
            )
        }
    }
}
