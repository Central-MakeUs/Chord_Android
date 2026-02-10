package com.team.chord.feature.menu.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.team.chord.feature.menu.detail.MenuDetailScreen
import com.team.chord.feature.menu.ingredient.IngredientEditScreen
import com.team.chord.feature.menu.list.MenuListScreen
import com.team.chord.feature.menu.management.MenuManagementScreen

const val MENU_LIST_ROUTE = "menu_list"
const val MENU_DETAIL_ROUTE = "menu_detail/{menuId}"
const val MENU_MANAGEMENT_ROUTE = "menu_management/{menuId}"
const val INGREDIENT_EDIT_ROUTE = "ingredient_edit/{menuId}"

fun NavController.navigateToMenuList(navOptions: NavOptions? = null) {
    navigate(MENU_LIST_ROUTE, navOptions)
}

fun NavController.navigateToMenuDetail(
    menuId: Long,
    navOptions: NavOptions? = null,
) {
    navigate("menu_detail/$menuId", navOptions)
}

fun NavController.navigateToMenuManagement(
    menuId: Long,
    navOptions: NavOptions? = null,
) {
    navigate("menu_management/$menuId", navOptions)
}

fun NavController.navigateToIngredientEdit(
    menuId: Long,
    navOptions: NavOptions? = null,
) {
    navigate("ingredient_edit/$menuId", navOptions)
}

fun NavGraphBuilder.menuListScreen(
    onNavigateToDetail: (Long) -> Unit,
    onAddMenuClick: () -> Unit,
) {
    composable(route = MENU_LIST_ROUTE) {
        MenuListScreen(
            onNavigateToDetail = onNavigateToDetail,
            onAddMenuClick = onAddMenuClick,
        )
    }
}

fun NavGraphBuilder.menuDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToManagement: (Long) -> Unit,
    onNavigateToIngredientEdit: (Long) -> Unit,
    onMenuDeleted: () -> Unit,
) {
    composable(
        route = MENU_DETAIL_ROUTE,
        arguments = listOf(
            navArgument("menuId") { type = NavType.LongType },
        ),
    ) {
        MenuDetailScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToManagement = onNavigateToManagement,
            onNavigateToIngredientEdit = onNavigateToIngredientEdit,
            onMenuDeleted = onMenuDeleted,
        )
    }
}

fun NavGraphBuilder.menuManagementScreen(
    onNavigateBack: () -> Unit,
    onMenuDeleted: () -> Unit,
) {
    composable(
        route = MENU_MANAGEMENT_ROUTE,
        arguments = listOf(
            navArgument("menuId") { type = NavType.LongType },
        ),
    ) {
        MenuManagementScreen(
            onNavigateBack = onNavigateBack,
            onMenuDeleted = onMenuDeleted,
        )
    }
}

fun NavGraphBuilder.ingredientEditScreen(onNavigateBack: () -> Unit) {
    composable(
        route = INGREDIENT_EDIT_ROUTE,
        arguments = listOf(
            navArgument("menuId") { type = NavType.LongType },
        ),
    ) {
        IngredientEditScreen(
            onNavigateBack = onNavigateBack,
        )
    }
}
