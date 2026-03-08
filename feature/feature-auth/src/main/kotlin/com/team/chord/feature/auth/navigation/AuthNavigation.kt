package com.team.chord.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.team.chord.feature.auth.login.LoginScreen
import com.team.chord.feature.auth.signup.SignUpCompleteScreen
import com.team.chord.feature.auth.signup.SignUpPrivacyScreen
import com.team.chord.feature.auth.signup.SignUpScreen
import com.team.chord.feature.auth.signup.SignUpTermsScreen

const val LOGIN_ROUTE = "login"
const val SIGNUP_ROUTE = "signup"
const val SIGNUP_TERMS_ROUTE = "signup/terms"
const val SIGNUP_PRIVACY_ROUTE = "signup/privacy"
const val SIGNUP_COMPLETE_ROUTE = "signup/complete"

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    navigate(LOGIN_ROUTE, navOptions)
}

fun NavController.navigateToSignUp(navOptions: NavOptions? = null) {
    navigate(SIGNUP_ROUTE, navOptions)
}

fun NavController.navigateToSignUpComplete(navOptions: NavOptions? = null) {
    navigate(SIGNUP_COMPLETE_ROUTE, navOptions)
}

fun NavController.navigateToSignUpTerms(navOptions: NavOptions? = null) {
    navigate(SIGNUP_TERMS_ROUTE, navOptions)
}

fun NavController.navigateToSignUpPrivacy(navOptions: NavOptions? = null) {
    navigate(SIGNUP_PRIVACY_ROUTE, navOptions)
}

fun isSignUpRoute(route: String?): Boolean = route?.startsWith("signup") == true

fun NavGraphBuilder.loginScreen(
    onLoginSuccess: (Boolean) -> Unit,
    onNavigateToSignUp: () -> Unit,
) {
    composable(route = LOGIN_ROUTE) {
        LoginScreen(
            onLoginSuccess = onLoginSuccess,
            onNavigateToSignUp = onNavigateToSignUp,
        )
    }
}

fun NavGraphBuilder.signUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToLoginFallback: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    composable(route = SIGNUP_ROUTE) {
        SignUpScreen(
            onSignUpSuccess = onSignUpSuccess,
            onNavigateToTerms = onNavigateToTerms,
            onNavigateToPrivacy = onNavigateToPrivacy,
            onNavigateToLoginFallback = onNavigateToLoginFallback,
            onNavigateBack = onNavigateBack,
        )
    }
}

fun NavGraphBuilder.signUpTermsScreen(onNavigateBack: () -> Unit) {
    composable(route = SIGNUP_TERMS_ROUTE) {
        SignUpTermsScreen(onNavigateBack = onNavigateBack)
    }
}

fun NavGraphBuilder.signUpPrivacyScreen(onNavigateBack: () -> Unit) {
    composable(route = SIGNUP_PRIVACY_ROUTE) {
        SignUpPrivacyScreen(onNavigateBack = onNavigateBack)
    }
}

fun NavGraphBuilder.signUpCompleteScreen(onNavigateToSetup: () -> Unit) {
    composable(route = SIGNUP_COMPLETE_ROUTE) {
        SignUpCompleteScreen(
            onNavigateToSetup = onNavigateToSetup,
        )
    }
}
