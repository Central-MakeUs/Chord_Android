package com.team.chord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.team.chord.core.ui.theme.ChordTheme
import com.team.chord.feature.home.navigation.HOME_ROUTE
import com.team.chord.feature.home.navigation.navigateToHome
import com.team.chord.feature.ingredient.navigation.INGREDIENT_LIST_ROUTE
import com.team.chord.feature.ingredient.navigation.navigateToIngredientList
import com.team.chord.feature.menu.navigation.MENU_LIST_ROUTE
import com.team.chord.feature.menu.navigation.navigateToMenuList
import com.team.chord.navigation.ChordNavHost
import com.team.chord.ui.ChordBottomNavBar
import com.team.chord.ui.TopLevelDestination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChordTheme {
                val navController = rememberNavController()
                MainScreen(navController = navController)
            }
        }
    }
}

@Composable
private fun MainScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val shouldShowBottomBar =
        currentDestination?.hierarchy?.any { destination ->
            destination.route in listOf(HOME_ROUTE, MENU_LIST_ROUTE, INGREDIENT_LIST_ROUTE, "ai_coach")
        } == true

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (shouldShowBottomBar) {
                ChordBottomNavBar(
                    currentDestination = currentDestination?.route,
                    onNavigateToDestination = { destination ->
                        navigateToTopLevelDestination(navController, destination)
                    },
                )
            }
        },
    ) { innerPadding ->
        ChordNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

private fun navigateToTopLevelDestination(
    navController: NavHostController,
    destination: TopLevelDestination,
) {
    val topLevelNavOptions =
        navOptions {
            popUpTo(HOME_ROUTE) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

    when (destination) {
        TopLevelDestination.HOME -> {
            navController.navigateToHome(topLevelNavOptions)
        }

        TopLevelDestination.MENU -> {
            navController.navigateToMenuList(topLevelNavOptions)
        }

        TopLevelDestination.INGREDIENT -> {
            navController.navigateToIngredientList(topLevelNavOptions)
        }

        TopLevelDestination.AI_COACH -> { }
    }
}
