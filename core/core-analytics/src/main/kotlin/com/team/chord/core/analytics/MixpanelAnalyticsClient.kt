package com.team.chord.core.analytics

import android.content.Context
import android.util.Log
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject

data class MixpanelAnalyticsConfiguration(
    val projectToken: String = BuildConfig.MIXPANEL_PROJECT_TOKEN,
    val isEnabled: Boolean = BuildConfig.MIXPANEL_ENABLED,
    val loggingEnabled: Boolean = BuildConfig.MIXPANEL_LOGGING_ENABLED,
) {
    val shouldStart: Boolean = isEnabled && projectToken.isNotBlank()
}

class MixpanelAnalyticsClient private constructor(
    private val mixpanel: MixpanelAPI,
    private val loggingEnabled: Boolean,
) : AnalyticsClient {
    override fun track(event: AnalyticsEvent) {
        if (loggingEnabled) {
            Log.d(TAG, "Track ${event.name}: ${event.properties}")
        }
        mixpanel.track(event.name, event.properties.toJsonObject())
    }

    override fun identify(userId: String) {
        mixpanel.identify(userId)
    }

    override fun reset() {
        mixpanel.reset()
    }

    override fun flush() {
        mixpanel.flush()
    }

    companion object {
        private const val TAG = "CoreAnalytics"

        fun create(
            context: Context,
            configuration: MixpanelAnalyticsConfiguration = MixpanelAnalyticsConfiguration(),
        ): AnalyticsClient {
            if (!configuration.shouldStart) {
                if (configuration.loggingEnabled) {
                    Log.d(TAG, "Mixpanel disabled or project token missing; using no-op analytics")
                }
                return NoopAnalyticsClient
            }

            return MixpanelAnalyticsClient(
                mixpanel = MixpanelAPI.getInstance(context, configuration.projectToken, false),
                loggingEnabled = configuration.loggingEnabled,
            )
        }
    }
}

private fun AnalyticsProperties.toJsonObject(): JSONObject =
    JSONObject().also { json ->
        forEach { (key, value) ->
            json.put(key, value)
        }
    }
