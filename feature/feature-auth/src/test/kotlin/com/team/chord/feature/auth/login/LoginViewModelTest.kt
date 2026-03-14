package com.team.chord.feature.auth.login

import com.team.chord.core.domain.model.AuthResult
import com.team.chord.core.domain.model.AuthState
import com.team.chord.core.domain.model.AuthToken
import com.team.chord.core.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `blank username sets username error only`() {
        val viewModel = LoginViewModel(FakeLoginAuthRepository())

        viewModel.onPasswordChanged("Passw0rd!")
        viewModel.onLoginClicked()

        assertEquals("아이디를 입력해주세요", viewModel.uiState.value.usernameError)
        assertNull(viewModel.uiState.value.passwordError)
        assertNull(viewModel.uiState.value.authError)
    }

    @Test
    fun `blank password sets password error only`() {
        val viewModel = LoginViewModel(FakeLoginAuthRepository())

        viewModel.onUsernameChanged("coach01")
        viewModel.onLoginClicked()

        assertNull(viewModel.uiState.value.usernameError)
        assertEquals("비밀번호를 입력해주세요", viewModel.uiState.value.passwordError)
        assertNull(viewModel.uiState.value.authError)
    }

    @Test
    fun `validation errors map to matching fields`() = runTest {
        val viewModel =
            LoginViewModel(
                FakeLoginAuthRepository(
                    signInResult =
                        AuthResult.ValidationError(
                            errors =
                                mapOf(
                                    "loginId" to "3~20자 소문자+숫자만 가능",
                                    "password" to "비밀번호 형식을 확인해주세요",
                                ),
                        ),
                ),
            )

        fillValidCredentials(viewModel)
        viewModel.onLoginClicked()
        advanceUntilIdle()

        assertEquals("3~20자 소문자+숫자만 가능", viewModel.uiState.value.usernameError)
        assertEquals("비밀번호 형식을 확인해주세요", viewModel.uiState.value.passwordError)
        assertNull(viewModel.uiState.value.authError)
    }

    @Test
    fun `unknown username message maps to username field error`() = runTest {
        val viewModel =
            LoginViewModel(
                FakeLoginAuthRepository(
                    signInResult = AuthResult.InvalidCredentials("존재하지 않는 아이디입니다."),
                ),
            )

        fillValidCredentials(viewModel)
        viewModel.onLoginClicked()
        advanceUntilIdle()

        assertEquals("존재하지 않는 아이디입니다.", viewModel.uiState.value.usernameError)
        assertNull(viewModel.uiState.value.passwordError)
        assertNull(viewModel.uiState.value.authError)
    }

    @Test
    fun `password mismatch message maps to password field error`() = runTest {
        val viewModel =
            LoginViewModel(
                FakeLoginAuthRepository(
                    signInResult = AuthResult.InvalidCredentials("비밀번호가 일치하지 않습니다."),
                ),
            )

        fillValidCredentials(viewModel)
        viewModel.onLoginClicked()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.usernameError)
        assertEquals("비밀번호가 일치하지 않습니다.", viewModel.uiState.value.passwordError)
        assertNull(viewModel.uiState.value.authError)
    }

    @Test
    fun `generic invalid credentials maps to auth error`() = runTest {
        val viewModel =
            LoginViewModel(
                FakeLoginAuthRepository(
                    signInResult = AuthResult.InvalidCredentials("아이디 또는 비밀번호가 올바르지 않습니다"),
                ),
            )

        fillValidCredentials(viewModel)
        viewModel.onLoginClicked()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.usernameError)
        assertNull(viewModel.uiState.value.passwordError)
        assertEquals("아이디 또는 비밀번호가 올바르지 않습니다", viewModel.uiState.value.authError)
    }

    @Test
    fun `network error maps to auth error`() = runTest {
        val viewModel =
            LoginViewModel(
                FakeLoginAuthRepository(
                    signInResult = AuthResult.NetworkError(IllegalStateException("network fail")),
                ),
            )

        fillValidCredentials(viewModel)
        viewModel.onLoginClicked()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.usernameError)
        assertNull(viewModel.uiState.value.passwordError)
        assertEquals("네트워크 오류가 발생했습니다", viewModel.uiState.value.authError)
    }

    @Test
    fun `changing input clears existing login errors`() = runTest {
        val viewModel =
            LoginViewModel(
                FakeLoginAuthRepository(
                    signInResult = AuthResult.InvalidCredentials("아이디 또는 비밀번호가 올바르지 않습니다"),
                ),
            )

        fillValidCredentials(viewModel)
        viewModel.onLoginClicked()
        advanceUntilIdle()

        assertEquals("아이디 또는 비밀번호가 올바르지 않습니다", viewModel.uiState.value.authError)

        viewModel.onUsernameChanged("coach02")

        assertNull(viewModel.uiState.value.usernameError)
        assertNull(viewModel.uiState.value.passwordError)
        assertNull(viewModel.uiState.value.authError)
    }

    private fun fillValidCredentials(viewModel: LoginViewModel) {
        viewModel.onUsernameChanged("coach01")
        viewModel.onPasswordChanged("Passw0rd!")
    }
}

private class FakeLoginAuthRepository(
    private val signInResult: AuthResult =
        AuthResult.LoginSuccess(
            token = AuthToken("access", "refresh"),
            onboardingCompleted = false,
        ),
) : AuthRepository {
    override fun observeAuthState(): Flow<AuthState> = flowOf(AuthState.Unauthenticated)

    override suspend fun signIn(loginId: String, password: String): AuthResult = signInResult

    override suspend fun signUp(loginId: String, password: String): AuthResult = signInResult

    override suspend fun signOut() = Unit

    override suspend fun refreshToken(): AuthToken = AuthToken("access", "refresh")
}
