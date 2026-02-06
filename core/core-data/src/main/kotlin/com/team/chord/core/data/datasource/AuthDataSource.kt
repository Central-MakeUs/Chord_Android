package com.team.chord.core.data.datasource

data class LoginResult(
    val accessToken: String,
    val refreshToken: String,
    val onboardingCompleted: Boolean,
)

interface AuthDataSource {
    suspend fun signUp(loginId: String, password: String)
    suspend fun login(loginId: String, password: String): LoginResult
    suspend fun refreshToken(refreshToken: String): String
}
