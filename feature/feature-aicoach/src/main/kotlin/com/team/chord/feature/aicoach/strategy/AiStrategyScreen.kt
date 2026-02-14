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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.component.ChordToast
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.aicoach.component.MonthNavigator
import com.team.chord.feature.aicoach.component.StrategyCard
import com.team.chord.feature.aicoach.component.StrategyFilterTabs
import com.team.chord.feature.aicoach.component.StrategyHistoryItem
import com.team.chord.core.ui.R as CoreUiR

@Composable
fun AiStrategyScreen(
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
        onMonthChange = viewModel::onMonthChange,
        onFilterChange = viewModel::onFilterChange,
        onRefresh = viewModel::refresh,
        onErrorDismissed = viewModel::clearError,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AiStrategyScreenContent(
    uiState: AiStrategyUiState,
    onMonthChange: (java.time.YearMonth) -> Unit,
    onFilterChange: (StrategyFilter) -> Unit,
    onRefresh: () -> Unit,
    onErrorDismissed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            snackbarHostState.showSnackbar(
                message = uiState.errorMessage,
                duration = SnackbarDuration.Short,
            )
            onErrorDismissed()
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
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Grayscale200),
        ) {
            // Header: 이번주 추천 전략
            Text(
                text = "이번주 추천 전략",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Grayscale900,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
            )

            // Recommended strategy cards - horizontal scroll
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
                        type = strategy.type,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Monthly history section with rounded top corners
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
                    // Month navigator with filter tabs
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

                    // History list
                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(color = PrimaryBlue500)
                        }
                    } else if (uiState.historyItems.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Grayscale200,
                                        shape = RoundedCornerShape(16.dp),
                                    )
                                    .padding(vertical = 40.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "저장된 전략이 없습니다",
                                    fontFamily = PretendardFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = Grayscale500,
                                )
                            }
                        }
                    } else {
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
