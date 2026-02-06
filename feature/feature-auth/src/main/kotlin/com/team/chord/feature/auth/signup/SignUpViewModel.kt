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
                    passwordConfirmError = confirmError,
                )
            }
        }

        fun onSignUpClicked() {
            val currentState = _uiState.value

            if (!currentState.usernameValidation.isValid) return
            if (!currentState.passwordValidation.isValid) return
            if (currentState.passwordConfirmError != null) return
            if (currentState.password != currentState.passwordConfirm) return
            if (currentState.passwordConfirm.isEmpty()) return

            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }

                when (val result = authRepository.signUp(currentState.username, currentState.password)) {
                    is AuthResult.SignUpSuccess -> {
                        _uiState.update { it.copy(isLoading = false, isSignUpSuccess = true) }
                    }

                    is AuthResult.LoginSuccess -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }

                    is AuthResult.UsernameAlreadyExists -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
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
                                usernameValidation =
                                    it.usernameValidation.copy(
                                        error = "네트워크 오류가 발생했습니다",
                                    ),
                            )
                        }
                    }

                    is AuthResult.InvalidCredentials -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
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
                                usernameValidation =
                                    it.usernameValidation.copy(error = errorMsg),
                            )
                        }
                    }
                }
            }
        }

        fun consumeSignUpSuccess() {
            _uiState.update { it.copy(isSignUpSuccess = false) }
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
