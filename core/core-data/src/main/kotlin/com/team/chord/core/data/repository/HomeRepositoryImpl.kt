package com.team.chord.core.data.repository

import com.team.chord.core.data.datasource.HomeDataSource
import com.team.chord.core.domain.model.home.HomeMenus
import com.team.chord.core.domain.model.home.HomeStrategyBrief
import com.team.chord.core.domain.repository.HomeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val homeDataSource: HomeDataSource,
) : HomeRepository {
    override suspend fun getHomeMenus(): HomeMenus =
        homeDataSource.getHomeMenus()

    override suspend fun getHomeStrategies(
        year: Int,
        month: Int,
        weekOfMonth: Int,
    ): List<HomeStrategyBrief> =
        homeDataSource.getHomeStrategies(
            year = year,
            month = month,
            weekOfMonth = weekOfMonth,
        )
}
