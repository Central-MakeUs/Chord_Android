package com.team.chord.feature.ingredient.detail

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.team.chord.core.ui.component.ChordToast
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
import com.team.chord.feature.ingredient.formatIngredientUnitLabel
import java.text.NumberFormat
import java.util.Locale
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
    onUpdatePriceInfo: (IngredientFilter, Int, Int, IngredientUnit) -> Unit,
    onUpdateSupplier: (String) -> Unit,
    onToastShown: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
                    )
                }
            }
        }
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
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        IngredientInfoCard(
            ingredientDetail = ingredientDetail,
            onEditClick = onEditClick,
            onSupplierClick = onSupplierClick,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(32.dp))

        UsedMenuSection(
            usedMenus = ingredientDetail.usedMenus,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(32.dp))

        SectionDivider()

        Spacer(modifier = Modifier.height(24.dp))

        PriceHistorySection(
            priceHistory = ingredientDetail.priceHistory,
            modifier = Modifier.padding(horizontal = 20.dp),
        )
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
                shape = RoundedCornerShape(16.dp),
            )
            .border(
                width = 1.5.dp,
                color = Grayscale300,
                shape = RoundedCornerShape(16.dp),
            )
            .padding(top = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        ) {
            Text(
                text = "식재료",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 20.sp,
                color = Grayscale500,
            )

            Text(
                text = ingredientDetail.name,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 26.sp,
                color = Grayscale900,
            )

            Row(
                modifier = Modifier.clickable(onClick = onEditClick),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = formatIngredientUnitLabel(
                        ingredientDetail.unitAmount,
                        ingredientDetail.unit,
                    ),
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    lineHeight = 29.sp,
                    color = Grayscale600,
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = formatIngredientPriceOnlyText(ingredientDetail.price),
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    lineHeight = 32.sp,
                    color = Grayscale900,
                )

                Spacer(modifier = Modifier.width(8.dp))

                EditChipButton(onClick = onEditClick)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp),
            color = Grayscale200,
            thickness = 1.dp,
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
                lineHeight = 26.sp,
                color = Grayscale500,
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = ingredientDetail.supplier.ifBlank { "-" },
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
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
            fontSize = 18.sp,
            lineHeight = 26.sp,
            color = Grayscale900,
        )

        if (usedMenus.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(
                    items = usedMenus,
                    key = { index, menu -> "${menu.id}-${menu.name}-${menu.usageAmount}-$index" },
                ) { _, menu ->
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
            fontSize = 18.sp,
            lineHeight = 26.sp,
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

@Composable
private fun EditChipButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Grayscale300,
                shape = RoundedCornerShape(8.dp),
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(
            text = "수정",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = Grayscale600,
        )
    }
}

private fun formatIngredientPriceOnlyText(price: Int): String {
    val numberFormatter = NumberFormat.getNumberInstance(Locale.KOREA)
    return "${numberFormatter.format(price)}원"
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
        onUpdatePriceInfo = { _, _, _, _ -> },
        onUpdateSupplier = {},
        onToastShown = {},
    )
}
