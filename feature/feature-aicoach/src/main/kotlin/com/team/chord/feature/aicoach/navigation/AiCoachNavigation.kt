package com.team.chord.feature.aicoach.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.team.chord.feature.aicoach.strategy.AiStrategyScreen
import com.team.chord.feature.aicoach.strategy.complete.StrategyCompleteScreen
import com.team.chord.feature.aicoach.strategy.detail.StrategyDetailScreen

const val AI_COACH_ROUTE = "ai_coach"
const val AI_COACH_DETAIL_ROUTE = "ai_coach_detail/{strategyId}?type={type}"
const val AI_COACH_COMPLETE_ROUTE = "ai_coach_complete?phrase={phrase}"

const val STRATEGY_STARTED_MESSAGE_KEY = "strategy_started_message"

private const val STRATEGY_ID_ARG = "strategyId"
private const val STRATEGY_TYPE_ARG = "type"
private const val STRATEGY_COMPLETE_PHRASE_ARG = "phrase"

fun NavController.navigateToAiCoach(navOptions: NavOptions? = null) {
    navigate(AI_COACH_ROUTE, navOptions)
}

fun NavController.navigateToAiCoachDetail(
    strategyId: Long,
    type: String,
    navOptions: NavOptions? = null,
) {
    val encodedType = Uri.encode(type)
    navigate("ai_coach_detail/$strategyId?type=$encodedType", navOptions)
}

fun NavController.navigateToAiCoachComplete(
    completionPhrase: String,
    navOptions: NavOptions? = null,
) {
    val encodedPhrase = Uri.encode(completionPhrase)
    navigate("ai_coach_complete?phrase=$encodedPhrase", navOptions)
}

fun NavGraphBuilder.aiCoachScreen(
    onNavigateToStrategyDetail: (Long, String) -> Unit,
) {
    composable(route = AI_COACH_ROUTE) { backStackEntry ->
        val strategyStartedMessage = backStackEntry.savedStateHandle.get<String>(STRATEGY_STARTED_MESSAGE_KEY)

        AiStrategyScreen(
            onNavigateToStrategyDetail = onNavigateToStrategyDetail,
            strategyStartedMessage = strategyStartedMessage,
            onStrategyStartedMessageConsumed = {
                backStackEntry.savedStateHandle.remove<String>(STRATEGY_STARTED_MESSAGE_KEY)
            },
        )
    }
}

fun NavGraphBuilder.strategyDetailScreen(
    onNavigateBack: () -> Unit,
    onStrategyStarted: (String) -> Unit,
    onNavigateToComplete: (String) -> Unit,
) {
    composable(
        route = AI_COACH_DETAIL_ROUTE,
        arguments = listOf(
            navArgument(STRATEGY_ID_ARG) { type = NavType.LongType },
            navArgument(STRATEGY_TYPE_ARG) {
                type = NavType.StringType
                defaultValue = "CAUTION"
            },
        ),
    ) {
        StrategyDetailScreen(
            onNavigateBack = onNavigateBack,
            onStrategyStarted = onStrategyStarted,
            onNavigateToComplete = onNavigateToComplete,
        )
    }
}

fun NavGraphBuilder.strategyCompleteScreen(
    onConfirm: () -> Unit,
) {
    composable(
        route = AI_COACH_COMPLETE_ROUTE,
        arguments = listOf(
            navArgument(STRATEGY_COMPLETE_PHRASE_ARG) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
        ),
    ) { backStackEntry ->
        val completionPhrase = Uri.decode(backStackEntry.arguments?.getString(STRATEGY_COMPLETE_PHRASE_ARG).orEmpty())
        StrategyCompleteScreen(
            completionPhrase = completionPhrase,
            onConfirm = onConfirm,
        )
    }
}
