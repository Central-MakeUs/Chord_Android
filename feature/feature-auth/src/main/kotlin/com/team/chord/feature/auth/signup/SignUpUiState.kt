package com.team.chord.feature.auth.signup

data class SignUpUiState(
    val username: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showAgreementBottomSheet: Boolean = false,
    val isServiceTermsAgreed: Boolean = false,
    val isPrivacyCollectionAgreed: Boolean = false,
    val navigationTarget: SignUpNavigationTarget? = null,
    val usernameValidation: UsernameValidation = UsernameValidation(),
    val passwordValidation: PasswordValidation = PasswordValidation(),
    val passwordConfirmError: String? = null,
) {
    val isFormValid: Boolean
        get() =
            usernameValidation.isValid &&
                passwordValidation.isValid &&
                passwordConfirmError == null &&
                password == passwordConfirm &&
                passwordConfirm.isNotEmpty()

    val isAgreementConfirmEnabled: Boolean
        get() = isServiceTermsAgreed && isPrivacyCollectionAgreed && !isLoading

    val isAllAgreed: Boolean
        get() = isServiceTermsAgreed && isPrivacyCollectionAgreed
}

enum class SignUpNavigationTarget {
    Terms,
    Privacy,
    Complete,
    Login,
}

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
