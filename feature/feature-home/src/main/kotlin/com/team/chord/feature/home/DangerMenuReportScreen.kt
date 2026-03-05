package com.team.chord.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.R as CoreUiR
import com.team.chord.core.ui.component.ChordTopAppBar
import com.team.chord.core.ui.component.ChordTooltipBubble
import com.team.chord.core.ui.component.TooltipDirection
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue100
import com.team.chord.core.ui.theme.PrimaryBlue400
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.core.ui.theme.StatusDanger
import com.team.chord.core.ui.theme.StatusDangerBackground
import com.team.chord.core.ui.theme.StatusSafe
import com.team.chord.core.ui.theme.StatusSafeBackground

@Composable
fun DangerMenuReportScreen(
    onNavigateBack: () -> Unit,
    onNavigateToStrategyDetail: (strategyId: Long, type: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DangerMenuReportViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DangerMenuReportScreenContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onNavigateToStrategyDetail = onNavigateToStrategyDetail,
        modifier = modifier,
    )
}

@Composable
internal fun DangerMenuReportScreenContent(
    uiState: DangerMenuReportUiState,
    onNavigateBack: () -> Unit,
    onNavigateToStrategyDetail: (strategyId: Long, type: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale200),
    ) {
        ChordTopAppBar(
            title = "",
            onBackClick = onNavigateBack,
            backgroundColor = Grayscale200,
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = PrimaryBlue500)
            }
            return
        }

        if (uiState.errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = uiState.errorMessage,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Grayscale600,
                )
            }
            return
        }

        if (uiState.isStable) {
            StableContent(dateLabel = uiState.dateLabel)
        } else {
            DangerContent(
                dateLabel = uiState.dateLabel,
                menus = uiState.menus,
                onNavigateToStrategyDetail = onNavigateToStrategyDetail,
            )
        }
    }
}

@Composable
private fun StableContent(
    dateLabel: String,
    modifier: Modifier = Modifier,
) {
    val goodReportPainter = painterResource(id = R.drawable.ic_menu_report_good)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
            DateChip(dateLabel = dateLabel)
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "메뉴 운영",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = StatusSafe,
                )
                Text(
                    text = "안정",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = StatusSafe,
                    modifier = Modifier
                        .background(
                            color = StatusSafeBackground,
                            shape = RoundedCornerShape(12.dp),
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "이번주는 진단이\n필요한 메뉴가 없어요",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Grayscale700,
                lineHeight = 34.sp,
            )

            Spacer(modifier = Modifier.height(52.dp))

            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                val imageSize = (maxWidth - 102.dp).coerceAtLeast(0.dp)
                Image(
                    painter = goodReportPainter,
                    contentDescription = null,
                    modifier = Modifier.size(imageSize),
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
}

@Composable
private fun DangerContent(
    dateLabel: String,
    menus: List<DangerMenuReportMenuUi>,
    onNavigateToStrategyDetail: (strategyId: Long, type: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showTooltip by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                DateChip(dateLabel = dateLabel)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_warning_menu),
                    contentDescription = null,
                    modifier = Modifier.size(124.dp),
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "원가율 50%",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                    color = StatusDanger,
                )
                Text(
                    text = " ▲",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = StatusDanger,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (showTooltip) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    ChordTooltipBubble(
                        text = "원가율이 50%이상인 메뉴를 대상으로 전략이 매주 일요일 밤 새롭게 생성돼요",
                        direction = TooltipDirection.Down,
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "관리가 필요한 메뉴",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Grayscale700,
                )
                Icon(
                    painter = painterResource(id = CoreUiR.drawable.ic_question),
                    contentDescription = "설명",
                    tint = Grayscale500,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(16.dp)
                        .clickable { showTooltip = !showTooltip },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        items(
            items = menus,
            key = { it.strategyId },
        ) { menu ->
            DangerMenuCard(
                menu = menu,
                onClick = { onNavigateToStrategyDetail(menu.strategyId, "DANGER") },
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun DateChip(
    dateLabel: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = dateLabel,
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        color = Grayscale700,
        modifier = modifier
            .background(
                color = Grayscale300,
                shape = RoundedCornerShape(50.dp),
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
private fun DangerMenuCard(
    menu: DangerMenuReportMenuUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Grayscale100,
                shape = RoundedCornerShape(24.dp),
            )
            .padding(20.dp),
    ) {
        Text(
            text = "위험",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = StatusDanger,
            modifier = Modifier
                .background(
                    color = StatusDangerBackground,
                    shape = RoundedCornerShape(10.dp),
                )
                .padding(horizontal = 10.dp, vertical = 5.dp),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = menu.menuName,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp,
            color = Grayscale900,
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
        ) {
            Column {
                Text(
                    text = "원가율",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Grayscale500,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = menu.costRateText,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = StatusDanger,
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

            Box(
                modifier = Modifier
                    .height(56.dp)
                    .width(1.dp)
                    .background(Grayscale300),
            )

            Spacer(modifier = Modifier.size(16.dp))

            Column {
                Text(
                    text = "마진율",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Grayscale500,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = menu.marginRateText,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Grayscale700,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .background(
                        color = PrimaryBlue100,
                        shape = RoundedCornerShape(20.dp),
                    )
                    .clickable(onClick = onClick)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = "전략 확인",
                    style = TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = PrimaryBlue500,
                    ),
                )
                Icon(
                    painter = painterResource(id = CoreUiR.drawable.ic_chevron_right),
                    contentDescription = null,
                    tint = PrimaryBlue400,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
    }
}
