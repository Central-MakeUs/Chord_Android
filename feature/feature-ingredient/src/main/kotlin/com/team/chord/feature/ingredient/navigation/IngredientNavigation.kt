package com.team.chord.feature.ingredient.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.team.chord.feature.ingredient.detail.IngredientDetailScreen
import com.team.chord.feature.ingredient.list.IngredientListScreen
import com.team.chord.feature.ingredient.list.IngredientListViewModel
import com.team.chord.feature.ingredient.search.IngredientSearchScreen

const val INGREDIENT_LIST_ROUTE = "ingredient"
const val INGREDIENT_LIST_REFRESH_REQUEST_KEY = "ingredient_list_refresh_request"
const val INGREDIENT_DETAIL_ROUTE = "ingredient_detail"
const val INGREDIENT_SEARCH_ROUTE = "ingredient_search"

fun NavController.navigateToIngredientList(navOptions: NavOptions? = null) {
    navigate(INGREDIENT_LIST_ROUTE, navOptions)
}

fun NavController.navigateToIngredientDetail(ingredientId: Long) {
    navigate("$INGREDIENT_DETAIL_ROUTE/$ingredientId")
}

fun NavController.navigateToIngredientSearch() {
    navigate(INGREDIENT_SEARCH_ROUTE)
}

fun NavGraphBuilder.ingredientListScreen(
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToSearch: () -> Unit,
) {
    composable(route = INGREDIENT_LIST_ROUTE) { backStackEntry ->
        val viewModel: IngredientListViewModel = hiltViewModel(backStackEntry)
        val refreshRequested by backStackEntry.savedStateHandle
            .getStateFlow(INGREDIENT_LIST_REFRESH_REQUEST_KEY, false)
            .collectAsStateWithLifecycle()

        LaunchedEffect(refreshRequested) {
            if (refreshRequested) {
                viewModel.refresh()
                backStackEntry.savedStateHandle[INGREDIENT_LIST_REFRESH_REQUEST_KEY] = false
            }
        }

        IngredientListScreen(
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToSearch = onNavigateToSearch,
            viewModel = viewModel,
        )
    }
}

fun NavGraphBuilder.ingredientDetailScreen(
    onNavigateBack: (Boolean) -> Unit,
) {
    composable(
        route = "$INGREDIENT_DETAIL_ROUTE/{ingredientId}",
        arguments = listOf(navArgument("ingredientId") { type = NavType.LongType }),
    ) {
        IngredientDetailScreen(
            onNavigateBack = onNavigateBack,
        )
    }
}

fun NavGraphBuilder.ingredientSearchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
) {
    composable(route = INGREDIENT_SEARCH_ROUTE) {
        IngredientSearchScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToDetail = onNavigateToDetail,
        )
    }
}
