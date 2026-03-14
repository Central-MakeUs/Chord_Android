package com.team.chord.feature.auth.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val authError: String? = null,
    val isLoginSuccess: Boolean = false,
    val isSetupCompleted: Boolean = false,
)
