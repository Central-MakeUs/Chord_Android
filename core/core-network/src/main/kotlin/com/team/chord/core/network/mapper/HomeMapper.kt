package com.team.chord.core.network.mapper

import com.team.chord.core.domain.model.home.HomeMenus
import com.team.chord.core.domain.model.home.HomeStrategyBrief
import com.team.chord.core.network.dto.home.HomeMenusResponseDto
import com.team.chord.core.network.dto.home.HomeStrategiesResponseDto
import com.team.chord.core.network.dto.home.HomeStrategyBriefDto

fun HomeMenusResponseDto.toDomain(): HomeMenus =
    HomeMenus(
        numOfDangerMenus = numOfDangerMenus ?: 0,
        avgCostRate = avgCostRate?.avgCostRate ?: 0.0,
        avgMarginRate = avgMarginRate ?: 0.0,
        avgCostRateGradeCode = avgCostRate?.marginGradeCode.orEmpty(),
    )

fun HomeStrategiesResponseDto.toDomain(): List<HomeStrategyBrief> =
    strategies.orEmpty().map { it.toDomain() }

fun HomeStrategyBriefDto.toDomain(): HomeStrategyBrief =
    HomeStrategyBrief(
        menuId = menuId ?: 0L,
        menuName = menuName.orEmpty(),
        strategyId = strategyId ?: 0L,
        summary = summary.orEmpty(),
    )
