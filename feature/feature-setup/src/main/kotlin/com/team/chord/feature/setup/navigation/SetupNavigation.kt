package com.team.chord.feature.setup.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.team.chord.feature.setup.OnboardingMenuViewModel
import com.team.chord.feature.setup.complete.SetupCompleteScreen
import com.team.chord.feature.setup.ingredientinput.IngredientInputScreen
import com.team.chord.feature.setup.menuconfirm.MenuConfirmScreen
import com.team.chord.feature.setup.menudetail.MenuDetailScreen
import com.team.chord.feature.setup.menusearch.MenuSearchScreen
import com.team.chord.feature.setup.menusuggestion.MenuSuggestionScreen
import com.team.chord.feature.setup.storeinfo.StoreInfoScreen

// Route constants
const val SETUP_GRAPH_ROUTE = "setup_graph"
const val STORE_INFO_ROUTE = "store_info"
const val MENU_SUGGESTION_ROUTE = "menu_suggestion"
const val MENU_SEARCH_ROUTE = "menu_search"
const val MENU_DETAIL_ROUTE = "menu_detail"
const val INGREDIENT_INPUT_ROUTE = "ingredient_input"
const val MENU_CONFIRM_ROUTE = "menu_confirm"
const val SETUP_COMPLETE_ROUTE = "setup_complete"

// Navigation argument keys
private const val ARG_MENU_NAME = "menuName"
private const val ARG_IS_TEMPLATE_APPLIED = "isTemplateApplied"
private const val ARG_TEMPLATE_PRICE = "templatePrice"

// Full route patterns with arguments
private const val MENU_DETAIL_ROUTE_PATTERN =
    "$MENU_DETAIL_ROUTE/{$ARG_MENU_NAME}/{$ARG_IS_TEMPLATE_APPLIED}?$ARG_TEMPLATE_PRICE={$ARG_TEMPLATE_PRICE}"

/**
 * Navigate to the setup graph (onboarding flow).
 */
fun NavController.navigateToSetupGraph(navOptions: NavOptions? = null) {
    navigate(SETUP_GRAPH_ROUTE, navOptions)
}

/**
 * Navigate to the store info screen.
 */
fun NavController.navigateToStoreInfo(navOptions: NavOptions? = null) {
    navigate(STORE_INFO_ROUTE, navOptions)
}

/**
 * Navigate to the menu suggestion screen.
 * This is the first screen after store info completion.
 */
fun NavController.navigateToMenuSuggestion(navOptions: NavOptions? = null) {
    navigate(MENU_SUGGESTION_ROUTE, navOptions)
}

/**
 * Navigate to the menu search screen.
 * User can search for menu templates or enter a new menu name.
 */
fun NavController.navigateToMenuSearch(navOptions: NavOptions? = null) {
    navigate(MENU_SEARCH_ROUTE, navOptions)
}

/**
 * Navigate to the menu detail screen with menu information.
 *
 * @param menuName The name of the menu
 * @param isTemplateApplied Whether a template is applied
 * @param templatePrice Optional suggested price from template
 * @param navOptions Navigation options
 */
fun NavController.navigateToMenuDetail(
    menuName: String,
    isTemplateApplied: Boolean,
    templatePrice: Int? = null,
    navOptions: NavOptions? = null,
) {
    val route = buildString {
        append("$MENU_DETAIL_ROUTE/")
        append(menuName.encodeForNavigation())
        append("/$isTemplateApplied")
        if (templatePrice != null) {
            append("?$ARG_TEMPLATE_PRICE=$templatePrice")
        }
    }
    navigate(route, navOptions)
}

/**
 * Navigate to the ingredient input screen.
 */
fun NavController.navigateToIngredientInput(navOptions: NavOptions? = null) {
    navigate(INGREDIENT_INPUT_ROUTE, navOptions)
}

/**
 * Navigate to the menu confirm screen.
 */
fun NavController.navigateToMenuConfirm(navOptions: NavOptions? = null) {
    navigate(MENU_CONFIRM_ROUTE, navOptions)
}

/**
 * Navigate to the setup complete screen.
 */
fun NavController.navigateToSetupComplete(navOptions: NavOptions? = null) {
    navigate(SETUP_COMPLETE_ROUTE, navOptions)
}

/**
 * Setup navigation graph containing all onboarding screens.
 *
 * Navigation flow:
 * StoreInfoScreen -> MenuSuggestionScreen -> MenuSearchScreen -> MenuDetailScreen
 *     -> IngredientInputScreen -> MenuConfirmScreen -> SetupCompleteScreen
 *
 * From MenuConfirmScreen, user can go back to MenuSearchScreen to add more menus.
 *
 * @param navController The navigation controller
 * @param onSetupComplete Callback when setup is complete and user should navigate to home
 */
fun NavGraphBuilder.setupGraph(
    navController: NavController,
    onSetupComplete: () -> Unit,
) {
    navigation(
        startDestination = STORE_INFO_ROUTE,
        route = SETUP_GRAPH_ROUTE,
    ) {
        // Store Info Screen (first screen in setup)
        composable(route = STORE_INFO_ROUTE) {
            StoreInfoScreen(
                onNavigateToMenuEntry = {
                    navController.navigateToMenuSuggestion()
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // Menu Suggestion Screen (explains menu registration benefits)
        composable(route = MENU_SUGGESTION_ROUTE) { backStackEntry ->
            // Get shared ViewModel from navigation graph scope
            val parentEntry = navController.getBackStackEntry(SETUP_GRAPH_ROUTE)
            val onboardingViewModel: OnboardingMenuViewModel = hiltViewModel(parentEntry)

            MenuSuggestionScreen(
                onStartMenuRegistration = {
                    navController.navigateToMenuSearch()
                },
            )
        }

        // Menu Search Screen (search for templates or enter new menu name)
        composable(route = MENU_SEARCH_ROUTE) { backStackEntry ->
            // Get shared ViewModel from navigation graph scope
            val parentEntry = navController.getBackStackEntry(SETUP_GRAPH_ROUTE)
            val onboardingViewModel: OnboardingMenuViewModel = hiltViewModel(parentEntry)

            MenuSearchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToDetailWithTemplate = { template ->
                    // Store template data in shared ViewModel
                    onboardingViewModel.startNewMenu(
                        name = template.name,
                        isTemplateApplied = true,
                        templatePrice = template.suggestedPrice,
                    )
                    navController.navigateToMenuDetail(
                        menuName = template.name,
                        isTemplateApplied = true,
                        templatePrice = template.suggestedPrice,
                    )
                },
                onNavigateToDetailWithoutTemplate = { menuName ->
                    // Store menu name in shared ViewModel (no template)
                    onboardingViewModel.startNewMenu(
                        name = menuName,
                        isTemplateApplied = false,
                        templatePrice = null,
                    )
                    navController.navigateToMenuDetail(
                        menuName = menuName,
                        isTemplateApplied = false,
                        templatePrice = null,
                    )
                },
            )
        }

        // Menu Detail Screen (enter price, category, preparation time)
        composable(
            route = MENU_DETAIL_ROUTE_PATTERN,
            arguments = listOf(
                navArgument(ARG_MENU_NAME) {
                    type = NavType.StringType
                },
                navArgument(ARG_IS_TEMPLATE_APPLIED) {
                    type = NavType.BoolType
                },
                navArgument(ARG_TEMPLATE_PRICE) {
                    type = NavType.IntType
                    defaultValue = 0
                },
            ),
        ) { backStackEntry ->
            // Get shared ViewModel from navigation graph scope
            val parentEntry = navController.getBackStackEntry(SETUP_GRAPH_ROUTE)
            val onboardingViewModel: OnboardingMenuViewModel = hiltViewModel(parentEntry)

            MenuDetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToIngredientInput = { menuDetailData ->
                    // Update shared ViewModel with menu details
                    onboardingViewModel.updateMenuDetail(
                        price = menuDetailData.price,
                        category = menuDetailData.category,
                        preparationSeconds = menuDetailData.preparationMinutes * 60 + menuDetailData.preparationSeconds,
                    )
                    navController.navigateToIngredientInput()
                },
            )
        }

        // Ingredient Input Screen (add ingredients to menu)
        composable(route = INGREDIENT_INPUT_ROUTE) { backStackEntry ->
            // Get shared ViewModel from navigation graph scope
            val parentEntry = navController.getBackStackEntry(SETUP_GRAPH_ROUTE)
            val onboardingViewModel: OnboardingMenuViewModel = hiltViewModel(parentEntry)

            IngredientInputScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToConfirm = { ingredients ->
                    // Store ingredients in shared ViewModel and complete menu
                    onboardingViewModel.addIngredients(ingredients)
                    onboardingViewModel.completeCurrentMenu()
                    navController.navigateToMenuConfirm()
                },
            )
        }

        // Menu Confirm Screen (review registered menus)
        composable(route = MENU_CONFIRM_ROUTE) { backStackEntry ->
            // Get shared ViewModel from navigation graph scope
            val parentEntry = navController.getBackStackEntry(SETUP_GRAPH_ROUTE)
            val onboardingViewModel: OnboardingMenuViewModel = hiltViewModel(parentEntry)

            MenuConfirmScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAddMore = {
                    // Go back to menu search to add another menu
                    navController.navigateToMenuSearch()
                },
                onComplete = {
                    navController.navigateToSetupComplete()
                },
            )
        }

        // Setup Complete Screen (final screen)
        composable(route = SETUP_COMPLETE_ROUTE) {
            SetupCompleteScreen(
                onNavigateToHome = onSetupComplete,
            )
        }
    }
}

/**
 * Encode a string for safe use in navigation routes.
 * Handles special characters that might cause issues in URLs.
 */
private fun String.encodeForNavigation(): String {
    return java.net.URLEncoder.encode(this, "UTF-8")
}
