package com.team.chord.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale800
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.feature.onboarding.component.OnboardingPage
import com.team.chord.feature.onboarding.component.OnboardingPageData
import com.team.chord.feature.onboarding.component.PageIndicator

private val onboardingPages =
    listOf(
        OnboardingPageData(
            title = "원가율이 만드는 것",
            description = "원가율 5%에 따라\n월수익 100~300만원의 차이가 발생해요",
        ),
        OnboardingPageData(
            title = "코치코치가 대신 해드려요",
            description = "어렵고 귀찮은 원가 관리부터\n우리 가게 전략 설계까지!",
        ),
        OnboardingPageData(
            title = "메뉴와 재료만 알려주세요",
            description = "바로 수익 진단을 내려드릴게요\n복잡한건 코치코치에게 맡기세요",
        ),
    )

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isCompleted) {
        if (uiState.isCompleted) {
            onComplete()
        }
    }

    OnboardingScreenContent(
        uiState = uiState,
        onPageChanged = viewModel::onPageChanged,
        onNextClicked = viewModel::onNextClicked,
        modifier = modifier,
    )
}

@Composable
internal fun OnboardingScreenContent(
    uiState: OnboardingUiState,
    onPageChanged: (Int) -> Unit,
    onNextClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState =
        rememberPagerState(
            initialPage = uiState.currentPage,
            pageCount = { uiState.totalPages },
        )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onPageChanged(page)
        }
    }

    LaunchedEffect(uiState.currentPage) {
        if (pagerState.currentPage != uiState.currentPage) {
            pagerState.animateScrollToPage(uiState.currentPage)
        }
    }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Grayscale100),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            OnboardingPage(
                pageData = onboardingPages[page],
            )
        }

        Column(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            PageIndicator(
                totalPages = uiState.totalPages,
                currentPage = uiState.currentPage,
            )

            Button(
                onClick = onNextClicked,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Grayscale800,
                        contentColor = Grayscale100,
                    ),
            ) {
                Text(
                    text = if (uiState.currentPage == uiState.totalPages - 1) "시작하기" else "다음",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                )
            }
        }
    }
}
