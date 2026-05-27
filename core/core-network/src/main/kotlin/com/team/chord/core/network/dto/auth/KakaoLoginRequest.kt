package com.team.chord.core.network.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class KakaoLoginRequest(
    val accessToken: String,
    val fcmToken: String? = null,
    val deviceType: String? = DEVICE_TYPE_ANDROID,
    val deviceId: String? = null,
) {
    private companion object {
        const val DEVICE_TYPE_ANDROID = "ANDROID"
    }
}
