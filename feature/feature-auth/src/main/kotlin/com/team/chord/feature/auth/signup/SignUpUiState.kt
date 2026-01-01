package com.team.chord.feature.auth.signup

data class SignUpUiState(
    val username: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val isTermsAgreed: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSignUpSuccess: Boolean = false,
    val usernameValidation: UsernameValidation = UsernameValidation(),
    val passwordValidation: PasswordValidation = PasswordValidation(),
)

data class UsernameValidation(
    val isLengthValid: Boolean = false,
    val isPatternValid: Boolean = false,
    val isAvailable: Boolean? = null,
) {
    val isValid: Boolean
        get() = isLengthValid && isPatternValid && isAvailable == true
}

data class PasswordValidation(
    val hasLetter: Boolean = false,
    val hasDigit: Boolean = false,
    val hasSpecialChar: Boolean = false,
    val hasMinLength: Boolean = false,
    val isConfirmMatch: Boolean = false,
) {
    val isValid: Boolean
        get() = hasLetter && hasDigit && hasSpecialChar && hasMinLength && isConfirmMatch
}
