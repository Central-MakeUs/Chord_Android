package com.team.chord.feature.setup.menudetail

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Test

class MenuDetailViewModelTest {

    @Test
    fun `init decodes encoded menu name`() {
        val viewModel = MenuDetailViewModel(
            SavedStateHandle(
                mapOf(
                    "menuName" to "아이스+아메리카노",
                    "isTemplateApplied" to false,
                ),
            ),
        )

        assertEquals("아이스 아메리카노", viewModel.uiState.value.menuName)
    }

    @Test
    fun `init hydrates template price and work time`() {
        val viewModel = MenuDetailViewModel(
            SavedStateHandle(
                mapOf(
                    "menuName" to "카페라떼",
                    "isTemplateApplied" to true,
                    "templatePrice" to 5800,
                    "templateWorkTime" to 125,
                ),
            ),
        )

        with(viewModel.uiState.value) {
            assertEquals("5800", price)
            assertEquals(2, preparationMinutes)
            assertEquals(5, preparationSeconds)
        }
    }
}
