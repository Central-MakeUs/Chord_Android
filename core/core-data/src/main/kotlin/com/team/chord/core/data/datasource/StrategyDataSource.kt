package com.team.chord.core.data.datasource

import com.team.chord.core.domain.model.strategy.Strategy

interface StrategyDataSource {
    suspend fun getWeeklyStrategies(year: Int, month: Int, weekOfMonth: Int): List<Strategy>
    suspend fun getSavedStrategies(year: Int, month: Int, isCompleted: Boolean): List<Strategy>
    suspend fun startStrategy(strategyId: Long, type: String)
    suspend fun saveStrategy(strategyId: Long, type: String, isSaved: Boolean)
}
