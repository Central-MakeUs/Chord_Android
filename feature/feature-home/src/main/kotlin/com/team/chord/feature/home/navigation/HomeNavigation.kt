package com.team.chord.feature.home.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.team.chord.feature.home.DangerMenuReportScreen
import com.team.chord.feature.home.DangerMenuReportViewModel
import com.team.chord.feature.home.HomeScreen
import com.team.chord.feature.home.HomeViewModel

const val HOME_ROUTE = "home"
const val HOME_REFRESH_REQUEST_KEY = "home_refresh_request"
const val DANGER_MENU_REPORT_ROUTE = "home/danger_menu_report"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HOME_ROUTE, navOptions)
}

fun NavController.navigateToDangerMenuReport(navOptions: NavOptions? = null) {
    navigate(DANGER_MENU_REPORT_ROUTE, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onNavigateToSetting: () -> Unit,
    onNavigateToDangerMenuReport: () -> Unit,
    onNavigateToAiCoach: () -> Unit,
) {
    composable(route = HOME_ROUTE) { backStackEntry ->
        val viewModel: HomeViewModel = hiltViewModel(backStackEntry)
        val refreshRequested by backStackEntry.savedStateHandle
            .getStateFlow(HOME_REFRESH_REQUEST_KEY, false)
            .collectAsStateWithLifecycle()

        LaunchedEffect(refreshRequested) {
            if (refreshRequested) {
                viewModel.refresh()
                backStackEntry.savedStateHandle[HOME_REFRESH_REQUEST_KEY] = false
            }
        }

        HomeScreen(
            onNavigateToSetting = onNavigateToSetting,
            onNavigateToDangerMenuReport = onNavigateToDangerMenuReport,
            onNavigateToAiCoach = onNavigateToAiCoach,
            viewModel = viewModel,
        )
    }
}

fun NavGraphBuilder.dangerMenuReportScreen(
    onNavigateBack: () -> Unit,
    onNavigateToStrategyDetail: (strategyId: Long, type: String) -> Unit,
) {
    composable(route = DANGER_MENU_REPORT_ROUTE) { backStackEntry ->
        val viewModel: DangerMenuReportViewModel = hiltViewModel(backStackEntry)
        DangerMenuReportScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToStrategyDetail = onNavigateToStrategyDetail,
            viewModel = viewModel,
        )
    }
}
