package com.team.chord.core.domain.repository

import com.team.chord.core.domain.model.home.HomeMenus
import com.team.chord.core.domain.model.home.HomeStrategyBrief

interface HomeRepository {
    suspend fun getHomeMenus(): HomeMenus

    suspend fun getHomeStrategies(
        year: Int,
        month: Int,
        weekOfMonth: Int,
    ): List<HomeStrategyBrief>
}
