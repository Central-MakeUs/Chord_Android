package com.team.chord.feature.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue600
import com.team.chord.feature.auth.component.AuthTextField

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            viewModel.consumeLoginSuccess()
            onLoginSuccess()
        }
    }

    LoginScreenContent(
        uiState = uiState,
        onUsernameChanged = viewModel::onUsernameChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onLoginClicked = viewModel::onLoginClicked,
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
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
            Spacer(modifier = Modifier.height(120.dp))

            Text(
                text = "코치코치",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = PrimaryBlue600,
            )

            Spacer(modifier = Modifier.height(48.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "아이디",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Grayscale900,
                )

                Spacer(modifier = Modifier.height(8.dp))

                AuthTextField(
                    value = uiState.username,
                    onValueChange = onUsernameChanged,
                    placeholder = "아이디를 입력해주세요.",
                    imeAction = ImeAction.Next,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "비밀번호",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Grayscale900,
                )

                Spacer(modifier = Modifier.height(8.dp))

                AuthTextField(
                    value = uiState.password,
                    onValueChange = onPasswordChanged,
                    placeholder = "비밀번호를 입력해주세요.",
                    isPassword = true,
                    imeAction = ImeAction.Done,
                    onImeAction = onLoginClicked,
                )
            }

            if (uiState.errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.errorMessage,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = PrimaryBlue600,
                    modifier = Modifier.align(Alignment.Start),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onLoginClicked,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue600,
                        contentColor = Grayscale100,
                        disabledContainerColor = Grayscale200,
                        disabledContentColor = Grayscale500,
                    ),
                enabled = isInputValid && !uiState.isLoading,
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = Grayscale100,
                        strokeWidth = 2.dp,
                        modifier = Modifier.height(24.dp),
                    )
                } else {
                    Text(
                        text = "로그인",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                    )
                }
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
                        .clickable(onClick = onNavigateToSignUp)
                        .padding(bottom = 48.dp),
            )
        }
    }
}
