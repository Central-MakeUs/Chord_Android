package com.team.chord.feature.auth.social

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NidOAuth
import com.navercorp.nid.oauth.util.NidOAuthCallback
import com.team.chord.feature.auth.BuildConfig

class AndroidSocialLoginClient {
    fun loginWithKakao(
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onCancelled: () -> Unit,
    ) {
        if (BuildConfig.KAKAO_NATIVE_APP_KEY.isBlank()) {
            onError("KAKAO_NATIVE_APP_KEY 설정이 필요합니다.")
            return
        }

        SocialLoginSdkInitializer.initialize(context)

        val accountLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            handleKakaoResult(
                token = token,
                error = error,
                onSuccess = onSuccess,
                onError = onError,
                onCancelled = onCancelled,
            )
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    onCancelled()
                    return@loginWithKakaoTalk
                }

                if (error != null) {
                    Log.w(TAG, "KakaoTalk login failed; fallback to Kakao account: ${error.message}")
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = accountLoginCallback)
                    return@loginWithKakaoTalk
                }

                handleKakaoResult(
                    token = token,
                    error = null,
                    onSuccess = onSuccess,
                    onError = onError,
                    onCancelled = onCancelled,
                )
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = accountLoginCallback)
        }
    }

    fun loginWithNaver(
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onCancelled: () -> Unit,
    ) {
        if (BuildConfig.NAVER_CLIENT_ID.isBlank()) {
            onError("NAVER_CLIENT_ID 설정이 필요합니다.")
            return
        }
        if (BuildConfig.NAVER_CLIENT_SECRET.isBlank()) {
            onError("NAVER_CLIENT_SECRET 설정이 필요합니다.")
            return
        }

        SocialLoginSdkInitializer.initialize(context)
        NidOAuth.requestLogin(
            context,
            object : NidOAuthCallback {
                override fun onSuccess() {
                    val accessToken = NidOAuth.getAccessToken()
                    if (accessToken.isNullOrBlank()) {
                        onError("네이버 access token을 가져오지 못했습니다.")
                    } else {
                        onSuccess(accessToken)
                    }
                }

                override fun onFailure(errorCode: String, errorDesc: String) {
                    if (isNaverUserCancel(errorCode, errorDesc)) {
                        Log.d(TAG, "Naver login cancelled: code=$errorCode desc=$errorDesc")
                        onCancelled()
                    } else {
                        Log.w(TAG, "Naver login failed: code=$errorCode desc=$errorDesc")
                        onError(errorDesc.ifBlank { "네이버 로그인에 실패했습니다." })
                    }
                }
            },
        )
    }

    private fun handleKakaoResult(
        token: OAuthToken?,
        error: Throwable?,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onCancelled: () -> Unit,
    ) {
        when {
            error is ClientError && error.reason == ClientErrorCause.Cancelled -> {
                onCancelled()
            }

            error != null -> {
                onError(error.message ?: "카카오 로그인에 실패했습니다.")
            }

            token?.accessToken.isNullOrBlank() -> {
                onError("카카오 access token을 가져오지 못했습니다.")
            }

            else -> {
                onSuccess(token.accessToken)
            }
        }
    }

    private companion object {
        const val TAG = "SocialLoginClient"
        const val NAVER_USER_CANCEL_ERROR_CODE = "user_cancel"
        const val NAVER_USER_CANCEL_ERROR_NAME = "CLIENT_USER_CANCEL"

        fun isNaverUserCancel(errorCode: String, errorDesc: String): Boolean =
            errorCode == NAVER_USER_CANCEL_ERROR_CODE ||
                errorCode == NAVER_USER_CANCEL_ERROR_NAME ||
                errorDesc == NAVER_USER_CANCEL_ERROR_CODE
    }
}
