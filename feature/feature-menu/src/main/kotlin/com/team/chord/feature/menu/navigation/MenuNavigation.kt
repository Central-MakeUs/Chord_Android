package com.team.chord.feature.menu.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.team.chord.feature.menu.detail.MenuDetailScreen
import com.team.chord.feature.menu.list.MenuListScreen

const val MENU_LIST_ROUTE = "menu_list"
const val MENU_DETAIL_ROUTE = "menu_detail/{menuId}"

fun NavController.navigateToMenuList(navOptions: NavOptions? = null) {
    navigate(MENU_LIST_ROUTE, navOptions)
}

fun NavController.navigateToMenuDetail(
    menuId: Long,
    navOptions: NavOptions? = null,
) {
    navigate("menu_detail/$menuId", navOptions)
}

fun NavGraphBuilder.menuListScreen(onNavigateToDetail: (Long) -> Unit) {
    composable(route = MENU_LIST_ROUTE) {
        MenuListScreen(
            onNavigateToDetail = onNavigateToDetail,
        )
    }
}

fun NavGraphBuilder.menuDetailScreen(onNavigateBack: () -> Unit) {
    composable(
        route = MENU_DETAIL_ROUTE,
        arguments =
            listOf(
                navArgument("menuId") { type = NavType.LongType },
            ),
    ) {
        MenuDetailScreen(
            onNavigateBack = onNavigateBack,
        )
    }
}
