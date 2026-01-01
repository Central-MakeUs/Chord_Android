package com.team.chord.core.domain.repository

import com.team.chord.core.domain.model.AuthResult
import com.team.chord.core.domain.model.AuthState
import com.team.chord.core.domain.model.AuthToken
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeAuthState(): Flow<AuthState>

    suspend fun signIn(
        username: String,
        password: String,
    ): AuthResult

    suspend fun signUp(
        username: String,
        password: String,
    ): AuthResult

    suspend fun signOut()

    suspend fun refreshToken(): Result<AuthToken>

    suspend fun isUsernameAvailable(username: String): Boolean
}
