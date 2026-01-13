package com.team.chord.feature.setup.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.team.chord.feature.setup.complete.SetupCompleteScreen
import com.team.chord.feature.setup.menuentry.MenuCategory
import com.team.chord.feature.setup.menuentry.MenuEntryScreen
import com.team.chord.feature.setup.menumanagement.MenuManagementScreen
import com.team.chord.feature.setup.menumanagement.MenuManagementViewModel
import com.team.chord.feature.setup.storeinfo.StoreInfoScreen

const val SETUP_GRAPH_ROUTE = "setup_graph"
const val STORE_INFO_ROUTE = "store_info"
const val MENU_ENTRY_ROUTE = "menu_entry"
const val MENU_MANAGEMENT_ROUTE = "menu_management"
const val SETUP_COMPLETE_ROUTE = "setup_complete"

fun NavController.navigateToSetupGraph(navOptions: NavOptions? = null) {
    navigate(SETUP_GRAPH_ROUTE, navOptions)
}

fun NavController.navigateToStoreInfo(navOptions: NavOptions? = null) {
    navigate(STORE_INFO_ROUTE, navOptions)
}

fun NavController.navigateToMenuEntry(navOptions: NavOptions? = null) {
    navigate(MENU_ENTRY_ROUTE, navOptions)
}

fun NavController.navigateToMenuManagement(navOptions: NavOptions? = null) {
    navigate(MENU_MANAGEMENT_ROUTE, navOptions)
}

fun NavController.navigateToSetupComplete(navOptions: NavOptions? = null) {
    navigate(SETUP_COMPLETE_ROUTE, navOptions)
}

fun NavGraphBuilder.setupGraph(
    navController: NavController,
    onSetupComplete: () -> Unit,
) {
    navigation(
        startDestination = STORE_INFO_ROUTE,
        route = SETUP_GRAPH_ROUTE,
    ) {
        composable(route = STORE_INFO_ROUTE) {
            StoreInfoScreen(
                onNavigateToMenuEntry = {
//                    navController.navigateToMenuManagement()
                    navController.navigateToSetupComplete()
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        composable(route = MENU_ENTRY_ROUTE) { backStackEntry ->
            val parentEntry = navController.getBackStackEntry(SETUP_GRAPH_ROUTE)
            val menuManagementViewModel: MenuManagementViewModel = hiltViewModel(parentEntry)

            MenuEntryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onMenuRegistered = { name, price, category ->
                    menuManagementViewModel.addMenu(name, price, category)
                    navController.popBackStack()
                },
            )
        }

        composable(route = MENU_MANAGEMENT_ROUTE) { backStackEntry ->
            val parentEntry = navController.getBackStackEntry(SETUP_GRAPH_ROUTE)
            val menuManagementViewModel: MenuManagementViewModel = hiltViewModel(parentEntry)
            val uiState by menuManagementViewModel.uiState.collectAsStateWithLifecycle()

            MenuManagementScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToMenuEntry = {
                    navController.navigateToMenuEntry()
                },
                onNavigateToComplete = {
                    navController.navigateToSetupComplete()
                },
                viewModel = menuManagementViewModel,
            )
        }

        composable(route = SETUP_COMPLETE_ROUTE) {
            SetupCompleteScreen(
                onNavigateToHome = onSetupComplete,
            )
        }
    }
}
