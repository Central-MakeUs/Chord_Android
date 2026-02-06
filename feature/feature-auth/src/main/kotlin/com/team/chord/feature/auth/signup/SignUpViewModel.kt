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
            _uiState.update {
                it.copy(
                    username = username,
                    usernameValidation = validation,
                    errorMessage = null,
                )
            }
        }

        fun onPasswordChanged(password: String) {
            val currentState = _uiState.value
            val validation = validatePassword(password, currentState.passwordConfirm)
            _uiState.update {
                it.copy(
                    password = password,
                    passwordValidation = validation,
                    errorMessage = null,
                )
            }
        }

        fun onPasswordConfirmChanged(passwordConfirm: String) {
            val currentState = _uiState.value
            _uiState.update {
                it.copy(
                    passwordConfirm = passwordConfirm,
                    passwordValidation =
                        it.passwordValidation.copy(
                            isConfirmMatch = currentState.password == passwordConfirm && passwordConfirm.isNotEmpty(),
                        ),
                    errorMessage = null,
                )
            }
        }

        fun onTermsAgreedChanged(isAgreed: Boolean) {
            _uiState.update { it.copy(isTermsAgreed = isAgreed) }
        }

        fun onSignUpClicked() {
            val currentState = _uiState.value

            if (!currentState.usernameValidation.isValid) {
                _uiState.update { it.copy(errorMessage = "아이디를 확인해주세요") }
                return
            }

            if (!currentState.passwordValidation.isValid) {
                _uiState.update { it.copy(errorMessage = "비밀번호를 확인해주세요") }
                return
            }

            if (!currentState.isTermsAgreed) {
                _uiState.update { it.copy(errorMessage = "이용약관에 동의해주세요") }
                return
            }

            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                when (val result = authRepository.signUp(currentState.username, currentState.password)) {
                    is AuthResult.SignUpSuccess -> {
                        _uiState.update { it.copy(isLoading = false, isSignUpSuccess = true) }
                    }

                    is AuthResult.LoginSuccess -> {
                        // Not expected during sign-up flow
                        _uiState.update { it.copy(isLoading = false) }
                    }

                    is AuthResult.UsernameAlreadyExists -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message,
                                usernameValidation = it.usernameValidation.copy(isAvailable = false),
                            )
                        }
                    }

                    is AuthResult.NetworkError -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "네트워크 오류가 발생했습니다") }
                    }

                    is AuthResult.InvalidCredentials -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                    }

                    is AuthResult.ValidationError -> {
                        val errorMsg = result.errors.values.firstOrNull() ?: "입력값을 확인해주세요"
                        _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
                    }
                }
            }
        }

        fun consumeSignUpSuccess() {
            _uiState.update { it.copy(isSignUpSuccess = false) }
        }

        private fun validateUsername(username: String): UsernameValidation =
            UsernameValidation(
                isLengthValid = username.length >= MIN_USERNAME_LENGTH,
                isPatternValid = USERNAME_PATTERN.matches(username),
                isAvailable = null,
            )

        private fun validatePassword(
            password: String,
            passwordConfirm: String,
        ): PasswordValidation =
            PasswordValidation(
                hasLetter = PASSWORD_HAS_LETTER.containsMatchIn(password),
                hasDigit = PASSWORD_HAS_DIGIT.containsMatchIn(password),
                hasSpecialChar = PASSWORD_HAS_SPECIAL.containsMatchIn(password),
                hasMinLength = password.length >= MIN_PASSWORD_LENGTH,
                isConfirmMatch = password == passwordConfirm && passwordConfirm.isNotEmpty(),
            )

        companion object {
            const val MIN_USERNAME_LENGTH = 4
            val USERNAME_PATTERN = Regex("^[A-Za-z0-9]+$")

            const val MIN_PASSWORD_LENGTH = 8
            val PASSWORD_HAS_LETTER = Regex("[A-Za-z]")
            val PASSWORD_HAS_DIGIT = Regex("\\d")
            val PASSWORD_HAS_SPECIAL = Regex("[!@#\$%^&*(),.?\":{}|<>]")
        }
    }
