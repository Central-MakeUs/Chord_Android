package com.team.chord.feature.setup.storeinfo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StoreInfoViewModelTest {
    @Test
    fun `onStoreNameConfirmed moves to post store name`() {
        val viewModel = StoreInfoViewModel()

        viewModel.onStoreNameConfirmed()

        assertEquals(StoreInfoScreenState.PostStoreName, viewModel.uiState.value.screenState)
    }

    @Test
    fun `onIsOwnerSoloChanged true forces employee count zero`() {
        val viewModel = StoreInfoViewModel()

        viewModel.onEmployeeCountChanged("5")
        viewModel.onIsOwnerSoloChanged(true)

        val state = viewModel.uiState.value
        assertTrue(state.ownerSolo)
        assertEquals("0", state.employeeCountInput)
        assertEquals(0, state.employeeCountValue)
    }

    @Test
    fun `onPostStoreNameNextClicked requires valid inputs`() {
        val viewModel = StoreInfoViewModel()

        viewModel.onStoreNameConfirmed()
        viewModel.onEmployeeCountChanged("0")
        viewModel.onHourlyWageChanged("1000")
        viewModel.onPostStoreNameNextClicked()

        assertEquals(StoreInfoScreenState.PostStoreName, viewModel.uiState.value.screenState)
        assertFalse(viewModel.uiState.value.isPostStoreNameNextEnabled)

        viewModel.onEmployeeCountChanged("2")
        viewModel.onHourlyWageChanged("1000")
        viewModel.onPostStoreNameNextClicked()

        val completedState = viewModel.uiState.value
        assertTrue(completedState.isPostStoreNameNextEnabled)
        assertEquals(StoreInfoScreenState.Completed, completedState.screenState)
        assertEquals(2, completedState.employeeCount)
    }
}
