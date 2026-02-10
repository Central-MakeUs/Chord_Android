package com.team.chord.core.data.repository

import com.team.chord.core.data.datasource.StrategyDataSource
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.strategy.Strategy
import com.team.chord.core.domain.repository.StrategyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StrategyRepositoryImpl @Inject constructor(
    private val strategyDataSource: StrategyDataSource,
) : StrategyRepository {
    override suspend fun getWeeklyStrategies(year: Int, month: Int, weekOfMonth: Int): List<Strategy> =
        strategyDataSource.getWeeklyStrategies(year, month, weekOfMonth)

    override suspend fun getSavedStrategies(year: Int, month: Int, isCompleted: Boolean): List<Strategy> =
        strategyDataSource.getSavedStrategies(year, month, isCompleted)

    override suspend fun startStrategy(strategyId: Long, type: String): Result<Unit> =
        runCatching { strategyDataSource.startStrategy(strategyId, type) }

    override suspend fun saveStrategy(strategyId: Long, type: String, isSaved: Boolean): Result<Unit> =
        runCatching { strategyDataSource.saveStrategy(strategyId, type, isSaved) }

    private inline fun runCatching(block: () -> Unit): Result<Unit> {
        return try {
            block()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
