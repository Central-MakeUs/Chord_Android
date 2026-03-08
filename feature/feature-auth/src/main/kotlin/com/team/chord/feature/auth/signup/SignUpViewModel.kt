package com.team.chord.feature.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.AuthResult
import com.team.chord.core.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SignUpUiState())
        val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

        fun onUsernameChanged(username: String) {
            val validation = validateUsername(username)
            val currentState = _uiState.value
            val passwordValidation = validatePassword(currentState.password, username)
            _uiState.update {
                it.copy(
                    username = username,
                    errorMessage = null,
                    usernameValidation = validation,
                    passwordValidation = passwordValidation,
                )
            }
        }

        fun onPasswordChanged(password: String) {
            val currentState = _uiState.value
            val validation = validatePassword(password, currentState.username)
            val confirmError =
                if (currentState.passwordConfirm.isNotEmpty() && password != currentState.passwordConfirm) {
                    "동일한 비밀번호를 입력해주세요"
                } else {
                    null
                }
            _uiState.update {
                it.copy(
                    password = password,
                    errorMessage = null,
                    passwordValidation = validation,
                    passwordConfirmError = confirmError,
                )
            }
        }

        fun onPasswordConfirmChanged(passwordConfirm: String) {
            val currentState = _uiState.value
            val confirmError =
                if (passwordConfirm.isNotEmpty() && currentState.password != passwordConfirm) {
                    "동일한 비밀번호를 입력해주세요"
                } else {
                    null
                }
            _uiState.update {
                it.copy(
                    passwordConfirm = passwordConfirm,
                    errorMessage = null,
                    passwordConfirmError = confirmError,
                )
            }
        }

        fun onSignUpClicked() {
            val currentState = _uiState.value

            if (!currentState.isFormValid) return

            _uiState.update {
                it.copy(
                    errorMessage = null,
                    showAgreementBottomSheet = true,
                )
            }
        }

        fun onAgreementBottomSheetDismissed() {
            _uiState.update { it.copy(showAgreementBottomSheet = false) }
        }

        fun onAllAgreementsChanged(checked: Boolean) {
            _uiState.update {
                it.copy(
                    isServiceTermsAgreed = checked,
                    isPrivacyCollectionAgreed = checked,
                )
            }
        }

        fun onServiceTermsAgreementChanged(checked: Boolean) {
            _uiState.update { it.copy(isServiceTermsAgreed = checked) }
        }

        fun onPrivacyCollectionAgreementChanged(checked: Boolean) {
            _uiState.update { it.copy(isPrivacyCollectionAgreed = checked) }
        }

        fun onTermsDetailClicked() {
            _uiState.update { it.copy(navigationTarget = SignUpNavigationTarget.Terms) }
        }

        fun onPrivacyDetailClicked() {
            _uiState.update { it.copy(navigationTarget = SignUpNavigationTarget.Privacy) }
        }

        fun onAgreementConfirmClicked() {
            val currentState = _uiState.value

            if (!currentState.isFormValid) return
            if (!currentState.isAgreementConfirmEnabled) return

            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }

                when (val result = authRepository.signUp(currentState.username, currentState.password)) {
                    is AuthResult.LoginSuccess -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                showAgreementBottomSheet = false,
                                navigationTarget = SignUpNavigationTarget.Complete,
                            )
                        }
                    }

                    is AuthResult.SignUpSuccess -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "가입은 완료됐지만 자동 로그인에 실패했어요. 로그인 후 계속 진행해 주세요.",
                                showAgreementBottomSheet = false,
                                navigationTarget = SignUpNavigationTarget.Login,
                            )
                        }
                    }

                    is AuthResult.UsernameAlreadyExists -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null,
                                showAgreementBottomSheet = false,
                                usernameValidation =
                                    it.usernameValidation.copy(
                                        error = result.message,
                                        isAvailable = false,
                                    ),
                            )
                        }
                    }

                    is AuthResult.NetworkError -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "네트워크 오류가 발생했습니다",
                                showAgreementBottomSheet = false,
                            )
                        }
                    }

                    is AuthResult.InvalidCredentials -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null,
                                showAgreementBottomSheet = false,
                                usernameValidation =
                                    it.usernameValidation.copy(error = result.message),
                            )
                        }
                    }

                    is AuthResult.ValidationError -> {
                        val errorMsg = result.errors.values.firstOrNull() ?: "입력값을 확인해주세요"
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null,
                                showAgreementBottomSheet = false,
                                usernameValidation =
                                    it.usernameValidation.copy(error = errorMsg),
                            )
                        }
                    }
                }
            }
        }

        fun consumeNavigationTarget() {
            _uiState.update { it.copy(navigationTarget = null) }
        }

        private fun validateUsername(username: String): UsernameValidation {
            if (username.isEmpty()) {
                return UsernameValidation()
            }
            val isLengthValid = username.length >= MIN_USERNAME_LENGTH
            val isPatternValid = USERNAME_PATTERN.matches(username)
            val error =
                when {
                    !isLengthValid -> "3자 이상 입력해주세요"
                    !isPatternValid -> "잘못된 입력 형식입니다. (영문 소문자 + 숫자만 가능)"
                    else -> null
                }
            return UsernameValidation(
                error = error,
                isLengthValid = isLengthValid,
                isPatternValid = isPatternValid,
                isAvailable = null,
            )
        }

        private fun validatePassword(
            password: String,
            username: String,
        ): PasswordValidation {
            val hasLetter = PASSWORD_HAS_LETTER.containsMatchIn(password)
            val hasDigit = PASSWORD_HAS_DIGIT.containsMatchIn(password)
            val hasSpecialChar = PASSWORD_HAS_SPECIAL.containsMatchIn(password)
            val typeCount = listOf(hasLetter, hasDigit, hasSpecialChar).count { it }
            val containsUsername = username.isNotEmpty() && password.contains(username)

            return PasswordValidation(
                hasMinLength = password.length >= MIN_PASSWORD_LENGTH,
                hasTwoOrMoreTypes = typeCount >= 2,
                containsUsername = containsUsername,
            )
        }

        companion object {
            const val MIN_USERNAME_LENGTH = 3
            val USERNAME_PATTERN = Regex("^[a-z0-9]+$")

            const val MIN_PASSWORD_LENGTH = 8
            val PASSWORD_HAS_LETTER = Regex("[A-Za-z]")
            val PASSWORD_HAS_DIGIT = Regex("\\d")
            val PASSWORD_HAS_SPECIAL = Regex("[!@#\$%^&*(),.?\":{}|<>]")
        }
    }
