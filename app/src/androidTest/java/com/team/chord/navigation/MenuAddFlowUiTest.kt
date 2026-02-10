package com.team.chord.navigation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.team.chord.core.domain.model.Result
import com.team.chord.feature.menu.add.complete.MenuAddCompleteScreen
import com.team.chord.feature.menu.add.confirm.IngredientSummary
import com.team.chord.feature.menu.add.confirm.MenuAddConfirmScreen
import com.team.chord.feature.menu.add.confirm.RegisteredMenuSummary
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MenuAddFlowUiTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun menuAddConfirmScreen_rendersPrimaryActions() {
        composeRule.setContent {
            MenuAddConfirmScreen(
                registeredMenus = listOf(
                    RegisteredMenuSummary(
                        index = 1,
                        name = "흑임자 라떼",
                        price = 6500,
                        ingredients = listOf(
                            IngredientSummary(name = "원두", amount = "30g", price = 1200),
                        ),
                    ),
                ),
                onNavigateBack = {},
                onAddAnother = {},
                onRegisterSuccess = {},
                onRegisterMenus = { Result.Success(Unit) },
            )
        }

        composeRule.onNodeWithText("이대로 등록을 마칠까요?").assertIsDisplayed()
        composeRule.onNodeWithText("이전").assertIsDisplayed()
        composeRule.onNodeWithText("추가 등록").assertIsDisplayed()
        composeRule.onNodeWithText("마치기").assertIsDisplayed()
    }

    @Test
    fun menuAddCompleteScreen_callsAutoFinishAfterDelay() {
        var called = false

        composeRule.mainClock.autoAdvance = false
        composeRule.setContent {
            MenuAddCompleteScreen(
                onAutoFinish = { called = true },
            )
        }

        composeRule.onNodeWithText("메뉴 등록 완료").assertIsDisplayed()
        assertTrue(!called)

        composeRule.mainClock.advanceTimeBy(1100)
        composeRule.waitForIdle()

        assertTrue(called)
    }
}
