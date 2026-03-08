package com.team.chord.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.team.chord.core.data.datasource.AuthDataSource
import com.team.chord.core.data.datasource.LoginResult
import com.team.chord.core.data.repository.AuthRepositoryImpl
import com.team.chord.core.domain.model.AuthResult
import com.team.chord.core.domain.repository.SetupRepository
import com.team.chord.core.network.auth.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class AuthRepositoryImplInstrumentedTest {
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var dataStoreFile: File
    private lateinit var tokenManager: TokenManager

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        dataStoreFile =
            File(
                context.filesDir,
                "auth_repository_instrumented_test_${UUID.randomUUID()}.preferences_pb",
            )
        dataStoreFile.delete()
        dataStore =
            PreferenceDataStoreFactory.create(
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
                produceFile = { dataStoreFile },
            )
        tokenManager = TokenManager(dataStore)
    }

    @After
    fun tearDown() {
        runBlocking {
            tokenManager.clearTokens()
        }
        dataStoreFile.delete()
    }

    @Test
    fun signUp_success_logsInAndSavesTokens() = runTest {
        val authDataSource =
            RecordingAuthDataSource(
                loginResult =
                    LoginResult(
                        accessToken = "access-token",
                        refreshToken = "refresh-token",
                        onboardingCompleted = false,
                    ),
            )
        val setupRepository = FakeSetupRepository()
        val repository = AuthRepositoryImpl(authDataSource, tokenManager, setupRepository)

        val result = repository.signUp("coach01", "Passw0rd!")

        assertTrue(result is AuthResult.LoginSuccess)
        assertEquals(1, authDataSource.signUpCalls)
        assertEquals(1, authDataSource.loginCalls)
        assertEquals("access-token", tokenManager.getAccessToken())
        assertEquals("refresh-token", tokenManager.getRefreshToken())
        assertEquals(false, setupRepository.lastSetupCompletedValue)
    }

    @Test
    fun signUp_whenAutoLoginFails_returnsSignUpSuccessWithoutSavingTokens() = runTest {
        val authDataSource =
            RecordingAuthDataSource(
                loginFailure = IllegalStateException("auto login fail"),
            )
        val setupRepository = FakeSetupRepository()
        val repository = AuthRepositoryImpl(authDataSource, tokenManager, setupRepository)

        val result = repository.signUp("coach01", "Passw0rd!")

        assertEquals(AuthResult.SignUpSuccess, result)
        assertEquals(1, authDataSource.signUpCalls)
        assertEquals(1, authDataSource.loginCalls)
        assertNull(tokenManager.getAccessToken())
        assertNull(tokenManager.getRefreshToken())
        assertEquals(null, setupRepository.lastSetupCompletedValue)
    }
}

private class RecordingAuthDataSource(
    private val loginResult: LoginResult =
        LoginResult(
            accessToken = "access-token",
            refreshToken = "refresh-token",
            onboardingCompleted = false,
        ),
    private val loginFailure: Throwable? = null,
) : AuthDataSource {
    var signUpCalls: Int = 0
    var loginCalls: Int = 0

    override suspend fun signUp(loginId: String, password: String) {
        signUpCalls += 1
    }

    override suspend fun login(loginId: String, password: String): LoginResult {
        loginCalls += 1
        loginFailure?.let { throw it }
        return loginResult
    }

    override suspend fun refreshToken(refreshToken: String): String = "refreshed-token"
}

private class FakeSetupRepository : SetupRepository {
    var lastSetupCompletedValue: Boolean? = null

    override fun isSetupCompleted(): Flow<Boolean> = flowOf(false)

    override suspend fun setSetupCompleted(isCompleted: Boolean) {
        lastSetupCompletedValue = isCompleted
    }

    override suspend fun completeOnboarding(
        name: String,
        employees: Int,
        laborCost: Int,
        rentCost: Int?,
        includeWeeklyHolidayPay: Boolean,
    ) = Unit
}
