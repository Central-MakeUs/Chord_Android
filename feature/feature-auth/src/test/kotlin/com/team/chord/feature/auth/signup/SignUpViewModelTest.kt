package com.team.chord.feature.auth.signup

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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpViewModelTest {
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
    fun `valid form opens agreement bottom sheet`() {
        val viewModel = SignUpViewModel(FakeAuthRepository())

        fillValidForm(viewModel)

        viewModel.onSignUpClicked()

        assertTrue(viewModel.uiState.value.showAgreementBottomSheet)
    }

    @Test
    fun `invalid form does not open agreement bottom sheet`() {
        val viewModel = SignUpViewModel(FakeAuthRepository())

        viewModel.onUsernameChanged("co")
        viewModel.onPasswordChanged("pass")
        viewModel.onPasswordConfirmChanged("pass")
        viewModel.onSignUpClicked()

        assertFalse(viewModel.uiState.value.showAgreementBottomSheet)
    }

    @Test
    fun `all agreement toggle syncs required agreements`() {
        val viewModel = SignUpViewModel(FakeAuthRepository())

        viewModel.onAllAgreementsChanged(true)
        assertTrue(viewModel.uiState.value.isServiceTermsAgreed)
        assertTrue(viewModel.uiState.value.isPrivacyCollectionAgreed)
        assertTrue(viewModel.uiState.value.isAllAgreed)

        viewModel.onPrivacyCollectionAgreementChanged(false)
        assertTrue(viewModel.uiState.value.isServiceTermsAgreed)
        assertFalse(viewModel.uiState.value.isPrivacyCollectionAgreed)
        assertFalse(viewModel.uiState.value.isAllAgreed)
    }

    @Test
    fun `terms detail keeps bottom sheet visible and emits navigation target`() {
        val viewModel = SignUpViewModel(FakeAuthRepository())

        fillValidForm(viewModel)
        viewModel.onSignUpClicked()
        viewModel.onTermsDetailClicked()

        assertTrue(viewModel.uiState.value.showAgreementBottomSheet)
        assertEquals(SignUpNavigationTarget.Terms, viewModel.uiState.value.navigationTarget)

        viewModel.consumeNavigationTarget()

        assertEquals(null, viewModel.uiState.value.navigationTarget)
    }

    @Test
    fun `agreement confirm with login success navigates to complete`() = runTest {
        val viewModel =
            SignUpViewModel(
                FakeAuthRepository(
                    signUpResult =
                        AuthResult.LoginSuccess(
                            token = AuthToken("access", "refresh"),
                            onboardingCompleted = false,
                        ),
                ),
            )

        fillValidForm(viewModel)
        viewModel.onSignUpClicked()
        viewModel.onAllAgreementsChanged(true)

        viewModel.onAgreementConfirmClicked()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.showAgreementBottomSheet)
        assertEquals(SignUpNavigationTarget.Complete, viewModel.uiState.value.navigationTarget)
    }

    @Test
    fun `agreement confirm with signup success navigates to login fallback`() = runTest {
        val viewModel =
            SignUpViewModel(
                FakeAuthRepository(
                    signUpResult = AuthResult.SignUpSuccess,
                ),
            )

        fillValidForm(viewModel)
        viewModel.onSignUpClicked()
        viewModel.onAllAgreementsChanged(true)

        viewModel.onAgreementConfirmClicked()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.showAgreementBottomSheet)
        assertEquals(SignUpNavigationTarget.Login, viewModel.uiState.value.navigationTarget)
        assertEquals(
            "가입은 완료됐지만 자동 로그인에 실패했어요. 로그인 후 계속 진행해 주세요.",
            viewModel.uiState.value.errorMessage,
        )
    }

    private fun fillValidForm(viewModel: SignUpViewModel) {
        viewModel.onUsernameChanged("coach01")
        viewModel.onPasswordChanged("Passw0rd!")
        viewModel.onPasswordConfirmChanged("Passw0rd!")
    }
}

private class FakeAuthRepository(
    private val signUpResult: AuthResult = AuthResult.SignUpSuccess,
) : AuthRepository {
    override fun observeAuthState(): Flow<AuthState> = flowOf(AuthState.Unauthenticated)

    override suspend fun signIn(loginId: String, password: String): AuthResult = signUpResult

    override suspend fun signUp(loginId: String, password: String): AuthResult = signUpResult

    override suspend fun signOut() = Unit

    override suspend fun refreshToken(): AuthToken = AuthToken("access", "refresh")
}
