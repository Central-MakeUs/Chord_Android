package com.team.chord.feature.setting

import com.team.chord.core.domain.model.AuthState
import com.team.chord.core.domain.model.AuthToken
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.Store
import com.team.chord.core.domain.repository.AuthRepository
import com.team.chord.core.domain.repository.UserRepository
import com.team.chord.core.domain.usecase.user.GetStoreUseCase
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingViewModelTest {
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
    fun `onNotificationsEnabledChanged updates ui state`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onNotificationsEnabledChanged(false)

        assertFalse(viewModel.uiState.value.notificationsEnabled)
    }

    @Test
    fun `onNotificationsEnabledChanged can re enable notifications`() = runTest {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onNotificationsEnabledChanged(false)
        viewModel.onNotificationsEnabledChanged(true)

        assertTrue(viewModel.uiState.value.notificationsEnabled)
    }

    private fun createViewModel(): SettingViewModel =
        SettingViewModel(
            authRepository = FakeAuthRepository(),
            getStoreUseCase = GetStoreUseCase(FakeUserRepository()),
        )
}

private class FakeAuthRepository : AuthRepository {
    override fun observeAuthState(): Flow<AuthState> = flowOf(AuthState.Unauthenticated)

    override suspend fun signIn(loginId: String, password: String) =
        throw UnsupportedOperationException("Not needed for this test")

    override suspend fun signUp(loginId: String, password: String) =
        throw UnsupportedOperationException("Not needed for this test")

    override suspend fun signOut() = Unit

    override suspend fun refreshToken(): AuthToken =
        throw UnsupportedOperationException("Not needed for this test")
}

private class FakeUserRepository : UserRepository {
    override suspend fun getStore(): Result<Store> = Result.Success(
        Store(
            name = "코치카페",
            employees = 3,
            laborCost = 12000,
            rentCost = null,
            includeWeeklyHolidayPay = false,
        ),
    )

    override suspend fun deleteMe(): Result<Unit> = Result.Success(Unit)

    override suspend fun updateStore(
        name: String,
        employees: Int,
        laborCost: Int,
        rentCost: Int?,
        includeWeeklyHolidayPay: Boolean,
    ): Result<Unit> = Result.Success(Unit)
}
