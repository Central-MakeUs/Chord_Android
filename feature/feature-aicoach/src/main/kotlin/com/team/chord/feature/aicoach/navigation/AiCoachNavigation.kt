package com.team.chord.feature.aicoach.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.team.chord.feature.aicoach.strategy.AiStrategyScreen

const val AI_COACH_ROUTE = "ai_coach"

fun NavController.navigateToAiCoach(navOptions: NavOptions? = null) {
    navigate(AI_COACH_ROUTE, navOptions)
}

fun NavGraphBuilder.aiCoachScreen() {
    composable(route = AI_COACH_ROUTE) {
        AiStrategyScreen()
    }
}
