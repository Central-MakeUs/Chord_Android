package com.team.chord.feature.ingredient.detail

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.domain.model.ingredient.IngredientFilter
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.ui.component.ChordOutlinedButton
import com.team.chord.core.ui.component.ChordTwoButtonDialog
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue100
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.ingredient.component.IngredientEditBottomSheet
import com.team.chord.feature.ingredient.component.PriceHistoryItem
import com.team.chord.feature.ingredient.component.SupplierEditBottomSheet
import com.team.chord.feature.ingredient.component.UsedMenuCard
import java.text.NumberFormat
import java.util.Locale
import com.team.chord.core.ui.R as CoreUiR

@Composable
fun IngredientDetailScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IngredientDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle deletion completion
    LaunchedEffect(uiState) {
        if (uiState is IngredientDetailUiState.Success) {
            val detail = (uiState as IngredientDetailUiState.Success).ingredientDetail
            if (detail.isDeleted) {
                onNavigateBack()
            }
        }
    }

    IngredientDetailScreenContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onFavoriteToggle = viewModel::onFavoriteToggle,
        onDelete = viewModel::onDelete,
        onUpdatePriceInfo = viewModel::onUpdatePriceInfo,
        onUpdateSupplier = viewModel::onUpdateSupplier,
        modifier = modifier,
    )
}

@Composable
internal fun IngredientDetailScreenContent(
    uiState: IngredientDetailUiState,
    onNavigateBack: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onDelete: () -> Unit,
    onUpdatePriceInfo: (IngredientFilter, Int, Int, IngredientUnit) -> Unit,
    onUpdateSupplier: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showPriceEditSheet by remember { mutableStateOf(false) }
    var showSupplierEditSheet by remember { mutableStateOf(false) }

    // Edit form states
    var editPrice by remember { mutableStateOf("") }
    var editAmount by remember { mutableStateOf("") }
    var editUnit by remember { mutableStateOf(IngredientUnit.G) }
    var editFilter by remember { mutableStateOf(IngredientFilter.FOOD_INGREDIENT) }
    var editSupplier by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        when (uiState) {
            is IngredientDetailUiState.Loading -> {
                IngredientDetailHeader(
                    isFavorite = false,
                    onNavigateBack = onNavigateBack,
                    onFavoriteToggle = {},
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = PrimaryBlue500)
                }
            }

            is IngredientDetailUiState.Error -> {
                IngredientDetailHeader(
                    isFavorite = false,
                    onNavigateBack = onNavigateBack,
                    onFavoriteToggle = {},
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = uiState.message,
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = Grayscale600,
                    )
                }
            }

            is IngredientDetailUiState.Success -> {
                IngredientDetailHeader(
                    isFavorite = uiState.ingredientDetail.isFavorite,
                    onNavigateBack = onNavigateBack,
                    onFavoriteToggle = onFavoriteToggle,
                )
                IngredientDetailContent(
                    ingredientDetail = uiState.ingredientDetail,
                    onPriceEditClick = {
                        // Initialize edit states from current values
                        editPrice = uiState.ingredientDetail.price.toString()
                        editAmount = uiState.ingredientDetail.unitAmount.toString()
                        editUnit = uiState.ingredientDetail.unit
                        editFilter = uiState.ingredientDetail.category
                        showPriceEditSheet = true
                    },
                    onSupplierEditClick = {
                        // Initialize edit state from current value
                        editSupplier = uiState.ingredientDetail.supplier
                        showSupplierEditSheet = true
                    },
                    onDeleteClick = { showDeleteDialog = true },
                )
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        ChordTwoButtonDialog(
            title = "재료를 삭제할까요?",
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            },
            dismissText = "취소하기",
            confirmText = "삭제하기",
        )
    }

    // Price edit BottomSheet
    if (showPriceEditSheet && uiState is IngredientDetailUiState.Success) {
        IngredientEditBottomSheet(
            ingredientName = uiState.ingredientDetail.name,
            selectedFilter = editFilter,
            price = editPrice,
            amount = editAmount,
            selectedUnit = editUnit,
            onFilterSelect = { editFilter = it },
            onPriceChange = { editPrice = it },
            onAmountChange = { editAmount = it },
            onUnitSelect = { editUnit = it },
            onConfirm = {
                val priceInt = editPrice.replace(",", "").toIntOrNull() ?: 0
                val amountInt = editAmount.toIntOrNull() ?: 0
                onUpdatePriceInfo(editFilter, priceInt, amountInt, editUnit)
                showPriceEditSheet = false
            },
            onDismiss = { showPriceEditSheet = false },
        )
    }

    // Supplier edit BottomSheet
    if (showSupplierEditSheet && uiState is IngredientDetailUiState.Success) {
        SupplierEditBottomSheet(
            supplierName = editSupplier,
            onSupplierNameChange = { editSupplier = it },
            onClear = { editSupplier = "" },
            onConfirm = {
                onUpdateSupplier(editSupplier)
                showSupplierEditSheet = false
            },
            onDismiss = { showSupplierEditSheet = false },
        )
    }
}

@Composable
private fun IngredientDetailHeader(
    isFavorite: Boolean,
    onNavigateBack: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Grayscale100)
            .padding(horizontal = 20.dp),
    ) {
        // Back button (left)
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(24.dp)
                .clickable { onNavigateBack() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(CoreUiR.drawable.ic_chevron_left),
                contentDescription = "뒤로가기",
                tint = Grayscale900,
            )
        }

        // Favorite star (right)
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(24.dp)
                .clickable { onFavoriteToggle() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(
                    if (isFavorite) CoreUiR.drawable.ic_star_filled else CoreUiR.drawable.ic_star_outline
                ),
                contentDescription = if (isFavorite) "즐겨찾기 해제" else "즐겨찾기",
                tint = if (isFavorite) PrimaryBlue500 else Grayscale400,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IngredientDetailContent(
    ingredientDetail: IngredientDetailUi,
    onPriceEditClick: () -> Unit,
    onSupplierEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Ingredient info box with category, name, price, and supplier
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Grayscale100,
                    shape = RoundedCornerShape(12.dp),
                )
                .border(
                    width = 1.dp,
                    color = Grayscale300,
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(16.dp),
        ) {
            // Category tag
            Box(
                modifier = Modifier
                    .background(
                        color = PrimaryBlue100,
                        shape = RoundedCornerShape(6.dp),
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = ingredientDetail.category.displayName,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = PrimaryBlue500,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Ingredient name
            Text(
                text = ingredientDetail.name,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Grayscale600,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Price row with arrow
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPriceEditClick() },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${numberFormat.format(ingredientDetail.price)}원 ${ingredientDetail.unitAmount}${ingredientDetail.unit.displayName}",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp,
                    color = Grayscale900,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(CoreUiR.drawable.ic_edit),
                    contentDescription = "가격 수정",
                    modifier = Modifier.size(20.dp),
                    tint = Grayscale600,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Supplier row (inside the same card)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSupplierEditClick() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "공급업체",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Grayscale600,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = ingredientDetail.supplier,
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Grayscale900,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(CoreUiR.drawable.ic_chevron_right),
                        contentDescription = "공급업체 수정",
                        modifier = Modifier.size(16.dp),
                        tint = Grayscale600,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Used menus section
        Text(
            text = buildAnnotatedString {
                append("메뉴 ")
                withStyle(SpanStyle(color = PrimaryBlue500)) {
                    append("${ingredientDetail.usedMenus.size}")
                }
                append("개에")
            },
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Grayscale900,
        )
        Text(
            text = "사용되고 있어요",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Grayscale900,
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Used menu cards
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ingredientDetail.usedMenus.forEach { menu ->
                UsedMenuCard(
                    menuName = menu.name,
                    usageAmount = menu.usageAmount,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Divider
        HorizontalDivider(
            color = Grayscale200,
            thickness = 1.dp,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Price history section
        Text(
            text = "변동 이력",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Grayscale900,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Price history items
        ingredientDetail.priceHistory.forEachIndexed { index, history ->
            PriceHistoryItem(
                date = history.date,
                price = history.price,
                unitAmount = history.unitAmount,
                unitDisplayName = history.unitDisplayName,
                isFirst = index == 0,
                isLast = index == ingredientDetail.priceHistory.lastIndex,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Delete button
        ChordOutlinedButton(
            text = "재료 삭제",
            onClick = onDeleteClick,
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun IngredientDetailScreenPreview() {
    IngredientDetailScreenContent(
        uiState = IngredientDetailUiState.Success(
            ingredientDetail = IngredientDetailUi(
                id = 1L,
                name = "원두",
                category = IngredientFilter.FOOD_INGREDIENT,
                price = 5000,
                unitAmount = 100,
                unit = IngredientUnit.G,
                supplier = "쿠팡",
                isFavorite = false,
                usedMenus = listOf(
                    UsedMenuUi(1L, "아메리카노", "10g"),
                    UsedMenuUi(2L, "카페라떼", "10g"),
                    UsedMenuUi(3L, "돌체라떼", "10g"),
                    UsedMenuUi(4L, "아인슈페너", "10g"),
                ),
                priceHistory = listOf(
                    PriceHistoryUi(1L, "25.11.12", 5000, 100, "g"),
                    PriceHistoryUi(2L, "25.11.09", 5000, 100, "g"),
                    PriceHistoryUi(3L, "25.10.11", 5000, 100, "g"),
                    PriceHistoryUi(4L, "25.09.08", 4800, 100, "g"),
                ),
            ),
        ),
        onNavigateBack = {},
        onFavoriteToggle = {},
        onDelete = {},
        onUpdatePriceInfo = { _, _, _, _ -> },
        onUpdateSupplier = {},
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun IngredientDetailScreenLoadingPreview() {
    IngredientDetailScreenContent(
        uiState = IngredientDetailUiState.Loading,
        onNavigateBack = {},
        onFavoriteToggle = {},
        onDelete = {},
        onUpdatePriceInfo = { _, _, _, _ -> },
        onUpdateSupplier = {},
    )
}
