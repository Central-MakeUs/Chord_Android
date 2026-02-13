package com.team.chord.feature.menu.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.component.ChordOneButtonDialog
import com.team.chord.core.ui.component.ChordTopAppBar
import com.team.chord.core.ui.component.ChordTwoButtonDialog
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.core.ui.theme.StatusDanger
import com.team.chord.core.ui.R as CoreUiR
import com.team.chord.feature.menu.component.IngredientListItem
import com.team.chord.feature.menu.component.IngredientTotalRow
import com.team.chord.feature.menu.component.ProfitAnalysisCard
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MenuDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToManagement: (Long) -> Unit,
    onNavigateToIngredientEdit: (Long) -> Unit,
    onMenuDeleted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MenuDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MenuDetailScreenContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onNavigateToManagement = { onNavigateToManagement(viewModel.getMenuId()) },
        onNavigateToIngredientEdit = { onNavigateToIngredientEdit(viewModel.getMenuId()) },
        onShowDropdownMenu = viewModel::showDropdownMenu,
        onHideDropdownMenu = viewModel::hideDropdownMenu,
        onShowDeleteDialog = viewModel::showDeleteDialog,
        onHideDeleteDialog = viewModel::hideDeleteDialog,
        onDeleteMenu = viewModel::deleteMenu,
        onDeleteSuccessConfirm = {
            viewModel.hideDeleteSuccessDialog()
            onMenuDeleted()
        },
        modifier = modifier,
    )
}

@Composable
internal fun MenuDetailScreenContent(
    uiState: MenuDetailUiState,
    onNavigateBack: () -> Unit,
    onNavigateToManagement: () -> Unit,
    onNavigateToIngredientEdit: () -> Unit,
    onShowDropdownMenu: () -> Unit,
    onHideDropdownMenu: () -> Unit,
    onShowDeleteDialog: () -> Unit,
    onHideDeleteDialog: () -> Unit,
    onDeleteMenu: () -> Unit,
    onDeleteSuccessConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        ChordTopAppBar(
            title = "",
            onBackClick = onNavigateBack,
            actionContent = {
                Box {
                    Icon(
                        painter = painterResource(CoreUiR.drawable.ic_ellipsis),
                        contentDescription = "메뉴",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onShowDropdownMenu() },
                        tint = Grayscale900,
                    )

                    if (uiState is MenuDetailUiState.Success) {
                        DropdownMenu(
                            expanded = uiState.showDropdownMenu,
                            onDismissRequest = onHideDropdownMenu,
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "수정",
                                        fontFamily = PretendardFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = Grayscale900,
                                    )
                                },
                                onClick = {
                                    onHideDropdownMenu()
                                    onNavigateToManagement()
                                },
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "삭제",
                                        fontFamily = PretendardFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = StatusDanger,
                                    )
                                },
                                onClick = onShowDeleteDialog,
                            )
                        }
                    }
                }
            },
        )

        when (uiState) {
            is MenuDetailUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = PrimaryBlue500)
                }
            }

            is MenuDetailUiState.Error -> {
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

            is MenuDetailUiState.Success -> {
                MenuDetailContent(
                    menuDetail = uiState.menuDetail,
                    onNavigateToIngredientEdit = onNavigateToIngredientEdit,
                )

                if (uiState.showDeleteDialog) {
                    ChordTwoButtonDialog(
                        title = "메뉴를 삭제할까요?",
                        onDismiss = onHideDeleteDialog,
                        onConfirm = onDeleteMenu,
                        dismissText = "취소하기",
                        confirmText = "삭제하기",
                    )
                }

                if (uiState.showDeleteSuccessDialog) {
                    ChordOneButtonDialog(
                        title = "메뉴가 삭제되었어요",
                        onConfirm = onDeleteSuccessConfirm,
                        confirmText = "확인했어요",
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuDetailContent(
    menuDetail: MenuDetailUi,
    onNavigateToIngredientEdit: () -> Unit,
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

        // 메뉴 정보 카드
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Grayscale100,
                    shape = RoundedCornerShape(16.dp),
                )
                .border(
                    width = 1.dp,
                    color = Grayscale300,
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(20.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 왼쪽: 메뉴명 + 가격
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = menuDetail.name,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Grayscale900,
                )
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = numberFormat.format(menuDetail.sellingPrice),
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        color = Grayscale900,
                    )
                    Text(
                        text = "원",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Grayscale900,
                        modifier = Modifier.padding(bottom = 2.dp),
                    )
                }
            }

            // 세로 구분선
            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp),
                color = Grayscale300,
                thickness = 1.dp,
            )

            // 오른쪽: 제조시간
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "제조시간",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Grayscale500,
                )
                Text(
                    text = formatPreparationTime(menuDetail.preparationTimeSeconds),
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Grayscale900,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 수익등급 통합 카드
        ProfitAnalysisCard(
            marginGrade = menuDetail.marginGrade,
            marginRatio = menuDetail.marginRatio,
            costRatio = menuDetail.costRatio,
            contributionProfit = menuDetail.contributionProfit,
        )

        // 권장가격 한 줄 (주의/위험 등급에서만 노출)
        if (shouldShowRecommendedPrice(menuDetail)) {
            menuDetail.recommendedPrice?.let { recommendedPrice ->
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        painter = painterResource(CoreUiR.drawable.ic_check),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = PrimaryBlue500,
                    )
                    Text(
                        text = "권장가격 ${numberFormat.format(recommendedPrice)}원",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = PrimaryBlue500,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 정보 안내 문구
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                painter = painterResource(CoreUiR.drawable.ic_tooltip),
                contentDescription = null,
                modifier = Modifier
                    .size(14.dp)
                    .padding(top = 2.dp),
                tint = Grayscale500,
            )
            Column {
                Text(
                    text = "마진율은 재료비와 인건비를 기준으로 계산한 추정값이에요.",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Grayscale500,
                )
                Text(
                    text = "우리 가게의 효율을 알려드려요.",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Grayscale500,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 재료 섹션 카드
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Grayscale100,
                    shape = RoundedCornerShape(16.dp),
                )
                .border(
                    width = 1.dp,
                    color = Grayscale300,
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(20.dp),
        ) {
            Row(
                modifier = Modifier.clickable { onNavigateToIngredientEdit() },
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("재료 ")
                        withStyle(SpanStyle(color = PrimaryBlue500)) {
                            append("${menuDetail.ingredients.size}")
                        }
                    },
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Grayscale900,
                )
                Icon(
                    painter = painterResource(CoreUiR.drawable.ic_arrow_right),
                    contentDescription = "재료 수정",
                    tint = Grayscale600,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            menuDetail.ingredients.forEach { ingredient ->
                IngredientListItem(ingredient = ingredient)
            }

            IngredientTotalRow(
                totalPrice = menuDetail.ingredients.sumOf { it.price },
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

private fun formatPreparationTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return if (minutes > 0 && remainingSeconds > 0) {
        "약 ${minutes}분 ${remainingSeconds}초"
    } else if (minutes > 0) {
        "약 ${minutes}분"
    } else {
        "약 ${remainingSeconds}초"
    }
}

private fun shouldShowRecommendedPrice(menuDetail: MenuDetailUi): Boolean {
    val normalizedGradeCode = menuDetail.marginGrade.code.trim().uppercase(Locale.ROOT)
    val isWarningOrDanger = normalizedGradeCode == "WARNING" || normalizedGradeCode == "DANGER"
    return isWarningOrDanger && menuDetail.recommendedPrice != null
}
