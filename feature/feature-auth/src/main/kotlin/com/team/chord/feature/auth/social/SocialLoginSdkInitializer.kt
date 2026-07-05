package com.team.chord.feature.auth.social

import android.content.Context
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NidOAuth
import com.navercorp.nid.core.data.datastore.NidOAuthInitializingCallback
import com.team.chord.feature.auth.BuildConfig

object SocialLoginSdkInitializer {
    private const val TAG = "SocialLoginSdk"

    fun initialize(context: Context) {
        val applicationContext = context.applicationContext
        initializeKakao(applicationContext)
        initializeNaver(applicationContext)
    }

    private fun initializeKakao(context: Context) {
        val appKey = BuildConfig.KAKAO_NATIVE_APP_KEY
        if (appKey.isBlank() || KakaoSdk.isInitialized) return

        KakaoSdk.init(context, appKey)
    }

    private fun initializeNaver(context: Context) {
        val clientId = BuildConfig.NAVER_CLIENT_ID
        val clientSecret = BuildConfig.NAVER_CLIENT_SECRET
        val clientName = BuildConfig.NAVER_CLIENT_NAME
        if (clientId.isBlank() || clientSecret.isBlank() || clientName.isBlank() || NidOAuth.isInitialized()) return

        NidOAuth.setLogEnabled(BuildConfig.DEBUG)
        NidOAuth.initialize(
            context = context,
            clientId = clientId,
            clientSecret = clientSecret,
            clientName = clientName,
            callback =
                object : NidOAuthInitializingCallback {
                    override fun onSuccess() {
                        Log.d(TAG, "Naver SDK initialized")
                    }

                    override fun onFailure(e: Exception) {
                        Log.w(TAG, "Naver SDK initialization failed: ${e.message}")
                    }
                },
        )
    }
}
