package com.team.chord

import android.app.Application
import com.team.chord.core.analytics.Analytics
import com.team.chord.core.analytics.AnalyticsEvent
import com.team.chord.feature.auth.social.SocialLoginSdkInitializer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SocialLoginSdkInitializer.initialize(this)
        Analytics.configure(this)
        Analytics.track(AnalyticsEvent.AppLaunched)
    }
}
