package com.team.chord.feature.ingredient.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.domain.model.ingredient.IngredientFilter
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.ui.component.ChordOutlinedButton
import com.team.chord.core.ui.component.ChordToast
import com.team.chord.core.ui.component.ChordTwoButtonDialog
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.ingredient.component.IngredientEditBottomSheet
import com.team.chord.feature.ingredient.component.PriceHistoryItem
import com.team.chord.feature.ingredient.component.SupplierEditBottomSheet
import com.team.chord.feature.ingredient.component.UsedMenuCard
import com.team.chord.feature.ingredient.formatIngredientPriceText
import com.team.chord.core.ui.R as CoreUiR

@Composable
fun IngredientDetailScreen(
    onNavigateBack: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IngredientDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is IngredientDetailUiState.Success) {
            val detail = (uiState as IngredientDetailUiState.Success).ingredientDetail
            if (detail.isDeleted) {
                onNavigateBack(true)
            }
        }
    }

    IngredientDetailScreenContent(
        uiState = uiState,
        onNavigateBack = { onNavigateBack(viewModel.hasChanges()) },
        onFavoriteToggle = viewModel::onFavoriteToggle,
        onDelete = viewModel::onDelete,
        onUpdatePriceInfo = viewModel::onUpdatePriceInfo,
        onUpdateSupplier = viewModel::onUpdateSupplier,
        onToastShown = viewModel::consumeToastMessage,
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
    onToastShown: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditSheet by remember { mutableStateOf(false) }
    var showSupplierSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    var editPrice by remember { mutableStateOf("") }
    var editAmount by remember { mutableStateOf("") }
    var editUnit by remember { mutableStateOf(IngredientUnit.G) }
    var editFilter by remember { mutableStateOf(IngredientFilter.FOOD_INGREDIENT) }
    var supplierName by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        val message = (uiState as? IngredientDetailUiState.Success)?.toastMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Short,
        )
        onToastShown()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Grayscale100,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                ChordToast(
                    text = data.visuals.message,
                    leadingIcon = CoreUiR.drawable.ic_check,
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Grayscale100)
                .padding(paddingValues),
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
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = Grayscale600,
                        )
                    }
                }

                is IngredientDetailUiState.Success -> {
                    val ingredientDetail = uiState.ingredientDetail

                    IngredientDetailHeader(
                        isFavorite = ingredientDetail.isFavorite,
                        onNavigateBack = onNavigateBack,
                        onFavoriteToggle = onFavoriteToggle,
                    )
                    IngredientDetailContent(
                        ingredientDetail = ingredientDetail,
                        onEditClick = {
                            editPrice = uiState.ingredientDetail.price.toString()
                            editAmount = uiState.ingredientDetail.unitAmount.toString()
                            editUnit = uiState.ingredientDetail.unit
                            editFilter = uiState.ingredientDetail.category
                            showEditSheet = true
                        },
                        onSupplierClick = {
                            supplierName = uiState.ingredientDetail.supplier
                            showSupplierSheet = true
                        },
                        onDeleteClick = { showDeleteDialog = true },
                    )
                }
            }
        }
    }

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

    if (showEditSheet && uiState is IngredientDetailUiState.Success) {
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
                showEditSheet = false
            },
            onDismiss = { showEditSheet = false },
        )
    }

    if (showSupplierSheet) {
        SupplierEditBottomSheet(
            supplierName = supplierName,
            onSupplierNameChange = { supplierName = it },
            onClear = { supplierName = "" },
            onConfirm = {
                onUpdateSupplier(supplierName)
                showSupplierSheet = false
            },
            onDismiss = { showSupplierSheet = false },
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
            .height(56.dp)
            .background(Grayscale100)
            .padding(horizontal = 20.dp),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(24.dp)
                .clickable(onClick = onNavigateBack),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(CoreUiR.drawable.ic_chevron_left),
                contentDescription = "뒤로가기",
                tint = Grayscale900,
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(32.dp)
                .clickable(onClick = onFavoriteToggle),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(
                    if (isFavorite) CoreUiR.drawable.ic_star_filled else CoreUiR.drawable.ic_star_outline,
                ),
                contentDescription = if (isFavorite) "즐겨찾기 해제" else "즐겨찾기",
                tint = if (isFavorite) PrimaryBlue500 else Grayscale400,
            )
        }
    }
}

@Composable
private fun IngredientDetailContent(
    ingredientDetail: IngredientDetailUi,
    onEditClick: () -> Unit,
    onSupplierClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        IngredientInfoCard(
            ingredientDetail = ingredientDetail,
            onEditClick = onEditClick,
            onSupplierClick = onSupplierClick,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(32.dp))

        UsedMenuSection(
            usedMenus = ingredientDetail.usedMenus,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(Grayscale200),
        )

        Spacer(modifier = Modifier.height(24.dp))

        PriceHistorySection(
            priceHistory = ingredientDetail.priceHistory,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        ChordOutlinedButton(
            text = "재료 삭제",
            onClick = onDeleteClick,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun IngredientInfoCard(
    ingredientDetail: IngredientDetailUi,
    onEditClick: () -> Unit,
    onSupplierClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(16.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PrimaryBlue100.copy(alpha = 0.5f),
                        Grayscale100,
                    ),
                ),
                shape = cardShape,
            )
            .border(
                width = 1.5.dp,
                color = Grayscale300,
                shape = cardShape,
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
        ) {
            Text(
                text = ingredientDetail.category.displayName,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Grayscale500,
            )

            Text(
                text = ingredientDetail.name,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Grayscale900,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.clickable(onClick = onEditClick),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = formatIngredientPriceText(
                        price = ingredientDetail.price,
                        unitAmount = ingredientDetail.unitAmount,
                        unit = ingredientDetail.unit,
                    ),
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    color = Grayscale900,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(CoreUiR.drawable.ic_edit),
                    contentDescription = "재료 정보 수정",
                    modifier = Modifier.size(20.dp),
                    tint = Grayscale500,
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp),
            thickness = 1.dp,
            color = Grayscale300,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onSupplierClick)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "공급업체",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Grayscale500,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = ingredientDetail.supplier.ifBlank { "-" },
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Grayscale700,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                painter = painterResource(CoreUiR.drawable.ic_chevron_right),
                contentDescription = "공급업체 수정",
                modifier = Modifier.size(16.dp),
                tint = Grayscale500,
            )
        }
    }
}

@Composable
private fun UsedMenuSection(
    usedMenus: List<UsedMenuUi>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = buildAnnotatedString {
                append("사용 중인 메뉴 ")
                withStyle(SpanStyle(color = PrimaryBlue500)) {
                    append(usedMenus.size.toString())
                }
            },
            modifier = Modifier.padding(horizontal = 20.dp),
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Grayscale900,
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (usedMenus.isNotEmpty()) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
            ) {
                items(
                    items = usedMenus,
                    key = { it.id },
                ) { menu ->
                    UsedMenuCard(
                        menuName = menu.name,
                        usageAmount = menu.usageAmount,
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun PriceHistorySection(
    priceHistory: List<PriceHistoryUi>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "변동 이력",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Grayscale900,
        )

        if (priceHistory.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))

            priceHistory.forEachIndexed { index, history ->
                PriceHistoryItem(
                    date = history.date,
                    price = history.price,
                    unitAmount = history.unitAmount,
                    unitDisplayName = history.unitDisplayName,
                    isFirst = index == 0,
                    isLast = index == priceHistory.lastIndex,
                )
            }
        }
    }
}

@Composable
private fun IngredientInfoCard(
    ingredientDetail: IngredientDetailUi,
    onEditClick: () -> Unit,
    onSupplierClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Grayscale100,
                shape = RoundedCornerShape(20.dp),
            )
            .border(
                width = 1.dp,
                color = Grayscale300,
                shape = RoundedCornerShape(20.dp),
            )
            .padding(horizontal = 20.dp, vertical = 20.dp),
    ) {
        Text(
            text = ingredientDetail.category.displayName,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Grayscale600,
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = ingredientDetail.name,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Grayscale900,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .clickable(onClick = onEditClick),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = formatIngredientDetailPriceText(
                    price = ingredientDetail.price,
                    unitAmount = ingredientDetail.unitAmount,
                    unit = ingredientDetail.unit,
                ),
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Grayscale900,
            )

            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                painter = painterResource(CoreUiR.drawable.ic_edit),
                contentDescription = "재료 수정",
                modifier = Modifier.size(18.dp),
                tint = Grayscale500,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(
            color = Grayscale200,
            thickness = 1.dp,
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onSupplierClick),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "공급업체",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Grayscale600,
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = ingredientDetail.supplier.ifBlank { "-" },
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Grayscale900,
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    painter = painterResource(CoreUiR.drawable.ic_chevron_right),
                    contentDescription = "공급업체 수정",
                    modifier = Modifier.size(16.dp),
                    tint = Grayscale500,
                )
            }
        }
    }
}

@Composable
private fun UsedMenuSection(
    usedMenus: List<UsedMenuUi>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = buildAnnotatedString {
                append("사용 중인 메뉴 ")
                withStyle(style = SpanStyle(color = PrimaryBlue500)) {
                    append(usedMenus.size.toString())
                }
            },
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            color = Grayscale900,
        )

        if (usedMenus.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    items = usedMenus,
                    key = { it.id },
                ) { menu ->
                    UsedMenuCard(
                        menuName = menu.name,
                        usageAmount = menu.usageAmount,
                    )
                }
            }
        }
    }
}

@Composable
private fun PriceHistorySection(
    priceHistory: List<PriceHistoryUi>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "변동 이력",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            color = Grayscale900,
        )

        if (priceHistory.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            priceHistory.forEachIndexed { index, history ->
                PriceHistoryItem(
                    date = history.date,
                    price = history.price,
                    unitAmount = history.unitAmount,
                    unitDisplayName = history.unitDisplayName,
                    isFirst = index == 0,
                    isLast = index == priceHistory.lastIndex,
                )
            }
        }
    }
}

@Composable
private fun SectionDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(
                color = Grayscale200,
                shape = RoundedCornerShape(999.dp),
            ),
    )
}

private fun canConfirmIngredientEdit(
    ingredientDetail: IngredientDetailUi,
    editFilter: IngredientFilter,
    editPrice: String,
    editAmount: String,
    editUnit: IngredientUnit,
): Boolean {
    if (editPrice.isBlank() || editAmount.isBlank()) return false

    return ingredientDetail.category != editFilter ||
        ingredientDetail.price.toString() != editPrice ||
        ingredientDetail.unitAmount.toString() != editAmount ||
        ingredientDetail.unit != editUnit
}

private fun formatIngredientDetailPriceText(
    price: Int,
    unitAmount: Int,
    unit: IngredientUnit,
): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)
    return "${numberFormat.format(price)}원 / ${unitAmount}${unit.displayName}"
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
                    UsedMenuUi(1L, "라이트 키위 라임 블렌디드", "100g"),
                    UsedMenuUi(2L, "라이트 키위 라임 블렌디드", "100g"),
                    UsedMenuUi(3L, "라이트 키위 라임 블렌디드", "100g"),
                    UsedMenuUi(4L, "라이트 키위 라임 블렌디드", "100g"),
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
        onToastShown = {},
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
        onToastShown = {},
    )
}
