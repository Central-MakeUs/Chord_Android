package com.team.chord.feature.setup.storeinfo

import com.team.chord.core.domain.repository.SetupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StoreInfoViewModelTest {
    @Test
    fun `onStoreNameConfirmed moves to post store name`() {
        val viewModel = StoreInfoViewModel(FakeSetupRepository())

        viewModel.onStoreNameConfirmed()

        assertEquals(StoreInfoScreenState.PostStoreName, viewModel.uiState.value.screenState)
    }

    @Test
    fun `owner solo toggle restores previous employee count`() {
        val viewModel = StoreInfoViewModel(FakeSetupRepository())

        viewModel.onEmployeeCountChanged("5")
        viewModel.onIsOwnerSoloChanged(true)

        val ownerSoloState = viewModel.uiState.value
        assertTrue(ownerSoloState.ownerSolo)
        assertEquals("0", ownerSoloState.employeeCountInput)
        assertEquals("5", ownerSoloState.lastNonZeroEmployeeCountInput)

        viewModel.onIsOwnerSoloChanged(false)

        val restoredState = viewModel.uiState.value
        assertEquals("5", restoredState.employeeCountInput)
        assertEquals(false, restoredState.ownerSolo)
    }

    @Test
    fun `owner solo toggle without previous employee count restores empty`() {
        val viewModel = StoreInfoViewModel(FakeSetupRepository())

        viewModel.onIsOwnerSoloChanged(true)
        viewModel.onIsOwnerSoloChanged(false)

        val state = viewModel.uiState.value
        assertEquals("", state.employeeCountInput)
        assertEquals("", state.lastNonZeroEmployeeCountInput)
    }

    @Test
    fun `hourly wage input is sanitized while typing`() {
        val viewModel = StoreInfoViewModel(FakeSetupRepository())

        viewModel.onHourlyWageChanged("0010320")

        assertEquals("10320", viewModel.uiState.value.hourlyWageInput)
    }
}

private class FakeSetupRepository : SetupRepository {
    override fun isSetupCompleted(): Flow<Boolean> = flowOf(false)

    override suspend fun setSetupCompleted(isCompleted: Boolean) = Unit

    override suspend fun completeOnboarding(
        name: String,
        employees: Int,
        laborCost: Int,
        rentCost: Int?,
        includeWeeklyHolidayPay: Boolean,
    ) = Unit
}
