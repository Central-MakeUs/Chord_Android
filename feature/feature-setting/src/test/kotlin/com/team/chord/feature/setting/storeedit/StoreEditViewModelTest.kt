package com.team.chord.feature.setting.storeedit

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.Store
import com.team.chord.core.domain.repository.UserRepository
import com.team.chord.core.domain.usecase.user.GetStoreUseCase
import com.team.chord.core.domain.usecase.user.UpdateStoreUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class StoreEditViewModelTest {
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
    fun `loadStoreInfo seeds previous employee count for staffed store`() = runTest {
        val repository =
            FakeUserRepository(
                store =
                    Store(
                        name = "코치카페",
                        employees = 3,
                        laborCost = 10320,
                        rentCost = null,
                        includeWeeklyHolidayPay = true,
                    ),
            )
        val viewModel = createViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("3", state.employeeCountInput)
        assertEquals("3", state.lastNonZeroEmployeeCountInput)
        assertFalse(state.ownerSolo)
        assertTrue(state.includeWeeklyHolidayPay)
    }

    @Test
    fun `owner solo store starts with empty restorable employee count`() = runTest {
        val repository =
            FakeUserRepository(
                store =
                    Store(
                        name = "코치카페",
                        employees = 0,
                        laborCost = 10320,
                        rentCost = null,
                        includeWeeklyHolidayPay = true,
                    ),
            )
        val viewModel = createViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.ownerSolo)
        assertEquals("0", state.employeeCountInput)
        assertEquals("", state.lastNonZeroEmployeeCountInput)
    }

    @Test
    fun `owner solo toggle restores previous employee count and keeps weekly allowance`() = runTest {
        val repository = FakeUserRepository()
        val viewModel = createViewModel(repository)
        advanceUntilIdle()

        viewModel.onIncludeWeeklyHolidayPayChanged(true)
        viewModel.onOwnerSoloChanged(true)

        val ownerSoloState = viewModel.uiState.value
        assertTrue(ownerSoloState.ownerSolo)
        assertEquals("0", ownerSoloState.employeeCountInput)
        assertEquals("2", ownerSoloState.lastNonZeroEmployeeCountInput)
        assertTrue(ownerSoloState.includeWeeklyHolidayPay)

        viewModel.onOwnerSoloChanged(false)

        val restoredState = viewModel.uiState.value
        assertFalse(restoredState.ownerSolo)
        assertEquals("2", restoredState.employeeCountInput)
        assertTrue(restoredState.includeWeeklyHolidayPay)
    }

    @Test
    fun `submit keeps weekly allowance value even when owner solo`() = runTest {
        val repository =
            FakeUserRepository(
                store =
                    Store(
                        name = "코치카페",
                        employees = 0,
                        laborCost = 10320,
                        rentCost = null,
                        includeWeeklyHolidayPay = true,
                    ),
            )
        val viewModel = createViewModel(repository)
        advanceUntilIdle()

        viewModel.onSubmitClicked()
        advanceUntilIdle()

        assertEquals(true, repository.lastUpdatedStore?.includeWeeklyHolidayPay)
        assertEquals(0, repository.lastUpdatedStore?.employees)
    }

    private fun createViewModel(repository: FakeUserRepository): StoreEditViewModel =
        StoreEditViewModel(
            getStoreUseCase = GetStoreUseCase(repository),
            updateStoreUseCase = UpdateStoreUseCase(repository),
        )
}

private class FakeUserRepository(
    private val store: Store =
        Store(
            name = "코치카페",
            employees = 2,
            laborCost = 10320,
            rentCost = null,
            includeWeeklyHolidayPay = false,
        ),
    private val updateResult: Result<Unit> = Result.Success(Unit),
) : UserRepository {
    var lastUpdatedStore: Store? = null

    override suspend fun getStore(): Result<Store> = Result.Success(store)

    override suspend fun deleteMe(): Result<Unit> = Result.Success(Unit)

    override suspend fun updateStore(
        name: String,
        employees: Int,
        laborCost: Int,
        rentCost: Int?,
        includeWeeklyHolidayPay: Boolean,
    ): Result<Unit> {
        lastUpdatedStore =
            Store(
                name = name,
                employees = employees,
                laborCost = laborCost,
                rentCost = rentCost,
                includeWeeklyHolidayPay = includeWeeklyHolidayPay,
            )
        return updateResult
    }
}
