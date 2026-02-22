package com.team.chord.core.data.datasource.remote

import com.team.chord.core.data.datasource.StrategyDataSource
import com.team.chord.core.domain.model.strategy.Strategy
import com.team.chord.core.domain.model.strategy.StrategyDetail
import com.team.chord.core.network.api.StrategyApi
import com.team.chord.core.network.mapper.toDomain
import com.team.chord.core.network.mapper.toDomainPhrase
import com.team.chord.core.network.util.safeApiCall
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteStrategyDataSource @Inject constructor(
    private val strategyApi: StrategyApi,
) : StrategyDataSource {
    override suspend fun getWeeklyStrategies(year: Int, month: Int, weekOfMonth: Int): List<Strategy> =
        safeApiCall { strategyApi.getWeeklyStrategies(year, month, weekOfMonth) }.map { it.toDomain() }

    override suspend fun getSavedStrategies(year: Int, month: Int, isCompleted: Boolean): List<Strategy> =
        safeApiCall { strategyApi.getSavedStrategies(year, month, isCompleted) }.map { it.toDomain() }

    override suspend fun getStrategyDetail(strategyId: Long, type: String): StrategyDetail {
        return when (type.uppercase(Locale.ROOT)) {
            "DANGER" -> safeApiCall { strategyApi.getDangerStrategyDetail(strategyId) }.toDomain()
            "HIGH_MARGIN" -> safeApiCall { strategyApi.getHighMarginStrategyDetail(strategyId) }.toDomain()
            else -> safeApiCall { strategyApi.getCautionStrategyDetail(strategyId) }.toDomain()
        }
    }

    override suspend fun startStrategy(strategyId: Long, type: String) {
        safeApiCall { strategyApi.startStrategy(strategyId, type) }
    }

    override suspend fun completeStrategy(strategyId: Long, type: String): String =
        safeApiCall { strategyApi.completeStrategy(strategyId, type) }.toDomainPhrase()

    override suspend fun saveStrategy(strategyId: Long, type: String, isSaved: Boolean) {
        safeApiCall { strategyApi.saveStrategy(strategyId, type, isSaved) }
    }
}
