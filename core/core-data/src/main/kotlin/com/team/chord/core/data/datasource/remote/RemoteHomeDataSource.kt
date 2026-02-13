package com.team.chord.core.data.datasource.remote

import com.team.chord.core.data.datasource.HomeDataSource
import com.team.chord.core.domain.model.home.HomeMenus
import com.team.chord.core.domain.model.home.HomeStrategyBrief
import com.team.chord.core.network.api.HomeApi
import com.team.chord.core.network.mapper.toDomain
import com.team.chord.core.network.util.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteHomeDataSource @Inject constructor(
    private val homeApi: HomeApi,
) : HomeDataSource {
    override suspend fun getHomeMenus(): HomeMenus =
        safeApiCall { homeApi.getHomeMenus() }.toDomain()

    override suspend fun getHomeStrategies(
        year: Int,
        month: Int,
        weekOfMonth: Int,
    ): List<HomeStrategyBrief> =
        safeApiCall {
            homeApi.getHomeStrategies(
                year = year,
                month = month,
                weekOfMonth = weekOfMonth,
            )
        }.toDomain()
}
