package com.team.chord.feature.auth.signup

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@Ignore("Compose UI instrumentation is unstable on the available Android 16 preview emulator; keep compile coverage and local enabling on stable API.")
class SignUpUiTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun agreementBottomSheet_confirmButtonEnabledOnlyWhenRequiredAgreementsChecked() {
        composeRule.setContent {
            SignUpAgreementBottomSheet(
                allAgreed = false,
                serviceTermsAgreed = false,
                privacyCollectionAgreed = false,
                isLoading = false,
                onDismiss = {},
                onAllAgreedChange = {},
                onServiceTermsChange = {},
                onPrivacyCollectionChange = {},
                onTermsDetailClick = {},
                onPrivacyDetailClick = {},
                onConfirmClick = {},
            )
        }

        composeRule.onNodeWithText("서비스 이용약관에\n동의해주세요").assertIsDisplayed()
        composeRule.onNodeWithText("확인").assertIsNotEnabled()

        composeRule.setContent {
            SignUpAgreementBottomSheet(
                allAgreed = true,
                serviceTermsAgreed = true,
                privacyCollectionAgreed = true,
                isLoading = false,
                onDismiss = {},
                onAllAgreedChange = {},
                onServiceTermsChange = {},
                onPrivacyCollectionChange = {},
                onTermsDetailClick = {},
                onPrivacyDetailClick = {},
                onConfirmClick = {},
            )
        }

        composeRule.onNodeWithText("확인").assertIsEnabled()
    }

    @Test
    fun signUpCompleteScreen_callsAutoNavigationAfterDelay() {
        var called = false

        composeRule.mainClock.autoAdvance = false
        composeRule.setContent {
            SignUpCompleteScreen(
                onNavigateToSetup = { called = true },
            )
        }

        composeRule.onNodeWithText("가입이 완료됐어요").assertIsDisplayed()
        assertFalse(called)

        composeRule.mainClock.advanceTimeBy(1100)
        composeRule.waitForIdle()

        assertTrue(called)
    }
}
