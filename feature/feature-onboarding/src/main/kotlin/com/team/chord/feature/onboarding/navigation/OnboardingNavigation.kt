package com.team.chord.feature.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.team.chord.feature.onboarding.OnboardingScreen

const val ONBOARDING_ROUTE = "onboarding"

fun NavController.navigateToOnboarding(navOptions: NavOptions? = null) {
    navigate(ONBOARDING_ROUTE, navOptions)
}

fun NavGraphBuilder.onboardingScreen(onComplete: () -> Unit) {
    composable(route = ONBOARDING_ROUTE) {
        OnboardingScreen(onComplete = onComplete)
    }
}
