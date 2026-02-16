package com.team.chord.core.data.repository

import com.team.chord.core.data.datasource.AuthDataSource
import com.team.chord.core.domain.model.AuthResult
import com.team.chord.core.domain.model.AuthState
import com.team.chord.core.domain.model.AuthToken
import com.team.chord.core.domain.repository.AuthRepository
import com.team.chord.core.domain.repository.SetupRepository
import com.team.chord.core.network.auth.TokenManager
import com.team.chord.core.network.model.ApiException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val tokenManager: TokenManager,
    private val setupRepository: SetupRepository,
) : AuthRepository {

    override fun observeAuthState(): Flow<AuthState> =
        tokenManager.observeAccessToken().map { token ->
            if (token != null) AuthState.Authenticated else AuthState.Unauthenticated
        }

    override suspend fun signIn(loginId: String, password: String): AuthResult {
        return try {
            val result = authDataSource.login(loginId, password)
            setupRepository.setSetupCompleted(result.onboardingCompleted)
            tokenManager.saveTokens(result.accessToken, result.refreshToken)
            AuthResult.LoginSuccess(
                token = AuthToken(
                    accessToken = result.accessToken,
                    refreshToken = result.refreshToken,
                ),
                onboardingCompleted = result.onboardingCompleted,
            )
        } catch (e: ApiException) {
            val errors = e.errors
            when {
                errors != null -> AuthResult.ValidationError(errors)
                else -> AuthResult.InvalidCredentials(message = e.message)
            }
        } catch (e: Exception) {
            AuthResult.NetworkError(e)
        }
    }

    override suspend fun signUp(loginId: String, password: String): AuthResult {
        return try {
            authDataSource.signUp(loginId, password)
            AuthResult.SignUpSuccess
        } catch (e: ApiException) {
            val errors = e.errors
            when {
                errors != null -> AuthResult.ValidationError(errors)
                e.code == "USER_001" -> AuthResult.UsernameAlreadyExists(message = e.message)
                else -> AuthResult.InvalidCredentials(message = e.message)
            }
        } catch (e: Exception) {
            AuthResult.NetworkError(e)
        }
    }

    override suspend fun signOut() {
        tokenManager.clearTokens()
    }

    override suspend fun refreshToken(): AuthToken {
        val currentRefreshToken = tokenManager.getRefreshToken()
            ?: throw IllegalStateException("No refresh token available")
        val newAccessToken = authDataSource.refreshToken(currentRefreshToken)
        tokenManager.saveTokens(newAccessToken, currentRefreshToken)
        return AuthToken(
            accessToken = newAccessToken,
            refreshToken = currentRefreshToken,
        )
    }
}
