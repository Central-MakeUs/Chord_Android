package com.team.chord.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.team.chord.feature.menu.add.complete.MenuAddCompleteScreen
import com.team.chord.feature.menu.add.confirm.MenuAddConfirmScreen
import com.team.chord.feature.menu.add.ingredientinput.IngredientInputScreen
import com.team.chord.feature.menu.add.menudetail.MenuDetailScreen
import com.team.chord.feature.menu.add.menusearch.MenuSearchScreen

const val MENU_ADD_GRAPH_ROUTE = "menu_add_graph"
private const val MENU_ADD_SEARCH_ROUTE = "menu_add_search"
private const val MENU_ADD_DETAIL_ROUTE = "menu_add_detail"
private const val MENU_ADD_INGREDIENT_INPUT_ROUTE = "menu_add_ingredient_input"
private const val MENU_ADD_CONFIRM_ROUTE = "menu_add_confirm"
private const val MENU_ADD_COMPLETE_ROUTE = "menu_add_complete"

private const val ARG_MENU_NAME = "menuName"
private const val ARG_IS_TEMPLATE_APPLIED = "isTemplateApplied"
private const val ARG_TEMPLATE_PRICE = "templatePrice"
private const val ARG_TEMPLATE_ID = "templateId"

private const val MENU_ADD_DETAIL_ROUTE_PATTERN =
    "$MENU_ADD_DETAIL_ROUTE/{$ARG_MENU_NAME}/{$ARG_IS_TEMPLATE_APPLIED}?$ARG_TEMPLATE_PRICE={$ARG_TEMPLATE_PRICE}"
private const val MENU_ADD_INGREDIENT_INPUT_ROUTE_PATTERN =
    "$MENU_ADD_INGREDIENT_INPUT_ROUTE/{$ARG_MENU_NAME}/{$ARG_IS_TEMPLATE_APPLIED}?$ARG_TEMPLATE_ID={$ARG_TEMPLATE_ID}"

fun NavController.navigateToMenuAddGraph(navOptions: NavOptions? = null) {
    navigate(MENU_ADD_GRAPH_ROUTE, navOptions)
}

fun NavGraphBuilder.menuAddGraph(
    navController: NavController,
    onComplete: () -> Unit,
) {
    navigation(
        startDestination = MENU_ADD_SEARCH_ROUTE,
        route = MENU_ADD_GRAPH_ROUTE,
    ) {
        composable(route = MENU_ADD_SEARCH_ROUTE) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MENU_ADD_GRAPH_ROUTE)
            }
            val menuAddFlowViewModel: MenuAddFlowViewModel = hiltViewModel(parentEntry)

            MenuSearchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToDetailWithTemplate = { template ->
                    menuAddFlowViewModel.startNewMenu(
                        name = template.menuName,
                        isTemplateApplied = true,
                        templatePrice = template.defaultSellingPrice,
                        templateId = template.templateId,
                        categoryCode = template.categoryCode,
                    )
                    val route = buildString {
                        append("$MENU_ADD_DETAIL_ROUTE/")
                        append(template.menuName.encodeForNavigation())
                        append("/true")
                        append("?$ARG_TEMPLATE_PRICE=${template.defaultSellingPrice}")
                    }
                    navController.navigate(route)
                },
                onNavigateToDetailWithoutTemplate = { menuName ->
                    menuAddFlowViewModel.startNewMenu(
                        name = menuName,
                        isTemplateApplied = false,
                        templatePrice = null,
                    )
                    val route = buildString {
                        append("$MENU_ADD_DETAIL_ROUTE/")
                        append(menuName.encodeForNavigation())
                        append("/false")
                    }
                    navController.navigate(route)
                },
            )
        }

        composable(
            route = MENU_ADD_DETAIL_ROUTE_PATTERN,
            arguments = listOf(
                navArgument(ARG_MENU_NAME) { type = NavType.StringType },
                navArgument(ARG_IS_TEMPLATE_APPLIED) { type = NavType.BoolType },
                navArgument(ARG_TEMPLATE_PRICE) {
                    type = NavType.IntType
                    defaultValue = 0
                },
            ),
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MENU_ADD_GRAPH_ROUTE)
            }
            val menuAddFlowViewModel: MenuAddFlowViewModel = hiltViewModel(parentEntry)

            MenuDetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToIngredientInput = { menuDetailData ->
                    menuAddFlowViewModel.updateMenuDetail(
                        price = menuDetailData.price,
                        category = menuDetailData.category,
                        preparationSeconds = menuDetailData.preparationMinutes * 60 + menuDetailData.preparationSeconds,
                    )
                    val route = buildString {
                        append("$MENU_ADD_INGREDIENT_INPUT_ROUTE/")
                        append(menuDetailData.menuName.encodeForNavigation())
                        append("/${menuDetailData.isTemplateApplied}")
                        val templateId = menuAddFlowViewModel.currentMenuDraft.value?.templateId
                        if (templateId != null) {
                            append("?$ARG_TEMPLATE_ID=$templateId")
                        }
                    }
                    navController.navigate(route)
                },
            )
        }

        composable(
            route = MENU_ADD_INGREDIENT_INPUT_ROUTE_PATTERN,
            arguments = listOf(
                navArgument(ARG_MENU_NAME) { type = NavType.StringType },
                navArgument(ARG_IS_TEMPLATE_APPLIED) { type = NavType.BoolType },
                navArgument(ARG_TEMPLATE_ID) {
                    type = NavType.LongType
                    defaultValue = 0L
                },
            ),
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MENU_ADD_GRAPH_ROUTE)
            }
            val menuAddFlowViewModel: MenuAddFlowViewModel = hiltViewModel(parentEntry)

            IngredientInputScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToConfirm = { ingredients ->
                    menuAddFlowViewModel.addIngredients(ingredients)
                    menuAddFlowViewModel.completeCurrentMenu()
                    navController.navigate(MENU_ADD_CONFIRM_ROUTE)
                },
            )
        }

        composable(route = MENU_ADD_CONFIRM_ROUTE) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MENU_ADD_GRAPH_ROUTE)
            }
            val menuAddFlowViewModel: MenuAddFlowViewModel = hiltViewModel(parentEntry)

            MenuAddConfirmScreen(
                registeredMenus = menuAddFlowViewModel.getRegisteredMenuSummaries(),
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAddAnother = {
                    navController.popBackStack(MENU_ADD_SEARCH_ROUTE, inclusive = false)
                },
                onRegisterSuccess = {
                    navController.navigate(MENU_ADD_COMPLETE_ROUTE)
                },
                onRegisterMenus = { menuAddFlowViewModel.registerMenus() },
            )
        }

        composable(route = MENU_ADD_COMPLETE_ROUTE) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MENU_ADD_GRAPH_ROUTE)
            }
            val menuAddFlowViewModel: MenuAddFlowViewModel = hiltViewModel(parentEntry)

            MenuAddCompleteScreen(
                onAutoFinish = {
                    menuAddFlowViewModel.clearAll()
                    onComplete()
                },
            )
        }
    }
}

private fun String.encodeForNavigation(): String {
    return java.net.URLEncoder.encode(this, "UTF-8")
}
