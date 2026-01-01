package com.team.chord.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.team.chord.feature.home.navigation.HOME_ROUTE
import com.team.chord.feature.home.navigation.homeScreen
import com.team.chord.feature.home.navigation.navigateToHome
import com.team.chord.feature.onboarding.navigation.ONBOARDING_ROUTE
import com.team.chord.feature.onboarding.navigation.onboardingScreen

@Composable
fun ChordNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: ChordNavHostViewModel = hiltViewModel(),
) {
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsStateWithLifecycle()

    if (isOnboardingCompleted == null) {
        return
    }

    val startDestination = if (isOnboardingCompleted == true) HOME_ROUTE else ONBOARDING_ROUTE

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        onboardingScreen(
            onComplete = {
                navController.navigateToHome(
                    navOptions =
                        navOptions {
                            popUpTo(ONBOARDING_ROUTE) { inclusive = true }
                        },
                )
            },
        )
        homeScreen()
    }
}
