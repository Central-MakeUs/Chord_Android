package com.team.chord.feature.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.analytics.Analytics
import com.team.chord.core.analytics.AnalyticsErrorCategory
import com.team.chord.core.analytics.AnalyticsEvent
import com.team.chord.core.analytics.SocialLoginProvider
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale800
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue600
import com.team.chord.core.ui.theme.StatusDanger
import com.team.chord.feature.auth.social.AndroidSocialLoginClient

@Composable
fun LoginScreen(
    onLoginSuccess: (Boolean) -> Unit,
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val socialLoginClient = remember { AndroidSocialLoginClient() }

    LaunchedEffect(Unit) {
        Analytics.track(AnalyticsEvent.LoginScreenViewed)
    }

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            viewModel.consumeLoginSuccess()
            onLoginSuccess(uiState.isSetupCompleted)
        }
    }

    LoginScreenContent(
        uiState = uiState,
        onKakaoLoginClicked = {
            if (uiState.isLoading) return@LoginScreenContent
            Analytics.track(AnalyticsEvent.SocialLoginTapped(SocialLoginProvider.KAKAO))
            viewModel.onSocialLoginStarted()
            socialLoginClient.loginWithKakao(
                context = context,
                onSuccess = viewModel::onKakaoAccessTokenReceived,
                onError = { message ->
                    Analytics.track(
                        AnalyticsEvent.LoginFailed(
                            provider = SocialLoginProvider.KAKAO,
                            errorCategory = message.toProviderErrorCategory(),
                        ),
                    )
                    viewModel.onSocialLoginFailed(message)
                },
                onCancelled = viewModel::onSocialLoginCancelled,
            )
        },
        onNaverLoginClicked = {
            if (uiState.isLoading) return@LoginScreenContent
            Analytics.track(AnalyticsEvent.SocialLoginTapped(SocialLoginProvider.NAVER))
            viewModel.onSocialLoginStarted()
            socialLoginClient.loginWithNaver(
                context = context,
                onSuccess = viewModel::onNaverAccessTokenReceived,
                onError = { message ->
                    Analytics.track(
                        AnalyticsEvent.LoginFailed(
                            provider = SocialLoginProvider.NAVER,
                            errorCategory = message.toProviderErrorCategory(),
                        ),
                    )
                    viewModel.onSocialLoginFailed(message)
                },
                onCancelled = viewModel::onSocialLoginCancelled,
            )
        },
        modifier = modifier,
    )
}

@Composable
internal fun LoginScreenContent(
    uiState: LoginUiState,
    onKakaoLoginClicked: () -> Unit,
    onNaverLoginClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Grayscale100),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(86.dp))

            Text(
                text = "카페코치",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = PrimaryBlue600,
            )

            Spacer(modifier = Modifier.height(36.dp))

            SignUpCallout()

            Spacer(modifier = Modifier.height(24.dp))

            ProviderButton(
                title = "카카오로 3초만에 로그인!",
                backgroundColor = KakaoYellow,
                foregroundColor = Color.Black,
                leadingText = "K",
                isLoading = uiState.isLoading,
                enabled = !uiState.isLoading,
                onClick = onKakaoLoginClicked,
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProviderButton(
                title = "네이버로 로그인",
                backgroundColor = NaverGreen,
                foregroundColor = Grayscale100,
                leadingText = "N",
                isLoading = uiState.isLoading,
                enabled = !uiState.isLoading,
                onClick = onNaverLoginClicked,
            )

            if (uiState.authError != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.authError,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = StatusDanger,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun SignUpCallout() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .background(
                        color = Grayscale800,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .padding(horizontal = 18.dp, vertical = 10.dp),
        ) {
            Text(
                text = "지금 가입하고",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Grayscale100,
            )
            Text(
                text = "카페 수익 전략을 받아보세요!",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Grayscale100,
            )
        }
    }
}

@Composable
private fun ProviderButton(
    title: String,
    backgroundColor: Color,
    foregroundColor: Color,
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    leadingText: String? = null,
) {
    Button(
        onClick = onClick,
        modifier =
            Modifier
                .fillMaxWidth()
                .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = foregroundColor,
                disabledContainerColor = Grayscale200,
                disabledContentColor = Grayscale500,
            ),
        enabled = enabled,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = foregroundColor,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp),
            )
        } else {
            if (leadingText != null) {
                Text(
                    text = leadingText,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Text(
                text = title,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )
        }
    }
}

private val KakaoYellow = Color(0xFFFEE500)
private val NaverGreen = Color(0xFF03C75A)

private fun String.toProviderErrorCategory(): AnalyticsErrorCategory =
    when {
        contains("설정이 필요합니다") -> AnalyticsErrorCategory.MISSING_CONFIG
        contains("access token을 가져오지 못했습니다") -> AnalyticsErrorCategory.INVALID_PROVIDER_RESPONSE
        isBlank() -> AnalyticsErrorCategory.SDK_UNKNOWN
        else -> AnalyticsErrorCategory.SDK_ERROR
    }
