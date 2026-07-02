package com.team.chord.core.analytics

import android.content.Context

typealias AnalyticsProperties = Map<String, Any>

enum class SocialLoginProvider(val value: String) {
    KAKAO("kakao"),
    NAVER("naver"),
}

enum class AnalyticsErrorCategory(val value: String) {
    VALIDATION("validation"),
    NETWORK("network"),
    SERVER_4XX("server_4xx"),
    SERVER_5XX("server_5xx"),
    SERVER_OTHER("server_other"),
    MISSING_CONFIG("missing_config"),
    INVALID_PROVIDER_RESPONSE("invalid_provider_response"),
    SDK_ERROR("sdk_error"),
    SDK_UNKNOWN("sdk_unknown"),
    UNKNOWN("unknown"),
}

sealed class AnalyticsEvent(
    val name: String,
    properties: AnalyticsProperties = emptyMap(),
) {
    val properties: AnalyticsProperties = mapOf("platform" to "android") + properties

    data object AppLaunched : AnalyticsEvent("app_launched")

    data object LoginScreenViewed : AnalyticsEvent("login_screen_viewed")

    data class SocialLoginTapped(
        val provider: SocialLoginProvider,
    ) : AnalyticsEvent(
        name = "social_login_tapped",
        properties = mapOf("provider" to provider.value),
    )

    data class LoginSucceeded(
        val provider: SocialLoginProvider,
    ) : AnalyticsEvent(
        name = "login_succeeded",
        properties = mapOf("provider" to provider.value),
    )

    data class LoginFailed(
        val provider: SocialLoginProvider,
        val errorCategory: AnalyticsErrorCategory,
    ) : AnalyticsEvent(
        name = "login_failed",
        properties = mapOf(
            "provider" to provider.value,
            "error_category" to errorCategory.value,
        ),
    )

    data object OnboardingStarted : AnalyticsEvent("onboarding_started")

    data object OnboardingCompleted : AnalyticsEvent("onboarding_completed")

    data object MenuRegistrationStarted : AnalyticsEvent("menu_registration_started")

    data object MenuRegistrationCompleted : AnalyticsEvent("menu_registration_completed")

    data object LogoutCompleted : AnalyticsEvent("logout_completed")

    data object WithdrawalRequested : AnalyticsEvent("withdrawal_requested")

    data object WithdrawalCompleted : AnalyticsEvent("withdrawal_completed")
}

interface AnalyticsClient {
    fun track(event: AnalyticsEvent)

    fun identify(userId: String)

    fun reset()

    fun flush()
}

object NoopAnalyticsClient : AnalyticsClient {
    override fun track(event: AnalyticsEvent) = Unit

    override fun identify(userId: String) = Unit

    override fun reset() = Unit

    override fun flush() = Unit
}

object Analytics {
    @Volatile
    private var client: AnalyticsClient = NoopAnalyticsClient

    fun configure(context: Context) {
        configure(
            MixpanelAnalyticsClient.create(
                context = context.applicationContext,
                configuration = MixpanelAnalyticsConfiguration(),
            ),
        )
    }

    fun configure(client: AnalyticsClient) {
        this.client = client
    }

    fun track(event: AnalyticsEvent) {
        client.track(event)
    }

    fun identify(userId: String) {
        if (userId.isNotBlank()) {
            client.identify(userId)
        }
    }

    fun reset() {
        client.reset()
    }

    fun flush() {
        client.flush()
    }
}
