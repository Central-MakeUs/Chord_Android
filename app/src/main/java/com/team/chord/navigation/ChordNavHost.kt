package com.team.chord.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.team.chord.feature.auth.navigation.navigateToSignUpComplete
import com.team.chord.feature.auth.navigation.signUpCompleteScreen
import com.team.chord.feature.auth.navigation.signUpScreen
import com.team.chord.feature.auth.navigation.SIGNUP_COMPLETE_ROUTE
import com.team.chord.feature.home.navigation.HOME_ROUTE
import com.team.chord.feature.home.navigation.homeScreen
import com.team.chord.feature.home.navigation.navigateToHome
import com.team.chord.feature.ingredient.navigation.INGREDIENT_LIST_ROUTE
import com.team.chord.feature.ingredient.navigation.ingredientDetailScreen
import com.team.chord.feature.ingredient.navigation.ingredientListScreen
import com.team.chord.feature.ingredient.navigation.ingredientSearchScreen
import com.team.chord.feature.ingredient.navigation.navigateToIngredientDetail
import com.team.chord.feature.ingredient.navigation.navigateToIngredientSearch
import com.team.chord.feature.menu.navigation.MENU_LIST_ROUTE
import com.team.chord.feature.menu.navigation.ingredientEditScreen
import com.team.chord.feature.menu.navigation.menuDetailScreen
import com.team.chord.feature.menu.navigation.menuListScreen
import com.team.chord.feature.menu.navigation.menuManagementScreen
import com.team.chord.feature.menu.navigation.navigateToIngredientEdit
import com.team.chord.feature.menu.navigation.navigateToMenuDetail
import com.team.chord.feature.menu.navigation.navigateToMenuList
import com.team.chord.feature.menu.navigation.navigateToMenuManagement
import com.team.chord.navigation.menuAddGraph
import com.team.chord.navigation.navigateToMenuAddGraph
import com.team.chord.feature.setup.navigation.SETUP_GRAPH_ROUTE
import com.team.chord.feature.setup.navigation.navigateToSetupGraph
import com.team.chord.feature.setup.navigation.setupGraph
import com.team.chord.feature.aicoach.navigation.aiCoachScreen
import com.team.chord.feature.setting.navigation.SETTING_ROUTE
import com.team.chord.feature.setting.navigation.faqScreen
import com.team.chord.feature.setting.navigation.navigateToFaq
import com.team.chord.feature.setting.navigation.navigateToSetting
import com.team.chord.feature.setting.navigation.navigateToStoreEdit
import com.team.chord.feature.setting.navigation.navigateToSettingWebView
import com.team.chord.feature.setting.navigation.navigateToWithdraw
import com.team.chord.feature.setting.navigation.settingScreen
import com.team.chord.feature.setting.navigation.storeEditScreen
import com.team.chord.feature.setting.navigation.settingWebViewScreen
import com.team.chord.feature.setting.navigation.withdrawScreen

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
            StartDestination.LOGIN -> LOGIN_ROUTE
            StartDestination.SETUP -> SETUP_GRAPH_ROUTE
            StartDestination.HOME -> HOME_ROUTE
        }

    // Handle session expiry: navigate to login when auth state changes to Unauthenticated
    LaunchedEffect(navigationState) {
        val state = navigationState
        if (state is NavigationState.Ready && state.startDestination == StartDestination.LOGIN) {
            val currentRoute = navController.currentDestination?.route
            if (currentRoute != null &&
                currentRoute != LOGIN_ROUTE &&
                currentRoute != SIGNUP_ROUTE &&
                currentRoute != SIGNUP_COMPLETE_ROUTE
            ) {
                navController.navigateToLogin(
                    navOptions = navOptions {
                        popUpTo(navController.graph.id) { inclusive = true }
                    },
                )
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        loginScreen(
            onLoginSuccess = { isSetupCompleted ->
                if (isSetupCompleted) {
                    navController.navigateToHome(
                        navOptions =
                            navOptions {
                                popUpTo(LOGIN_ROUTE) { inclusive = true }
                            },
                    )
                } else {
                    navController.navigateToSetupGraph(
                        navOptions =
                            navOptions {
                                popUpTo(LOGIN_ROUTE) { inclusive = true }
                            },
                    )
                }
            },
            onNavigateToSignUp = {
                navController.navigateToSignUp()
            },
        )

        signUpScreen(
            onSignUpSuccess = {
                navController.navigateToSignUpComplete(
                    navOptions =
                        navOptions {
                            popUpTo(SIGNUP_ROUTE) { inclusive = true }
                        },
                )
            },
            onNavigateBack = {
                navController.popBackStack()
            },
        )

        signUpCompleteScreen(
            onNavigateToLogin = {
                navController.navigateToLogin(
                    navOptions =
                        navOptions {
                            popUpTo(SIGNUP_COMPLETE_ROUTE) { inclusive = true }
                        },
                )
            },
        )

        setupGraph(
            navController = navController,
            onSetupComplete = {
                viewModel.markSetupCompleted()
                navController.navigateToHome(
                    navOptions =
                        navOptions {
                            popUpTo(SETUP_GRAPH_ROUTE) { inclusive = true }
                        },
                )
            },
        )

        homeScreen(
            onNavigateToSetting = {
                navController.navigateToSetting()
            },
        )

        settingScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToStoreEdit = {
                navController.navigateToStoreEdit()
            },
            onNavigateToFaq = {
                navController.navigateToFaq()
            },
            onNavigateToTerms = {
                navController.navigateToSettingWebView(
                    title = "이용약관",
                    url = "https://www.notion.so/30064018c6f6805aaa09c3a1c71a3376",
                )
            },
            onNavigateToWithdraw = {
                navController.navigateToWithdraw()
            },
            onLogout = {
                navController.navigateToLogin(
                    navOptions = navOptions {
                        popUpTo(SETTING_ROUTE) { inclusive = true }
                    },
                )
            },
        )

        storeEditScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onStoreEditComplete = {
                navController.popBackStack()
            },
        )

        faqScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
        )

        withdrawScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onWithdrawSuccess = {
                navController.navigateToLogin(
                    navOptions = navOptions {
                        popUpTo(navController.graph.id) { inclusive = true }
                    },
                )
            },
        )

        settingWebViewScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
        )

        menuListScreen(
            onNavigateToDetail = { menuId ->
                navController.navigateToMenuDetail(menuId)
            },
            onAddMenuClick = { categoryCode ->
                navController.navigateToMenuAddGraph(categoryCode)
            },
        )

        menuAddGraph(
            navController = navController,
            onComplete = {
                navController.popBackStack(MENU_LIST_ROUTE, inclusive = false)
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
            onMenuDeleted = {
                navController.popBackStack(MENU_LIST_ROUTE, inclusive = false)
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

        ingredientListScreen(
            onNavigateToDetail = { ingredientId ->
                navController.navigateToIngredientDetail(ingredientId)
            },
            onNavigateToSearch = {
                navController.navigateToIngredientSearch()
            },
        )

        ingredientDetailScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
        )

        ingredientSearchScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToDetail = { ingredientId ->
                navController.navigateToIngredientDetail(ingredientId)
            },
        )

        aiCoachScreen()
    }
}
