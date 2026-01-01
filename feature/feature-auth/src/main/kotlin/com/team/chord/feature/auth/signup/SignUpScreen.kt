package com.team.chord.feature.auth.signup

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale800
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue600
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
        onTermsAgreedChanged = viewModel::onTermsAgreedChanged,
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
    onTermsAgreedChanged: (Boolean) -> Unit,
    onSignUpClicked: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

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
                    .verticalScroll(scrollState),
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로가기",
                    tint = Grayscale900,
                )
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
            ) {
                Text(
                    text = "회원가입",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Grayscale900,
                )

                Spacer(modifier = Modifier.height(32.dp))

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
                    placeholder = "아이디를 입력해주세요",
                    imeAction = ImeAction.Next,
                )

                Spacer(modifier = Modifier.height(8.dp))

                UsernameValidationIndicator(validation = uiState.usernameValidation)

                Spacer(modifier = Modifier.height(24.dp))

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
                    placeholder = "비밀번호를 입력해주세요",
                    isPassword = true,
                    imeAction = ImeAction.Next,
                )

                Spacer(modifier = Modifier.height(8.dp))

                PasswordValidationIndicator(validation = uiState.passwordValidation)

                Spacer(modifier = Modifier.height(24.dp))

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
                    placeholder = "비밀번호를 다시 입력해주세요",
                    isPassword = true,
                    imeAction = ImeAction.Done,
                    onImeAction = onSignUpClicked,
                )

                if (uiState.passwordConfirm.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    ValidationItem(
                        isValid = uiState.passwordValidation.isConfirmMatch,
                        text = "비밀번호 일치",
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable { onTermsAgreedChanged(!uiState.isTermsAgreed) },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = uiState.isTermsAgreed,
                        onCheckedChange = onTermsAgreedChanged,
                        colors =
                            CheckboxDefaults.colors(
                                checkedColor = Grayscale800,
                                uncheckedColor = Grayscale400,
                                checkmarkColor = Grayscale100,
                            ),
                    )
                    Text(
                        text = "이용약관에 동의합니다",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Grayscale800,
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
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onSignUpClicked,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Grayscale800,
                            contentColor = Grayscale100,
                            disabledContainerColor = Grayscale400,
                            disabledContentColor = Grayscale100,
                        ),
                    enabled =
                        !uiState.isLoading &&
                            uiState.usernameValidation.isValid &&
                            uiState.passwordValidation.isValid &&
                            uiState.isTermsAgreed,
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = Grayscale100,
                            strokeWidth = 2.dp,
                            modifier = Modifier.height(24.dp),
                        )
                    } else {
                        Text(
                            text = "회원가입",
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
private fun UsernameValidationIndicator(
    validation: UsernameValidation,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        ValidationItem(
            isValid = validation.isLengthValid,
            text = "4자 이상",
        )
        ValidationItem(
            isValid = validation.isPatternValid,
            text = "영문/숫자만 사용",
        )
        if (validation.isLengthValid && validation.isPatternValid) {
            ValidationItem(
                isValid = validation.isAvailable == true,
                text = if (validation.isAvailable == true) "사용 가능한 아이디" else "이미 사용 중인 아이디",
            )
        }
    }
}

@Composable
private fun PasswordValidationIndicator(
    validation: PasswordValidation,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ValidationItem(
            isValid = validation.hasLetter,
            text = "영문",
        )
        ValidationItem(
            isValid = validation.hasDigit,
            text = "숫자",
        )
        ValidationItem(
            isValid = validation.hasSpecialChar,
            text = "특수문자",
        )
        ValidationItem(
            isValid = validation.hasMinLength,
            text = "8자 이상",
        )
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
