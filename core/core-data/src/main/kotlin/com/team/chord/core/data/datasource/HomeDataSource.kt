package com.team.chord.core.data.datasource

import com.team.chord.core.domain.model.home.HomeMenus
import com.team.chord.core.domain.model.home.HomeStrategyBrief

interface HomeDataSource {
    suspend fun getHomeMenus(): HomeMenus

    suspend fun getHomeStrategies(
        year: Int,
        month: Int,
        weekOfMonth: Int,
    ): List<HomeStrategyBrief>
}
