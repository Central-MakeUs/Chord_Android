package com.team.chord.feature.aicoach.strategy.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.domain.model.strategy.StrategyProgressStatus
import com.team.chord.core.ui.R as CoreUiR
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordToast
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.aicoach.component.StrategyDetailBadge
import com.team.chord.feature.aicoach.component.StrategyDetailSectionCard

@Composable
fun StrategyDetailScreen(
    onNavigateBack: () -> Unit,
    onStrategyStarted: (String) -> Unit,
    onNavigateToComplete: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StrategyDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.startedMessage) {
        val message = uiState.startedMessage ?: return@LaunchedEffect
        onStrategyStarted(message)
        viewModel.consumeStartedMessage()
    }

    LaunchedEffect(uiState.completionPhrase) {
        val phrase = uiState.completionPhrase ?: return@LaunchedEffect
        onNavigateToComplete(phrase)
        viewModel.consumeCompletionPhrase()
    }

    StrategyDetailScreenContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onPrimaryActionClick = viewModel::onPrimaryActionClick,
        onErrorDismissed = viewModel::clearError,
        modifier = modifier,
    )
}

@Composable
private fun StrategyDetailScreenContent(
    uiState: StrategyDetailUiState,
    onNavigateBack: () -> Unit,
    onPrimaryActionClick: () -> Unit,
    onErrorDismissed: () -> Unit,
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

    val detail = uiState.detail
    val buttonEnabled = detail != null && detail.status != StrategyProgressStatus.COMPLETED && !uiState.isSubmitting
    val buttonText = when (detail?.status) {
        StrategyProgressStatus.NOT_STARTED -> "전략 실행하기"
        StrategyProgressStatus.IN_PROGRESS -> "실행 완료"
        StrategyProgressStatus.COMPLETED -> "실행 완료"
        null -> ""
    }

    Scaffold(
        modifier = modifier,
        containerColor = Grayscale200,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                ChordToast(
                    text = data.visuals.message,
                    leadingIcon = CoreUiR.drawable.ic_check,
                )
            }
        },
        bottomBar = {
            if (detail != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Grayscale200)
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                ) {
                    ChordLargeButton(
                        text = buttonText,
                        enabled = buttonEnabled,
                        onClick = onPrimaryActionClick,
                    )
                }
            }
        },
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = PrimaryBlue500)
                }
            }

            detail == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "전략 상세를 불러오지 못했어요.",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Grayscale600,
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                        ) {
                            IconButton(
                                onClick = onNavigateBack,
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .size(24.dp),
                            ) {
                                Icon(
                                    painter = painterResource(id = CoreUiR.drawable.ic_chevron_left),
                                    contentDescription = "뒤로가기",
                                    tint = Grayscale900,
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = detail.weekLabel,
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Grayscale600,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    }

                    item {
                        if (detail.status == StrategyProgressStatus.COMPLETED || detail.status == StrategyProgressStatus.IN_PROGRESS) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center,
                            ) {
                                StrategyDetailBadge(
                                    text = if (detail.status == StrategyProgressStatus.COMPLETED) "실행완료" else "진행중",
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = detail.title,
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = if (detail.status == StrategyProgressStatus.COMPLETED) Grayscale600 else PrimaryBlue500,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    }

                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    item {
                        StrategyDetailSectionCard(
                            title = "진단",
                            headline = detail.diagnosisHeadline,
                            body = detail.diagnosisBody,
                            menuNames = detail.menuNames,
                        )
                    }

                    item {
                        StrategyDetailSectionCard(
                            title = "행동 가이드",
                            body = detail.guideBody,
                        )
                    }

                    item {
                        StrategyDetailSectionCard(
                            title = "기대효과",
                            body = detail.expectedEffectBody,
                        )
                    }

                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
        }
    }
}
