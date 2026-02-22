package com.team.chord.navigation

import androidx.compose.ui.graphics.Color
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.PrimaryBlue100
import com.team.chord.feature.aicoach.navigation.AI_COACH_COMPLETE_ROUTE
import com.team.chord.feature.aicoach.navigation.AI_COACH_DETAIL_ROUTE
import com.team.chord.feature.aicoach.navigation.AI_COACH_ROUTE
import com.team.chord.feature.auth.navigation.LOGIN_ROUTE
import com.team.chord.feature.auth.navigation.SIGNUP_COMPLETE_ROUTE
import com.team.chord.feature.auth.navigation.SIGNUP_ROUTE
import com.team.chord.feature.home.navigation.HOME_ROUTE
import com.team.chord.feature.ingredient.navigation.INGREDIENT_DETAIL_ROUTE
import com.team.chord.feature.ingredient.navigation.INGREDIENT_LIST_ROUTE
import com.team.chord.feature.ingredient.navigation.INGREDIENT_SEARCH_ROUTE
import com.team.chord.feature.menu.navigation.INGREDIENT_EDIT_ROUTE
import com.team.chord.feature.menu.navigation.MENU_DETAIL_ROUTE as MENU_DETAIL_WITH_ID_ROUTE
import com.team.chord.feature.menu.navigation.MENU_LIST_ROUTE
import com.team.chord.feature.menu.navigation.MENU_MANAGEMENT_ROUTE
import com.team.chord.feature.setting.navigation.FAQ_ROUTE
import com.team.chord.feature.setting.navigation.SETTING_ROUTE
import com.team.chord.feature.setting.navigation.SETTING_WEBVIEW_ROUTE
import com.team.chord.feature.setting.navigation.STORE_EDIT_ROUTE
import com.team.chord.feature.setting.navigation.WITHDRAW_ROUTE
import com.team.chord.feature.setup.navigation.INGREDIENT_INPUT_ROUTE as SETUP_INGREDIENT_INPUT_ROUTE
import com.team.chord.feature.setup.navigation.MENU_CONFIRM_ROUTE
import com.team.chord.feature.setup.navigation.MENU_DETAIL_ROUTE as SETUP_MENU_DETAIL_BASE_ROUTE
import com.team.chord.feature.setup.navigation.MENU_SEARCH_ROUTE
import com.team.chord.feature.setup.navigation.MENU_SUGGESTION_ROUTE
import com.team.chord.feature.setup.navigation.SETUP_COMPLETE_ROUTE
import com.team.chord.feature.setup.navigation.SETUP_GRAPH_ROUTE
import com.team.chord.feature.setup.navigation.STORE_INFO_ROUTE
import com.team.chord.ui.SystemBarStyleSpec
import com.team.chord.ui.shouldUseDarkIcons

private const val MENU_ADD_GRAPH_BASE = "menu_add_graph"
private const val MENU_ADD_SEARCH_ROUTE = "menu_add_search"
private const val MENU_ADD_DETAIL_ROUTE = "menu_add_detail"
private const val MENU_ADD_INGREDIENT_INPUT_ROUTE = "menu_add_ingredient_input"
private const val MENU_ADD_CONFIRM_ROUTE = "menu_add_confirm"
private const val MENU_ADD_COMPLETE_ROUTE = "menu_add_complete"

fun statusBarStyleForRoute(route: String?): SystemBarStyleSpec {
    val color = statusBarColorForRoute(route)
    return SystemBarStyleSpec(
        color = color,
        darkIcons = color.shouldUseDarkIcons(),
    )
}

private fun statusBarColorForRoute(route: String?): Color {
    if (route == null) return Grayscale100

    return when {
        // Top-level tabs
        route == HOME_ROUTE -> PrimaryBlue100
        route == MENU_LIST_ROUTE -> Grayscale200
        route == INGREDIENT_LIST_ROUTE -> Grayscale200
        route == AI_COACH_ROUTE -> Grayscale200
        routeMatches(route, AI_COACH_DETAIL_ROUTE) -> Grayscale200
        routeMatches(route, AI_COACH_COMPLETE_ROUTE) -> Grayscale200
        route == SETTING_ROUTE -> Grayscale200

        // Auth
        route == LOGIN_ROUTE || route == SIGNUP_ROUTE || route == SIGNUP_COMPLETE_ROUTE -> Grayscale100

        // Setting sub-routes
        route == STORE_EDIT_ROUTE -> Grayscale100
        route == FAQ_ROUTE -> Grayscale100
        route == WITHDRAW_ROUTE -> Grayscale100
        routeMatches(route, SETTING_WEBVIEW_ROUTE) -> Grayscale100

        // Menu screens
        routeMatches(route, MENU_DETAIL_WITH_ID_ROUTE) -> Grayscale100
        routeMatches(route, MENU_MANAGEMENT_ROUTE) -> Grayscale100
        routeMatches(route, INGREDIENT_EDIT_ROUTE) -> Grayscale100

        // Ingredient screens
        routeMatches(route, "$INGREDIENT_DETAIL_ROUTE/{ingredientId}") -> Grayscale100
        route == INGREDIENT_SEARCH_ROUTE -> Grayscale200

        // Setup graph screens
        route == SETUP_GRAPH_ROUTE -> Grayscale100
        route == STORE_INFO_ROUTE -> Grayscale100
        route == MENU_SUGGESTION_ROUTE -> Grayscale100
        route == MENU_SEARCH_ROUTE -> Grayscale100
        routeMatches(route, SETUP_MENU_DETAIL_BASE_ROUTE) -> Grayscale100
        routeMatches(route, SETUP_INGREDIENT_INPUT_ROUTE) -> Grayscale100
        route == MENU_CONFIRM_ROUTE -> Grayscale100
        route == SETUP_COMPLETE_ROUTE -> Grayscale100

        // Menu-add graph screens
        routeMatches(route, MENU_ADD_GRAPH_ROUTE) -> Grayscale100
        routeMatches(route, MENU_ADD_GRAPH_BASE) -> Grayscale100
        routeMatches(route, MENU_ADD_SEARCH_ROUTE) -> Grayscale100
        routeMatches(route, MENU_ADD_DETAIL_ROUTE) -> Grayscale100
        routeMatches(route, MENU_ADD_INGREDIENT_INPUT_ROUTE) -> Grayscale100
        routeMatches(route, MENU_ADD_CONFIRM_ROUTE) -> Grayscale100
        routeMatches(route, MENU_ADD_COMPLETE_ROUTE) -> Grayscale100

        else -> Grayscale100
    }
}

private fun routeMatches(
    route: String,
    declaredRoute: String,
): Boolean {
    val baseRoute = declaredRoute.substringBefore("{").trimEnd('/', '?')
    return route == declaredRoute ||
        (baseRoute.isNotEmpty() && (
            route == baseRoute ||
                route.startsWith("$baseRoute/") ||
                route.startsWith("$baseRoute?")
        ))
}
