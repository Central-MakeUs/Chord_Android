package com.team.chord.feature.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.team.chord.feature.auth.login.LoginScreen
import com.team.chord.feature.auth.signup.SignUpCompleteScreen
import com.team.chord.feature.auth.signup.SignUpScreen

const val LOGIN_ROUTE = "login"
const val SIGNUP_ROUTE = "signup"
const val SIGNUP_COMPLETE_ROUTE = "signup_complete"

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    navigate(LOGIN_ROUTE, navOptions)
}

fun NavController.navigateToSignUp(navOptions: NavOptions? = null) {
    navigate(SIGNUP_ROUTE, navOptions)
}

fun NavController.navigateToSignUpComplete(navOptions: NavOptions? = null) {
    navigate(SIGNUP_COMPLETE_ROUTE, navOptions)
}

fun NavGraphBuilder.loginScreen(
    onLoginSuccess: () -> Unit,
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
    onNavigateBack: () -> Unit,
) {
    composable(route = SIGNUP_ROUTE) {
        SignUpScreen(
            onSignUpSuccess = onSignUpSuccess,
            onNavigateBack = onNavigateBack,
        )
    }
}

fun NavGraphBuilder.signUpCompleteScreen(onNavigateToLogin: () -> Unit) {
    composable(route = SIGNUP_COMPLETE_ROUTE) {
        SignUpCompleteScreen(
            onNavigateToLogin = onNavigateToLogin,
        )
    }
}
