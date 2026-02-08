package com.team.chord.feature.setting

data class SettingUiState(
    val storeName: String = "코치카페",
    val employeeCount: Int = 3,
    val laborCost: String = "12,000원",
    val subscriptionStatus: String = "요금제 구독중",
    val notificationEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val logoutSuccess: Boolean = false,
)
