package com.team.chord.feature.auth.login

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
class LoginViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(LoginUiState())
        val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

        fun onUsernameChanged(username: String) {
            _uiState.update {
                it.copy(
                    username = username,
                    usernameError = null,
                    passwordError = null,
                    authError = null,
                )
            }
        }

        fun onPasswordChanged(password: String) {
            _uiState.update {
                it.copy(
                    password = password,
                    usernameError = null,
                    passwordError = null,
                    authError = null,
                )
            }
        }

        fun onLoginClicked() {
            val currentState = _uiState.value

            if (currentState.username.isBlank()) {
                _uiState.update {
                    it.copy(
                        usernameError = "아이디를 입력해주세요",
                        passwordError = null,
                        authError = null,
                    )
                }
                return
            }

            if (currentState.password.isBlank()) {
                _uiState.update {
                    it.copy(
                        usernameError = null,
                        passwordError = "비밀번호를 입력해주세요",
                        authError = null,
                    )
                }
                return
            }

            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        usernameError = null,
                        passwordError = null,
                        authError = null,
                    )
                }

                when (val result = authRepository.signIn(currentState.username, currentState.password)) {
                    is AuthResult.LoginSuccess -> {
                        _uiState.update { it.copy(isLoading = false, isLoginSuccess = true, isSetupCompleted = result.onboardingCompleted) }
                    }

                    is AuthResult.SignUpSuccess -> {
                        // Not expected during login flow
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                usernameError = null,
                                passwordError = null,
                                authError = null,
                            )
                        }
                    }

                    is AuthResult.InvalidCredentials -> {
                        _uiState.update { it.copy(isLoading = false).withMappedMessage(result.message) }
                    }

                    is AuthResult.NetworkError -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                usernameError = null,
                                passwordError = null,
                                authError = "네트워크 오류가 발생했습니다",
                            )
                        }
                    }

                    is AuthResult.UsernameAlreadyExists -> {
                        _uiState.update { it.copy(isLoading = false).withMappedMessage(result.message) }
                    }

                    is AuthResult.ValidationError -> {
                        _uiState.update { it.copy(isLoading = false).withValidationErrors(result.errors) }
                    }
                }
            }
        }

        fun consumeLoginSuccess() {
            _uiState.update { it.copy(isLoginSuccess = false) }
        }
    }

private fun LoginUiState.withMappedMessage(message: String): LoginUiState =
    when {
        message.contains("존재하지 않는 아이디입니다") ->
            copy(
                usernameError = message,
                passwordError = null,
                authError = null,
            )

        message.contains("비밀번호가 일치하지 않습니다") ->
            copy(
                usernameError = null,
                passwordError = message,
                authError = null,
            )

        else ->
            copy(
                usernameError = null,
                passwordError = null,
                authError = message,
            )
    }

private fun LoginUiState.withValidationErrors(errors: Map<String, String>): LoginUiState {
    val usernameError = errors["loginId"]
    val passwordError = errors["password"]
    val authError =
        if (usernameError == null && passwordError == null) {
            errors.values.firstOrNull() ?: "입력값을 확인해주세요"
        } else {
            null
        }

    return copy(
        usernameError = usernameError,
        passwordError = passwordError,
        authError = authError,
    )
}
