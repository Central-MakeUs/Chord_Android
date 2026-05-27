package com.team.chord.feature.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale800
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue600
import com.team.chord.core.ui.theme.StatusDanger
import com.team.chord.feature.auth.component.AuthTextField
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

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            viewModel.consumeLoginSuccess()
            onLoginSuccess(uiState.isSetupCompleted)
        }
    }

    LoginScreenContent(
        uiState = uiState,
        onUsernameChanged = viewModel::onUsernameChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onLoginClicked = viewModel::onLoginClicked,
        onKakaoLoginClicked = {
            if (uiState.isLoading) return@LoginScreenContent
            viewModel.onSocialLoginStarted()
            socialLoginClient.loginWithKakao(
                context = context,
                onSuccess = viewModel::onKakaoAccessTokenReceived,
                onError = viewModel::onSocialLoginFailed,
                onCancelled = viewModel::onSocialLoginCancelled,
            )
        },
        onNaverLoginClicked = {
            if (uiState.isLoading) return@LoginScreenContent
            viewModel.onSocialLoginStarted()
            socialLoginClient.loginWithNaver(
                context = context,
                onSuccess = viewModel::onNaverAccessTokenReceived,
                onError = viewModel::onSocialLoginFailed,
                onCancelled = viewModel::onSocialLoginCancelled,
            )
        },
        onNavigateToSignUp = onNavigateToSignUp,
        modifier = modifier,
    )
}

@Composable
internal fun LoginScreenContent(
    uiState: LoginUiState,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onKakaoLoginClicked: () -> Unit,
    onNaverLoginClicked: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showsCredentialForm by rememberSaveable { mutableStateOf(false) }
    val shouldShowCredentialForm =
        showsCredentialForm ||
            uiState.username.isNotBlank() ||
            uiState.password.isNotBlank() ||
            uiState.usernameError != null ||
            uiState.passwordError != null
    val isInputValid = uiState.username.isNotBlank() && uiState.password.isNotBlank()

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
                title = if (shouldShowCredentialForm) "아이디 로그인 접기" else "아이디로 로그인",
                backgroundColor = PrimaryBlue600,
                foregroundColor = Grayscale100,
                isLoading = false,
                enabled = !uiState.isLoading,
                onClick = {
                    showsCredentialForm = !shouldShowCredentialForm
                },
            )

            if (shouldShowCredentialForm) {
                CredentialLoginSection(
                    uiState = uiState,
                    isInputValid = isInputValid,
                    onUsernameChanged = onUsernameChanged,
                    onPasswordChanged = onPasswordChanged,
                    onLoginClicked = onLoginClicked,
                )
            }

            Spacer(modifier = Modifier.height(if (shouldShowCredentialForm) 28.dp else 32.dp))

            DividerWithText()

            Spacer(modifier = Modifier.height(28.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                SocialIconButton(
                    text = "N",
                    color = NaverGreen,
                    enabled = !uiState.isLoading,
                    onClick = onNaverLoginClicked,
                )
            }

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

            Text(
                text = "회원가입",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Grayscale500,
                modifier =
                    Modifier
                        .clickable(enabled = !uiState.isLoading, onClick = onNavigateToSignUp)
                        .padding(bottom = 48.dp),
            )
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
private fun CredentialLoginSection(
    uiState: LoginUiState,
    isInputValid: Boolean,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
    ) {
        AuthFieldSection(
            title = "아이디",
            error = uiState.usernameError,
        ) {
            AuthTextField(
                value = uiState.username,
                onValueChange = onUsernameChanged,
                placeholder = "아이디를 입력해주세요.",
                isError = uiState.usernameError != null,
                imeAction = ImeAction.Next,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        AuthFieldSection(
            title = "비밀번호",
            error = uiState.passwordError,
        ) {
            AuthTextField(
                value = uiState.password,
                onValueChange = onPasswordChanged,
                placeholder = "비밀번호를 입력해주세요.",
                isPassword = true,
                isError = uiState.passwordError != null,
                imeAction = ImeAction.Done,
                onImeAction = onLoginClicked,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        ProviderButton(
            title = "로그인",
            backgroundColor = PrimaryBlue600,
            foregroundColor = Grayscale100,
            isLoading = uiState.isLoading,
            enabled = isInputValid && !uiState.isLoading,
            onClick = onLoginClicked,
        )
    }
}

@Composable
private fun AuthFieldSection(
    title: String,
    error: String?,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Grayscale900,
        )

        Spacer(modifier = Modifier.height(8.dp))
        content()

        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = StatusDanger,
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

@Composable
private fun DividerWithText() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = Grayscale300,
        )
        Text(
            text = "또는",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = Grayscale400,
            modifier = Modifier.padding(horizontal = 10.dp),
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = Grayscale300,
        )
    }
}

@Composable
private fun SocialIconButton(
    text: String,
    color: Color,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(56.dp),
        shape = CircleShape,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = Grayscale100,
                contentColor = color,
                disabledContainerColor = Grayscale200,
                disabledContentColor = Grayscale500,
            ),
        enabled = enabled,
    ) {
        Text(
            text = text,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Black,
            fontSize = 26.sp,
        )
    }
}

private val KakaoYellow = Color(0xFFFEE500)
private val NaverGreen = Color(0xFF03C75A)
