package com.team.chord.core.network.mapper

import com.team.chord.core.domain.model.strategy.Strategy
import com.team.chord.core.domain.model.strategy.StrategyProgressStatus
import com.team.chord.core.network.dto.strategy.StrategyDto

fun StrategyDto.toDomain(): Strategy {
    val rawStatus = state.orEmpty().uppercase()
    val progressStatus = when (rawStatus) {
        "ONGOING" -> StrategyProgressStatus.IN_PROGRESS
        "COMPLETED" -> StrategyProgressStatus.COMPLETED
        else -> StrategyProgressStatus.NOT_STARTED
    }

    val derivedWeekLabel = weekLabel ?: if (month != null && weekOfMonth != null) {
        "${month}월 ${weekOfMonth}주차"
    } else {
        null
    }

    return Strategy(
        id = strategyId,
        title = title ?: summary.orEmpty(),
        description = detail.orEmpty(),
        weekLabel = derivedWeekLabel,
        status = progressStatus,
        type = type ?: "CAUTION",
        isSaved = isSaved ?: false,
    )
}
