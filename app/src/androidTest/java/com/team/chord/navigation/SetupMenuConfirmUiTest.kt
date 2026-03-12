package com.team.chord.navigation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.team.chord.core.domain.model.Result
import com.team.chord.feature.setup.menuconfirm.IngredientSummary
import com.team.chord.feature.setup.menuconfirm.MenuConfirmScreen
import com.team.chord.feature.setup.menuconfirm.RegisteredMenuSummary
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import org.junit.Rule
import org.junit.Test

class SetupMenuConfirmUiTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun menuConfirmScreen_rendersPrimaryMenuSummary() {
        composeRule.setContent {
            MenuConfirmScreen(
                registeredMenus = listOf(sampleMenuSummary),
                onNavigateBack = {},
                onComplete = {},
                onRegisterMenus = { Result.Success(Unit) },
            )
        }

        composeRule.onNodeWithText("이대로 등록을 마칠까요?").assertIsDisplayed()
        composeRule.onNodeWithText("흑임자 라떼").assertIsDisplayed()
        composeRule.onNodeWithText("6,500원").assertIsDisplayed()
        composeRule.onNodeWithText("흑임자 토핑").assertIsDisplayed()
        composeRule.onNodeWithText("20g").assertIsDisplayed()
        composeRule.onNodeWithText("5,000원").assertIsDisplayed()
        composeRule.onNodeWithText("마치기").assertIsDisplayed()
    }

    @Test
    fun menuConfirmScreen_disablesFinishButtonWithoutMenu() {
        composeRule.setContent {
            MenuConfirmScreen(
                registeredMenus = emptyList(),
                onNavigateBack = {},
                onComplete = {},
                onRegisterMenus = { Result.Success(Unit) },
            )
        }

        composeRule.onNodeWithText("마치기").assertIsNotEnabled()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun menuConfirmScreen_disablesFinishButtonWhileRegistering() {
        val pendingResult = CompletableDeferred<Result<Unit>>()

        composeRule.setContent {
            MenuConfirmScreen(
                registeredMenus = listOf(sampleMenuSummary),
                onNavigateBack = {},
                onComplete = {},
                onRegisterMenus = { pendingResult.await() },
            )
        }

        composeRule.onNodeWithText("마치기").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("마치기").assertIsNotEnabled()

        pendingResult.cancel()
    }

    private companion object {
        val sampleMenuSummary = RegisteredMenuSummary(
            index = 1,
            name = "흑임자 라떼",
            price = 6500,
            ingredients = listOf(
                IngredientSummary(name = "흑임자 토핑", amount = "20g", price = 5000),
                IngredientSummary(name = "물", amount = "250ml", price = 2000),
            ),
        )
    }
}
