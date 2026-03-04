package com.team.chord

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.team.chord.feature.aicoach.navigation.AI_COACH_ROUTE
import com.team.chord.feature.aicoach.navigation.navigateToAiCoach
import com.team.chord.navigation.ChordNavHost
import com.team.chord.navigation.ChordNavHostViewModel
import com.team.chord.navigation.NavigationState
import com.team.chord.navigation.statusBarStyleForRoute
import com.team.chord.ui.ApplySystemBarStyle
import com.team.chord.ui.ChordBottomNavBar
import com.team.chord.ui.TopLevelDestination
import dagger.hilt.android.AndroidEntryPoint

private const val BACK_PRESS_EXIT_INTERVAL_MS = 2_000L
private const val BACK_PRESS_EXIT_MESSAGE = "뒤로가기를 한번 더 누르면 종료됩니다."

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val navHostViewModel: ChordNavHostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            navHostViewModel.navigationState.value is NavigationState.Loading
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChordTheme {
                val navController = rememberNavController()
                MainScreen(
                    navController = navController,
                    navHostViewModel = navHostViewModel,
                )
            }
        }
    }
}

@Composable
private fun MainScreen(
    navController: NavHostController,
    navHostViewModel: ChordNavHostViewModel,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val statusBarStyle = statusBarStyleForRoute(currentDestination?.route)

    ApplySystemBarStyle(style = statusBarStyle)

    val shouldShowBottomBar =
        currentDestination?.hierarchy?.any { destination ->
            destination.route in listOf(HOME_ROUTE, MENU_LIST_ROUTE, INGREDIENT_LIST_ROUTE, AI_COACH_ROUTE)
        } == true

    val context = LocalContext.current
    var lastBackPressedAt by remember { mutableLongStateOf(0L) }

    BackHandler {
        if (navController.popBackStack()) {
            lastBackPressedAt = 0L
            return@BackHandler
        }

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressedAt <= BACK_PRESS_EXIT_INTERVAL_MS) {
            (context as? Activity)?.finish()
        } else {
            lastBackPressedAt = currentTime
            Toast.makeText(context, BACK_PRESS_EXIT_MESSAGE, Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                viewModel = navHostViewModel,
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .background(statusBarStyle.color)
                .align(Alignment.TopCenter),
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

        TopLevelDestination.AI_COACH -> {
            navController.navigateToAiCoach(topLevelNavOptions)
        }
    }
}
