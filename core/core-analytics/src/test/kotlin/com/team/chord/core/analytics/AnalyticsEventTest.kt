package com.team.chord.core.analytics

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AnalyticsEventTest {
    @Test
    fun `event names use stable snake case taxonomy`() {
        val names = listOf(
            AnalyticsEvent.AppLaunched.name,
            AnalyticsEvent.LoginScreenViewed.name,
            AnalyticsEvent.SocialLoginTapped(SocialLoginProvider.KAKAO).name,
            AnalyticsEvent.LoginSucceeded(SocialLoginProvider.NAVER).name,
            AnalyticsEvent.LoginFailed(SocialLoginProvider.KAKAO, AnalyticsErrorCategory.NETWORK).name,
            AnalyticsEvent.OnboardingStarted.name,
            AnalyticsEvent.OnboardingCompleted.name,
            AnalyticsEvent.MenuRegistrationStarted.name,
            AnalyticsEvent.MenuRegistrationCompleted.name,
            AnalyticsEvent.LogoutCompleted.name,
            AnalyticsEvent.WithdrawalRequested.name,
            AnalyticsEvent.WithdrawalCompleted.name,
        )

        assertEquals(
            listOf(
                "app_launched",
                "login_screen_viewed",
                "social_login_tapped",
                "login_succeeded",
                "login_failed",
                "onboarding_started",
                "onboarding_completed",
                "menu_registration_started",
                "menu_registration_completed",
                "logout_completed",
                "withdrawal_requested",
                "withdrawal_completed",
            ),
            names,
        )
    }

    @Test
    fun `social events contain only provider and coarse error category`() {
        val tapped = AnalyticsEvent.SocialLoginTapped(SocialLoginProvider.KAKAO)
        val failed = AnalyticsEvent.LoginFailed(SocialLoginProvider.NAVER, AnalyticsErrorCategory.SERVER_4XX)

        assertEquals(mapOf("platform" to "android", "provider" to "kakao"), tapped.properties)
        assertEquals(
            mapOf(
                "platform" to "android",
                "provider" to "naver",
                "error_category" to "server_4xx",
            ),
            failed.properties,
        )
    }

    @Test
    fun `tracked properties do not use token or personal data keys`() {
        val forbiddenFragments = listOf(
            "token",
            "accessToken",
            "refreshToken",
            "authorizationCode",
            "password",
            "email",
            "phone",
            "name",
            "clientSecret",
        ).map { it.normalized() }

        val events = listOf(
            AnalyticsEvent.AppLaunched,
            AnalyticsEvent.LoginScreenViewed,
            AnalyticsEvent.SocialLoginTapped(SocialLoginProvider.KAKAO),
            AnalyticsEvent.LoginSucceeded(SocialLoginProvider.NAVER),
            AnalyticsEvent.LoginFailed(SocialLoginProvider.KAKAO, AnalyticsErrorCategory.SDK_ERROR),
            AnalyticsEvent.OnboardingStarted,
            AnalyticsEvent.OnboardingCompleted,
            AnalyticsEvent.MenuRegistrationStarted,
            AnalyticsEvent.MenuRegistrationCompleted,
            AnalyticsEvent.LogoutCompleted,
            AnalyticsEvent.WithdrawalRequested,
            AnalyticsEvent.WithdrawalCompleted,
        )

        val keys = events.flatMap { it.properties.keys }.map { it.normalized() }

        assertFalse(keys.any { key -> forbiddenFragments.any(key::contains) })
    }

    @Test
    fun `mixpanel configuration stays disabled without token`() {
        assertFalse(MixpanelAnalyticsConfiguration(projectToken = "", isEnabled = true).shouldStart)
        assertFalse(MixpanelAnalyticsConfiguration(projectToken = "token", isEnabled = false).shouldStart)
        assertTrue(MixpanelAnalyticsConfiguration(projectToken = "token", isEnabled = true).shouldStart)
    }

    private fun String.normalized(): String = lowercase().replace("_", "").replace("-", "")
}
