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
            _uiState.update { it.copy(username = username, errorMessage = null) }
        }

        fun onPasswordChanged(password: String) {
            _uiState.update { it.copy(password = password, errorMessage = null) }
        }

        fun onLoginClicked() {
            val currentState = _uiState.value

            if (currentState.username.isBlank()) {
                _uiState.update { it.copy(errorMessage = "아이디를 입력해주세요") }
                return
            }

            if (currentState.password.isBlank()) {
                _uiState.update { it.copy(errorMessage = "비밀번호를 입력해주세요") }
                return
            }

            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                when (val result = authRepository.signIn(currentState.username, currentState.password)) {
                    is AuthResult.LoginSuccess -> {
                        _uiState.update { it.copy(isLoading = false, isLoginSuccess = true, isSetupCompleted = result.onboardingCompleted) }
                    }

                    is AuthResult.SignUpSuccess -> {
                        // Not expected during login flow
                        _uiState.update { it.copy(isLoading = false) }
                    }

                    is AuthResult.InvalidCredentials -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                    }

                    is AuthResult.NetworkError -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "네트워크 오류가 발생했습니다") }
                    }

                    is AuthResult.UsernameAlreadyExists -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                    }

                    is AuthResult.ValidationError -> {
                        val errorMsg = result.errors.values.firstOrNull() ?: "입력값을 확인해주세요"
                        _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
                    }
                }
            }
        }

        fun consumeLoginSuccess() {
            _uiState.update { it.copy(isLoginSuccess = false) }
        }
    }
