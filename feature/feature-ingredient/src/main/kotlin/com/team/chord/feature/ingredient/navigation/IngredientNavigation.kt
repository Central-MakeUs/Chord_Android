package com.team.chord.feature.ingredient.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.team.chord.feature.ingredient.detail.IngredientDetailScreen
import com.team.chord.feature.ingredient.list.IngredientListScreen
import com.team.chord.feature.ingredient.search.IngredientSearchScreen

const val INGREDIENT_LIST_ROUTE = "ingredient"
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
    composable(route = INGREDIENT_LIST_ROUTE) {
        IngredientListScreen(
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToSearch = onNavigateToSearch,
        )
    }
}

fun NavGraphBuilder.ingredientDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPriceEdit: (Long) -> Unit,
    onNavigateToSupplierEdit: (Long) -> Unit,
) {
    composable(
        route = "$INGREDIENT_DETAIL_ROUTE/{ingredientId}",
        arguments = listOf(navArgument("ingredientId") { type = NavType.LongType }),
    ) {
        IngredientDetailScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToPriceEdit = onNavigateToPriceEdit,
            onNavigateToSupplierEdit = onNavigateToSupplierEdit,
        )
    }
}

fun NavGraphBuilder.ingredientSearchScreen(
    onNavigateBack: () -> Unit,
) {
    composable(route = INGREDIENT_SEARCH_ROUTE) {
        IngredientSearchScreen(
            onNavigateBack = onNavigateBack,
        )
    }
}
