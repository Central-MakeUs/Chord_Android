package com.team.chord.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.team.chord.feature.auth.navigation.LOGIN_ROUTE
import com.team.chord.feature.auth.navigation.SIGNUP_ROUTE
import com.team.chord.feature.auth.navigation.loginScreen
import com.team.chord.feature.auth.navigation.navigateToLogin
import com.team.chord.feature.auth.navigation.navigateToSignUp
import com.team.chord.feature.auth.navigation.signUpScreen
import com.team.chord.feature.home.navigation.HOME_ROUTE
import com.team.chord.feature.home.navigation.homeScreen
import com.team.chord.feature.home.navigation.navigateToHome
import com.team.chord.feature.menu.navigation.MENU_LIST_ROUTE
import com.team.chord.feature.menu.navigation.ingredientEditScreen
import com.team.chord.feature.menu.navigation.menuDetailScreen
import com.team.chord.feature.menu.navigation.menuListScreen
import com.team.chord.feature.menu.navigation.menuManagementScreen
import com.team.chord.feature.menu.navigation.navigateToIngredientEdit
import com.team.chord.feature.menu.navigation.navigateToMenuDetail
import com.team.chord.feature.menu.navigation.navigateToMenuList
import com.team.chord.feature.menu.navigation.navigateToMenuManagement
import com.team.chord.feature.onboarding.navigation.ONBOARDING_ROUTE
import com.team.chord.feature.onboarding.navigation.onboardingScreen
import com.team.chord.feature.setup.navigation.SETUP_GRAPH_ROUTE
import com.team.chord.feature.setup.navigation.navigateToSetupGraph
import com.team.chord.feature.setup.navigation.setupGraph

@Composable
fun ChordNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: ChordNavHostViewModel = hiltViewModel(),
) {
    val navigationState by viewModel.navigationState.collectAsStateWithLifecycle()

    if (navigationState is NavigationState.Loading) {
        return
    }

    val startDestination =
        when ((navigationState as NavigationState.Ready).startDestination) {
            StartDestination.ONBOARDING -> ONBOARDING_ROUTE
            StartDestination.LOGIN -> LOGIN_ROUTE
            StartDestination.HOME -> HOME_ROUTE
        }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        onboardingScreen(
            onComplete = {
                navController.navigateToLogin(
                    navOptions =
                        navOptions {
                            popUpTo(ONBOARDING_ROUTE) { inclusive = true }
                        },
                )
            },
        )

        loginScreen(
            onLoginSuccess = {
                navController.navigateToHome(
                    navOptions =
                        navOptions {
                            popUpTo(LOGIN_ROUTE) { inclusive = true }
                        },
                )
            },
            onNavigateToSignUp = {
                navController.navigateToSetupGraph()
            },
        )

        signUpScreen(
            onSignUpSuccess = {
                navController.navigateToHome(
                    navOptions =
                        navOptions {
                            popUpTo(0) { inclusive = true }
                        },
                )
            },
            onNavigateBack = {
                navController.popBackStack()
            },
        )

        setupGraph(
            navController = navController,
            onSetupComplete = {
                navController.navigateToHome(
                    navOptions =
                        navOptions {
                            popUpTo(0) { inclusive = true }
                        },
                )
            },
        )

        homeScreen()

        menuListScreen(
            onNavigateToDetail = { menuId ->
                navController.navigateToMenuDetail(menuId)
            },
            onAddMenuClick = {
                // TODO: Navigate to add menu screen
            },
        )

        menuDetailScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToManagement = { menuId ->
                navController.navigateToMenuManagement(menuId)
            },
            onNavigateToIngredientEdit = { menuId ->
                navController.navigateToIngredientEdit(menuId)
            },
        )

        menuManagementScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onMenuDeleted = {
                navController.popBackStack(MENU_LIST_ROUTE, inclusive = false)
            },
        )

        ingredientEditScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
        )
    }
}
