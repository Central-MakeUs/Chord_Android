package com.team.chord.feature.menu.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.team.chord.core.ui.component.ChordTopAppBarWithTextAction
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.core.ui.R as CoreUiR
import com.team.chord.feature.menu.R
import com.team.chord.feature.menu.component.CostAnalysisCard
import com.team.chord.feature.menu.component.IngredientListItem
import com.team.chord.feature.menu.component.IngredientTotalRow
import com.team.chord.feature.menu.component.MarginGradeCard
import com.team.chord.feature.menu.component.RecommendedPriceSection
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MenuDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToManagement: (Long) -> Unit,
    onNavigateToIngredientEdit: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MenuDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MenuDetailScreenContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onNavigateToManagement = { onNavigateToManagement(viewModel.getMenuId()) },
        onNavigateToIngredientEdit = { onNavigateToIngredientEdit(viewModel.getMenuId()) },
        modifier = modifier,
    )
}

@Composable
internal fun MenuDetailScreenContent(
    uiState: MenuDetailUiState,
    onNavigateBack: () -> Unit,
    onNavigateToManagement: () -> Unit,
    onNavigateToIngredientEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        ChordTopAppBarWithTextAction(
            title = "",
            actionText = "관리",
            onBackClick = onNavigateBack,
            onActionClick = onNavigateToManagement,
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
            .verticalScroll(scrollState),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // 메뉴명 + 제조시간
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = menuDetail.name,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Grayscale900,
                )
                // 제조시간 배지
                Box(
                    modifier = Modifier
                        .background(
                            color = Grayscale100,
                            shape = RoundedCornerShape(6.dp),
                        )
                        .border(
                            width = 1.dp,
                            color = Grayscale500,
                            shape = RoundedCornerShape(6.dp),
                        )
                        .padding(horizontal = 6.dp),
                ) {
                    Text(
                        text = formatPreparationTime(menuDetail.preparationTimeSeconds),
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Grayscale700,
                    )
                }
            }
            Text(
                text = "${numberFormat.format(menuDetail.sellingPrice)}원",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 32.sp,
                color = Grayscale900,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 원가 분석 카드
        CostAnalysisCard(
            menuDetail = menuDetail,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(Grayscale200),
        )

        // 마진등급 카드
        MarginGradeCard(
            marginGrade = menuDetail.marginGrade,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        // 권장가격 섹션 (있는 경우만)
        if (menuDetail.recommendedPrice != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "권장가격",
                    modifier = Modifier.align(Alignment.Start),
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Grayscale900,
                )
                Spacer(modifier = Modifier.height(24.dp))
                RecommendedPriceSection(
                    recommendedPrice = menuDetail.recommendedPrice,
                    message = menuDetail.recommendedPriceMessage,
                )
            }
        }

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(Grayscale200),
        )

        // 재료 섹션
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
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
