package com.team.chord.core.domain.usecase.strategy

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.strategy.Strategy
import com.team.chord.core.domain.model.strategy.StrategyDetail
import com.team.chord.core.domain.repository.StrategyRepository
import javax.inject.Inject

class GetWeeklyStrategiesUseCase @Inject constructor(
    private val strategyRepository: StrategyRepository,
) {
    suspend operator fun invoke(year: Int, month: Int, weekOfMonth: Int): List<Strategy> =
        strategyRepository.getWeeklyStrategies(year, month, weekOfMonth)
}

class GetSavedStrategiesUseCase @Inject constructor(
    private val strategyRepository: StrategyRepository,
) {
    suspend operator fun invoke(year: Int, month: Int, isCompleted: Boolean): List<Strategy> =
        strategyRepository.getSavedStrategies(year, month, isCompleted)
}

class GetStrategyDetailUseCase @Inject constructor(
    private val strategyRepository: StrategyRepository,
) {
    suspend operator fun invoke(strategyId: Long, type: String): StrategyDetail =
        strategyRepository.getStrategyDetail(strategyId, type)
}

class StartStrategyUseCase @Inject constructor(
    private val strategyRepository: StrategyRepository,
) {
    suspend operator fun invoke(strategyId: Long, type: String): Result<Unit> =
        strategyRepository.startStrategy(strategyId, type)
}

class CompleteStrategyUseCase @Inject constructor(
    private val strategyRepository: StrategyRepository,
) {
    suspend operator fun invoke(strategyId: Long, type: String): Result<String> =
        strategyRepository.completeStrategy(strategyId, type)
}

class SaveStrategyUseCase @Inject constructor(
    private val strategyRepository: StrategyRepository,
) {
    suspend operator fun invoke(strategyId: Long, type: String, isSaved: Boolean): Result<Unit> =
        strategyRepository.saveStrategy(strategyId, type, isSaved)
}
