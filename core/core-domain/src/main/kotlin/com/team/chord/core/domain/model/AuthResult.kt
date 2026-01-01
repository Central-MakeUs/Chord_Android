package com.team.chord.core.domain.model

sealed interface AuthResult {
    data class Success(
        val user: User,
        val token: AuthToken,
    ) : AuthResult

    data class InvalidCredentials(
        val message: String = "아이디 또는 비밀번호가 올바르지 않습니다",
    ) : AuthResult

    data class UsernameAlreadyExists(
        val message: String = "이미 사용 중인 아이디입니다",
    ) : AuthResult

    data class NetworkError(
        val exception: Throwable,
    ) : AuthResult

    data class ValidationError(
        val errors: Map<String, String>,
    ) : AuthResult
}

sealed interface AuthState {
    data object Loading : AuthState

    data class Authenticated(
        val user: User,
    ) : AuthState

    data object Unauthenticated : AuthState
}
