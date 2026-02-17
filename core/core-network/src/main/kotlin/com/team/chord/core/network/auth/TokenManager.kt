package com.team.chord.core.network.auth

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    suspend fun getAccessToken(): String? =
        dataStore.data.first()[KEY_ACCESS_TOKEN]?.let {
            runCatching { decryptIfNeeded(it) }.getOrNull()
        }

    suspend fun getRefreshToken(): String? =
        dataStore.data.first()[KEY_REFRESH_TOKEN]?.let {
            runCatching { decryptIfNeeded(it) }.getOrNull()
        }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        val encryptedAccessToken = encrypt(accessToken)
        val encryptedRefreshToken = encrypt(refreshToken)

        dataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN] = encryptedAccessToken
            prefs[KEY_REFRESH_TOKEN] = encryptedRefreshToken
        }
    }

    suspend fun clearTokens() {
        dataStore.edit { prefs ->
            prefs.remove(KEY_ACCESS_TOKEN)
            prefs.remove(KEY_REFRESH_TOKEN)
        }
    }

    fun observeAccessToken(): Flow<String?> =
        dataStore.data.map { prefs ->
            prefs[KEY_ACCESS_TOKEN]?.let {
                runCatching { decryptIfNeeded(it) }.getOrNull()
            }
        }

    private fun encrypt(rawValue: String): String {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey())

        val iv = Base64.encodeToString(cipher.iv, Base64.NO_WRAP)
        val encryptedBytes = Base64.encodeToString(
            cipher.doFinal(rawValue.toByteArray(Charsets.UTF_8)),
            Base64.NO_WRAP,
        )

        return "$ENCRYPTED_PREFIX$VALUE_SEPARATOR$iv$VALUE_SEPARATOR$encryptedBytes"
    }

    private fun decryptIfNeeded(storedValue: String): String {
        if (!storedValue.startsWith("$ENCRYPTED_PREFIX$VALUE_SEPARATOR")) {
            return storedValue
        }

        val parts = storedValue.split(VALUE_SEPARATOR, limit = 3)
        if (parts.size != 3 || parts[0] != ENCRYPTED_PREFIX) {
            return storedValue
        }

        val iv = Base64.decode(parts[1], Base64.NO_WRAP)
        val encryptedBytes = Base64.decode(parts[2], Base64.NO_WRAP)

        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(
            Cipher.DECRYPT_MODE,
            getOrCreateSecretKey(),
            GCMParameterSpec(AUTH_TAG_LENGTH, iv),
        )

        return String(cipher.doFinal(encryptedBytes), Charsets.UTF_8)
    }

    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
            load(null)
        }

        (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)?.let { return it }

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val keySpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGenerator.init(keySpec)
        return keyGenerator.generateKey()
    }

    private companion object {
        val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")

        const val CIPHER_TRANSFORMATION = "AES/GCM/NoPadding"
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val KEY_ALIAS = "chord_token_key"
        const val ENCRYPTED_PREFIX = "v1"
        const val VALUE_SEPARATOR = ":"
        const val AUTH_TAG_LENGTH = 128
    }
}
