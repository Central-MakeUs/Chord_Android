package com.team.chord.core.domain.usecase.home

import com.team.chord.core.domain.model.home.HomeMenus
import com.team.chord.core.domain.model.home.HomeStrategyBrief
import com.team.chord.core.domain.repository.HomeRepository
import javax.inject.Inject

class GetHomeMenusUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke(): HomeMenus =
        homeRepository.getHomeMenus()
}

class GetHomeStrategiesUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke(year: Int, month: Int, weekOfMonth: Int): List<HomeStrategyBrief> =
        homeRepository.getHomeStrategies(
            year = year,
            month = month,
            weekOfMonth = weekOfMonth,
        )
}
