package com.team.chord.feature.aicoach.strategy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.R as CoreUiR
import com.team.chord.core.ui.component.ChordToast
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.aicoach.component.MonthNavigator
import com.team.chord.feature.aicoach.component.StrategyCard
import com.team.chord.feature.aicoach.component.StrategyFilterTabs
import com.team.chord.feature.aicoach.component.StrategyHistoryItem
import java.time.YearMonth

@Composable
fun AiStrategyScreen(
    onNavigateToStrategyDetail: (strategyId: Long, type: String) -> Unit,
    strategyStartedMessage: String? = null,
    onStrategyStartedMessageConsumed: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: AiStrategyViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.refresh()
        onPauseOrDispose { }
    }

    AiStrategyScreenContent(
        uiState = uiState,
        strategyStartedMessage = strategyStartedMessage,
        onStrategyStartedMessageConsumed = onStrategyStartedMessageConsumed,
        onMonthChange = viewModel::onMonthChange,
        onFilterChange = viewModel::onFilterChange,
        onRefresh = viewModel::refresh,
        onErrorDismissed = viewModel::clearError,
        onNavigateToStrategyDetail = onNavigateToStrategyDetail,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AiStrategyScreenContent(
    uiState: AiStrategyUiState,
    strategyStartedMessage: String?,
    onStrategyStartedMessageConsumed: () -> Unit,
    onMonthChange: (YearMonth) -> Unit,
    onFilterChange: (StrategyFilter) -> Unit,
    onRefresh: () -> Unit,
    onErrorDismissed: () -> Unit,
    onNavigateToStrategyDetail: (Long, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        val message = uiState.errorMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Short,
        )
        onErrorDismissed()
    }

    LaunchedEffect(strategyStartedMessage) {
        val message = strategyStartedMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Short,
        )
        onStrategyStartedMessageConsumed()
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
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Grayscale200),
        ) {
            Text(
                text = "이번주 추천 전략",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Grayscale900,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp),
            )
            Text(
                text = uiState.generatedAtMessage,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Grayscale600,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.recommendedStrategies.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .background(
                            color = Grayscale100,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.recommendedEmptyTitle,
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = PrimaryBlue500,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.recommendedEmptyDescription,
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            color = Grayscale500,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(
                        items = uiState.recommendedStrategies,
                        key = { it.id },
                    ) { strategy ->
                        StrategyCard(
                            state = strategy.state,
                            title = strategy.title,
                            description = strategy.description,
                            onClick = { onNavigateToStrategyDetail(strategy.id, strategy.type) },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Grayscale100,
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                        )
                        .padding(top = 20.dp),
                ) {
                    MonthNavigator(
                        currentMonth = uiState.selectedMonth,
                        onPreviousMonth = { onMonthChange(uiState.selectedMonth.minusMonths(1)) },
                        onNextMonth = { onMonthChange(uiState.selectedMonth.plusMonths(1)) },
                        modifier = Modifier.padding(horizontal = 20.dp),
                        trailingContent = {
                            StrategyFilterTabs(
                                selectedFilter = uiState.selectedFilter,
                                onFilterSelect = onFilterChange,
                            )
                        },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    when {
                        uiState.isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator(color = PrimaryBlue500)
                            }
                        }

                        uiState.historyItems.isEmpty() -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 48.dp)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = uiState.historyEmptyMessage,
                                    fontFamily = PretendardFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = Grayscale500,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }

                        else -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 20.dp),
                            ) {
                                items(
                                    items = uiState.historyItems,
                                    key = { it.id },
                                ) { item ->
                                    StrategyHistoryItem(
                                        weekLabel = item.weekLabel,
                                        title = item.title,
                                        description = item.description,
                                        onClick = { onNavigateToStrategyDetail(item.id, item.type) },
                                    )
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = Grayscale200,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
