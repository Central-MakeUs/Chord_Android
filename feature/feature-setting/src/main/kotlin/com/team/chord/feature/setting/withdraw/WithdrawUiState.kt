package com.team.chord.feature.setting.withdraw

data class WithdrawUiState(
    val showConfirmDialog: Boolean = false,
    val isSubmitting: Boolean = false,
    val deleteSuccess: Boolean = false,
    val errorMessage: String? = null,
)
