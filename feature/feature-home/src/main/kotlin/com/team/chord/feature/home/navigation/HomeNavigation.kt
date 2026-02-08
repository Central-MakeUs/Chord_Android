package com.team.chord.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.team.chord.feature.home.HomeScreen

const val HOME_ROUTE = "home"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HOME_ROUTE, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onNavigateToSetting: () -> Unit,
) {
    composable(route = HOME_ROUTE) {
        HomeScreen(
            onNavigateToSetting = onNavigateToSetting,
        )
    }
}
