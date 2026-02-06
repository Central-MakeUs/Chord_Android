package com.team.chord.feature.auth.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
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
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTopAppBar
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale800
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue600
import com.team.chord.core.ui.theme.StatusDanger
import com.team.chord.feature.auth.component.AuthTextField

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSignUpSuccess) {
        if (uiState.isSignUpSuccess) {
            viewModel.consumeSignUpSuccess()
            onSignUpSuccess()
        }
    }

    SignUpScreenContent(
        uiState = uiState,
        onUsernameChanged = viewModel::onUsernameChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onPasswordConfirmChanged = viewModel::onPasswordConfirmChanged,
        onSignUpClicked = viewModel::onSignUpClicked,
        onNavigateBack = onNavigateBack,
        modifier = modifier,
    )
}

@Composable
internal fun SignUpScreenContent(
    uiState: SignUpUiState,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onPasswordConfirmChanged: (String) -> Unit,
    onSignUpClicked: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    val isButtonEnabled =
        !uiState.isLoading &&
            uiState.usernameValidation.isValid &&
            uiState.passwordValidation.isValid &&
            uiState.passwordConfirmError == null &&
            uiState.password == uiState.passwordConfirm &&
            uiState.passwordConfirm.isNotEmpty()

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(Grayscale100)
                .imePadding(),
    ) {
        // TopBar
        ChordTopAppBar(
            title = "회원가입",
            onBackClick = onNavigateBack,
        )

        // Scrollable content
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp),
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Username field
            Text(
                text = "아이디",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Grayscale800,
            )

            Spacer(modifier = Modifier.height(8.dp))

            AuthTextField(
                value = uiState.username,
                onValueChange = onUsernameChanged,
                placeholder = "아이디 입력",
                isError = uiState.usernameValidation.error != null,
                imeAction = ImeAction.Next,
            )

            if (uiState.usernameValidation.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.usernameValidation.error,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = StatusDanger,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Password field
            Text(
                text = "비밀번호",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Grayscale800,
            )

            Spacer(modifier = Modifier.height(8.dp))

            AuthTextField(
                value = uiState.password,
                onValueChange = onPasswordChanged,
                placeholder = "비밀번호 입력",
                isPassword = true,
                isError = uiState.passwordValidation.containsUsername,
                imeAction = ImeAction.Next,
            )

            Spacer(modifier = Modifier.height(8.dp))

            PasswordValidationHints(validation = uiState.passwordValidation)

            Spacer(modifier = Modifier.height(24.dp))

            // Password confirm field
            Text(
                text = "비밀번호 확인",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Grayscale800,
            )

            Spacer(modifier = Modifier.height(8.dp))

            AuthTextField(
                value = uiState.passwordConfirm,
                onValueChange = onPasswordConfirmChanged,
                placeholder = "비밀번호 재입력",
                isPassword = true,
                isError = uiState.passwordConfirmError != null,
                imeAction = ImeAction.Done,
                onImeAction = onSignUpClicked,
            )

            if (uiState.passwordConfirmError != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.passwordConfirmError,
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = StatusDanger,
                )
            }

            Spacer(modifier = Modifier.height(48.dp))
        }

        // Bottom fixed button
        ChordLargeButton(
            text = "가입하기",
            onClick = onSignUpClicked,
            modifier =
                Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
            enabled = isButtonEnabled,
        )
    }
}

@Composable
private fun PasswordValidationHints(
    validation: PasswordValidation,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (validation.containsUsername) {
            Text(
                text = "비밀번호에 아이디가 포함될 수 없습니다",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = StatusDanger,
            )
        } else {
            ValidationItem(
                isValid = validation.hasMinLength,
                text = "8자리 이상",
            )
            ValidationItem(
                isValid = validation.hasTwoOrMoreTypes,
                text = "영문 대소문자, 숫자, 특수문자 중 2가지 이상 포함",
            )
        }
    }
}

@Composable
private fun ValidationItem(
    isValid: Boolean,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = if (isValid) PrimaryBlue600 else Grayscale400,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = if (isValid) PrimaryBlue600 else Grayscale500,
        )
    }
}
