package com.team.chord.core.domain.repository

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.strategy.Strategy

interface StrategyRepository {
    suspend fun getWeeklyStrategies(year: Int, month: Int, weekOfMonth: Int): List<Strategy>
    suspend fun getSavedStrategies(year: Int, month: Int, isCompleted: Boolean): List<Strategy>
    suspend fun startStrategy(strategyId: Long, type: String): Result<Unit>
    suspend fun saveStrategy(strategyId: Long, type: String, isSaved: Boolean): Result<Unit>
}
