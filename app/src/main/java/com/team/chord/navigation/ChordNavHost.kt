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
import com.team.chord.feature.aicoach.navigation.AI_COACH_ROUTE
import com.team.chord.feature.auth.navigation.LOGIN_ROUTE
import com.team.chord.feature.auth.navigation.SIGNUP_ROUTE
import com.team.chord.feature.auth.navigation.SIGNUP_PRIVACY_ROUTE
import com.team.chord.feature.auth.navigation.loginScreen
import com.team.chord.feature.auth.navigation.isSignUpRoute
import com.team.chord.feature.auth.navigation.navigateToLogin
import com.team.chord.feature.auth.navigation.navigateToSignUp
import com.team.chord.feature.auth.navigation.navigateToSignUpComplete
import com.team.chord.feature.auth.navigation.navigateToSignUpPrivacy
import com.team.chord.feature.auth.navigation.navigateToSignUpTerms
import com.team.chord.feature.auth.navigation.signUpCompleteScreen
import com.team.chord.feature.auth.navigation.signUpPrivacyScreen
import com.team.chord.feature.auth.navigation.signUpScreen
import com.team.chord.feature.auth.navigation.signUpTermsScreen
import com.team.chord.feature.auth.navigation.SIGNUP_COMPLETE_ROUTE
import com.team.chord.feature.auth.navigation.SIGNUP_TERMS_ROUTE
import com.team.chord.feature.home.navigation.HOME_REFRESH_REQUEST_KEY
import com.team.chord.feature.home.navigation.HOME_ROUTE
import com.team.chord.feature.home.navigation.dangerMenuReportScreen
import com.team.chord.feature.home.navigation.homeScreen
import com.team.chord.feature.home.navigation.navigateToDangerMenuReport
import com.team.chord.feature.home.navigation.navigateToHome
import com.team.chord.feature.ingredient.navigation.INGREDIENT_LIST_ROUTE
import com.team.chord.feature.ingredient.navigation.INGREDIENT_LIST_REFRESH_REQUEST_KEY
import com.team.chord.feature.ingredient.navigation.ingredientDetailScreen
import com.team.chord.feature.ingredient.navigation.ingredientListScreen
import com.team.chord.feature.ingredient.navigation.ingredientSearchScreen
import com.team.chord.feature.ingredient.navigation.navigateToIngredientDetail
import com.team.chord.feature.ingredient.navigation.navigateToIngredientSearch
import com.team.chord.feature.menu.navigation.MENU_LIST_ROUTE
import com.team.chord.feature.menu.navigation.MENU_LIST_REFRESH_REQUEST_KEY
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
import com.team.chord.feature.aicoach.navigation.AI_COACH_REFRESH_REQUEST_KEY
import com.team.chord.feature.aicoach.navigation.navigateToAiCoach
import com.team.chord.feature.aicoach.navigation.navigateToAiCoachComplete
import com.team.chord.feature.aicoach.navigation.navigateToAiCoachDetail
import com.team.chord.feature.aicoach.navigation.strategyCompleteScreen
import com.team.chord.feature.aicoach.navigation.strategyDetailScreen
import com.team.chord.feature.aicoach.navigation.STRATEGY_STARTED_MESSAGE_KEY
import com.team.chord.feature.setting.navigation.SETTING_ROUTE
import com.team.chord.feature.setting.navigation.faqScreen
import com.team.chord.feature.setting.navigation.navigateToFaq
import com.team.chord.feature.setting.navigation.navigateToSetting
import com.team.chord.feature.setting.navigation.navigateToStoreEdit
import com.team.chord.feature.setting.navigation.navigateToTerms
import com.team.chord.feature.setting.navigation.navigateToWithdraw
import com.team.chord.feature.setting.navigation.settingScreen
import com.team.chord.feature.setting.navigation.storeEditScreen
import com.team.chord.feature.setting.navigation.settingWebViewScreen
import com.team.chord.feature.setting.navigation.termsScreen
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
            if (currentRoute != null && currentRoute != LOGIN_ROUTE && !isSignUpRoute(currentRoute)) {
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
            onNavigateToTerms = {
                navController.navigateToSignUpTerms()
            },
            onNavigateToPrivacy = {
                navController.navigateToSignUpPrivacy()
            },
            onNavigateToLoginFallback = {
                navController.navigateToLogin(
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

        signUpTermsScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
        )

        signUpPrivacyScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
        )

        signUpCompleteScreen(
            onNavigateToSetup = {
                navController.navigateToSetupGraph(
                    navOptions =
                        navOptions {
                            popUpTo(LOGIN_ROUTE) { inclusive = true }
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
            onNavigateToDangerMenuReport = {
                navController.navigateToDangerMenuReport()
            },
            onNavigateToAiCoach = {
                navController.navigateToAiCoach(
                    navOptions = navOptions {
                        popUpTo(HOME_ROUTE) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    },
                )
            },
        )

        dangerMenuReportScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToStrategyDetail = { strategyId, type ->
                navController.navigateToAiCoachDetail(strategyId = strategyId, type = type)
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
                navController.navigateToTerms()
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
                requestRefresh(navController, HOME_ROUTE, HOME_REFRESH_REQUEST_KEY)
                navController.popBackStack()
            },
        )

        faqScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
        )

        termsScreen(
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
                requestRefresh(navController, MENU_LIST_ROUTE, MENU_LIST_REFRESH_REQUEST_KEY)
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
                requestRefresh(navController, MENU_LIST_ROUTE, MENU_LIST_REFRESH_REQUEST_KEY)
                navController.popBackStack(MENU_LIST_ROUTE, inclusive = false)
            },
        )

        menuManagementScreen(
            onNavigateBack = { hasChanges ->
                if (hasChanges) {
                    requestRefresh(navController, MENU_LIST_ROUTE, MENU_LIST_REFRESH_REQUEST_KEY)
                }
                navController.popBackStack()
            },
            onMenuDeleted = {
                requestRefresh(navController, MENU_LIST_ROUTE, MENU_LIST_REFRESH_REQUEST_KEY)
                navController.popBackStack(MENU_LIST_ROUTE, inclusive = false)
            },
        )

        ingredientEditScreen(
            onNavigateBack = { hasChanges ->
                if (hasChanges) {
                    requestRefresh(navController, MENU_LIST_ROUTE, MENU_LIST_REFRESH_REQUEST_KEY)
                }
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
            onNavigateBack = { hasChanges ->
                if (hasChanges) {
                    requestRefresh(navController, INGREDIENT_LIST_ROUTE, INGREDIENT_LIST_REFRESH_REQUEST_KEY)
                }
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

        aiCoachScreen(
            onNavigateToStrategyDetail = { strategyId, type ->
                navController.navigateToAiCoachDetail(strategyId = strategyId, type = type)
            },
        )

        strategyDetailScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onStrategyStarted = { message ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(STRATEGY_STARTED_MESSAGE_KEY, message)
                requestRefresh(navController, AI_COACH_ROUTE, AI_COACH_REFRESH_REQUEST_KEY)
                navController.popBackStack()
            },
            onNavigateToComplete = { completionPhrase ->
                navController.navigateToAiCoachComplete(completionPhrase = completionPhrase)
            },
        )

        strategyCompleteScreen(
            onConfirm = {
                requestRefresh(navController, AI_COACH_ROUTE, AI_COACH_REFRESH_REQUEST_KEY)
                val popped = navController.popBackStack(AI_COACH_ROUTE, inclusive = false)
                if (!popped) {
                    navController.navigateToAiCoach(
                        navOptions = navOptions {
                            popUpTo(navController.graph.id) { inclusive = false }
                            launchSingleTop = true
                        },
                    )
                }
            },
        )
    }
}

private fun requestRefresh(
    navController: NavHostController,
    route: String,
    key: String,
) {
    runCatching {
        navController.getBackStackEntry(route).savedStateHandle[key] = true
    }
}
