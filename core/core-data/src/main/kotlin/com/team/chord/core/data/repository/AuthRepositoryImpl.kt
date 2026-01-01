package com.team.chord.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.team.chord.core.domain.model.AuthResult
import com.team.chord.core.domain.model.AuthState
import com.team.chord.core.domain.model.AuthToken
import com.team.chord.core.domain.model.User
import com.team.chord.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : AuthRepository {
        override fun observeAuthState(): Flow<AuthState> =
            dataStore.data.map { preferences ->
                val accessToken = preferences[KEY_ACCESS_TOKEN]
                val userId = preferences[KEY_USER_ID]
                val username = preferences[KEY_USERNAME]
                val createdAt = preferences[KEY_CREATED_AT]

                if (accessToken != null && userId != null && username != null && createdAt != null) {
                    AuthState.Authenticated(
                        user =
                            User(
                                id = userId,
                                username = username,
                                createdAt = createdAt,
                            ),
                    )
                } else {
                    AuthState.Unauthenticated
                }
            }

        override suspend fun signIn(
            username: String,
            password: String,
        ): AuthResult {
            val preferences = dataStore.data.first()
            val storedUsername = preferences[KEY_STORED_USERNAME]
            val storedPassword = preferences[KEY_STORED_PASSWORD]

            if (storedUsername == null || storedPassword == null) {
                return AuthResult.InvalidCredentials()
            }

            if (username != storedUsername || password != storedPassword) {
                return AuthResult.InvalidCredentials()
            }

            val user =
                User(
                    id = preferences[KEY_USER_ID] ?: UUID.randomUUID().toString(),
                    username = username,
                    createdAt = preferences[KEY_CREATED_AT] ?: System.currentTimeMillis(),
                )

            val token = generateToken()

            dataStore.edit { prefs ->
                prefs[KEY_ACCESS_TOKEN] = token.accessToken
                prefs[KEY_REFRESH_TOKEN] = token.refreshToken
                prefs[KEY_TOKEN_EXPIRES_AT] = token.expiresAt
                prefs[KEY_USER_ID] = user.id
                prefs[KEY_USERNAME] = user.username
                prefs[KEY_CREATED_AT] = user.createdAt
            }

            return AuthResult.Success(user = user, token = token)
        }

        override suspend fun signUp(
            username: String,
            password: String,
        ): AuthResult {
            val preferences = dataStore.data.first()
            val storedUsername = preferences[KEY_STORED_USERNAME]

            if (storedUsername == username) {
                return AuthResult.UsernameAlreadyExists()
            }

            val userId = UUID.randomUUID().toString()
            val createdAt = System.currentTimeMillis()
            val token = generateToken()

            dataStore.edit { prefs ->
                prefs[KEY_STORED_USERNAME] = username
                prefs[KEY_STORED_PASSWORD] = password
                prefs[KEY_ACCESS_TOKEN] = token.accessToken
                prefs[KEY_REFRESH_TOKEN] = token.refreshToken
                prefs[KEY_TOKEN_EXPIRES_AT] = token.expiresAt
                prefs[KEY_USER_ID] = userId
                prefs[KEY_USERNAME] = username
                prefs[KEY_CREATED_AT] = createdAt
            }

            val user =
                User(
                    id = userId,
                    username = username,
                    createdAt = createdAt,
                )

            return AuthResult.Success(user = user, token = token)
        }

        override suspend fun signOut() {
            dataStore.edit { prefs ->
                prefs.remove(KEY_ACCESS_TOKEN)
                prefs.remove(KEY_REFRESH_TOKEN)
                prefs.remove(KEY_TOKEN_EXPIRES_AT)
                prefs.remove(KEY_USER_ID)
                prefs.remove(KEY_USERNAME)
                prefs.remove(KEY_CREATED_AT)
            }
        }

        override suspend fun refreshToken(): Result<AuthToken> {
            val preferences = dataStore.data.first()
            val refreshToken =
                preferences[KEY_REFRESH_TOKEN]
                    ?: return Result.failure(IllegalStateException("No refresh token"))

            val newToken = generateToken()

            dataStore.edit { prefs ->
                prefs[KEY_ACCESS_TOKEN] = newToken.accessToken
                prefs[KEY_REFRESH_TOKEN] = newToken.refreshToken
                prefs[KEY_TOKEN_EXPIRES_AT] = newToken.expiresAt
            }

            return Result.success(newToken)
        }

        override suspend fun isUsernameAvailable(username: String): Boolean {
            val preferences = dataStore.data.first()
            val storedUsername = preferences[KEY_STORED_USERNAME]
            return storedUsername != username
        }

        private fun generateToken(): AuthToken =
            AuthToken(
                accessToken = UUID.randomUUID().toString(),
                refreshToken = UUID.randomUUID().toString(),
                expiresAt = System.currentTimeMillis() + TOKEN_EXPIRATION_MS,
            )

        private companion object {
            const val TOKEN_EXPIRATION_MS = 24 * 60 * 60 * 1000L

            val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
            val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
            val KEY_TOKEN_EXPIRES_AT = longPreferencesKey("token_expires_at")
            val KEY_USER_ID = stringPreferencesKey("user_id")
            val KEY_USERNAME = stringPreferencesKey("username")
            val KEY_CREATED_AT = longPreferencesKey("created_at")

            val KEY_STORED_USERNAME = stringPreferencesKey("stored_username")
            val KEY_STORED_PASSWORD = stringPreferencesKey("stored_password")
        }
    }
