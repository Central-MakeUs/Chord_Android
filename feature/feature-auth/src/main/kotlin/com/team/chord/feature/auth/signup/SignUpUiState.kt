package com.team.chord.feature.auth.signup

data class SignUpUiState(
    val username: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val isLoading: Boolean = false,
    val isSignUpSuccess: Boolean = false,
    val usernameValidation: UsernameValidation = UsernameValidation(),
    val passwordValidation: PasswordValidation = PasswordValidation(),
    val passwordConfirmError: String? = null,
)

data class UsernameValidation(
    val error: String? = null,
    val isLengthValid: Boolean = false,
    val isPatternValid: Boolean = false,
    val isAvailable: Boolean? = null,
) {
    val isValid: Boolean
        get() = isLengthValid && isPatternValid && error == null
}

data class PasswordValidation(
    val hasMinLength: Boolean = false,
    val hasTwoOrMoreTypes: Boolean = false,
    val containsUsername: Boolean = false,
) {
    val isValid: Boolean
        get() = hasMinLength && hasTwoOrMoreTypes && !containsUsername
}
