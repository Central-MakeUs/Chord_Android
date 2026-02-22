package com.team.chord.core.network.mapper

import com.team.chord.core.domain.model.strategy.Strategy
import com.team.chord.core.domain.model.strategy.StrategyDetail
import com.team.chord.core.domain.model.strategy.StrategyProgressStatus
import com.team.chord.core.network.dto.strategy.CautionMenuStrategyDetailResponseDto
import com.team.chord.core.network.dto.strategy.CompletionPhraseResponseDto
import com.team.chord.core.network.dto.strategy.DangerMenuStrategyDetailResponseDto
import com.team.chord.core.network.dto.strategy.HighMarginMenuStrategyDetailResponseDto
import com.team.chord.core.network.dto.strategy.StrategyDto
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.Locale

fun StrategyDto.toDomain(): Strategy {
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
        status = state.toProgressStatus(),
        type = (type ?: "CAUTION").uppercase(Locale.ROOT),
        isSaved = isSaved ?: false,
        createdAt = createdAt.toLocalDateTimeOrNull(),
    )
}

fun HighMarginMenuStrategyDetailResponseDto.toDomain(): StrategyDetail =
    StrategyDetail(
        strategyId = strategyId,
        type = (type ?: "HIGH_MARGIN").uppercase(Locale.ROOT),
        title = summary ?: "고마진 메뉴 판매 집중 전략",
        weekLabel = deriveWeekLabel(month, weekOfMonth),
        status = state.toProgressStatus(),
        diagnosisHeadline = "현재 고마진 메뉴 ${menuNames.orEmpty().size}개",
        diagnosisBody = detail.orEmpty(),
        guideBody = guide.orEmpty(),
        expectedEffectBody = expectedEffect.orEmpty(),
        menuNames = menuNames.orEmpty(),
    )

fun DangerMenuStrategyDetailResponseDto.toDomain(): StrategyDetail =
    StrategyDetail(
        strategyId = strategyId,
        type = (type ?: "DANGER").uppercase(Locale.ROOT),
        title = summary ?: "${menuName.orEmpty()} 메뉴의 마진율 개선이 필요해요",
        weekLabel = deriveWeekLabel(month, weekOfMonth),
        status = state.toProgressStatus(),
        diagnosisHeadline = buildMenuCostHeadline(menuName = menuName, costRate = costRate),
        diagnosisBody = detail.orEmpty(),
        guideBody = guide.orEmpty(),
        expectedEffectBody = expectedEffect.orEmpty(),
        menuName = menuName,
        costRate = costRate,
    )

fun CautionMenuStrategyDetailResponseDto.toDomain(): StrategyDetail =
    StrategyDetail(
        strategyId = strategyId,
        type = (type ?: "CAUTION").uppercase(Locale.ROOT),
        title = summary ?: "${menuName.orEmpty()} 메뉴의 원가율 관리가 필요해요",
        weekLabel = deriveWeekLabel(month, weekOfMonth),
        status = state.toProgressStatus(),
        diagnosisHeadline = buildMenuCostHeadline(menuName = menuName, costRate = null),
        diagnosisBody = detail.orEmpty(),
        guideBody = guide.orEmpty(),
        expectedEffectBody = expectedEffect.orEmpty(),
        menuName = menuName,
    )

fun CompletionPhraseResponseDto.toDomainPhrase(): String =
    completionPhrase?.takeIf { it.isNotBlank() } ?: "전략 실행이 완료됐어요"

private fun String?.toProgressStatus(): StrategyProgressStatus {
    val rawStatus = this.orEmpty().uppercase(Locale.ROOT)
    return when (rawStatus) {
        "ONGOING", "IN_PROGRESS" -> StrategyProgressStatus.IN_PROGRESS
        "COMPLETED" -> StrategyProgressStatus.COMPLETED
        else -> StrategyProgressStatus.NOT_STARTED
    }
}

private fun deriveWeekLabel(month: Int?, weekOfMonth: Int?): String? =
    if (month != null && weekOfMonth != null) "${month}월 ${weekOfMonth}주차" else null

private fun String?.toLocalDateTimeOrNull(): LocalDateTime? {
    val value = this?.trim().orEmpty()
    if (value.isBlank()) return null

    return runCatching { LocalDateTime.parse(value) }
        .recoverCatching { OffsetDateTime.parse(value).toLocalDateTime() }
        .getOrNull()
}

private fun buildMenuCostHeadline(menuName: String?, costRate: Double?): String {
    val normalizedMenuName = menuName.orEmpty().ifBlank { "해당 메뉴" }
    val percentText = costRate?.let { rate ->
        if (rate % 1.0 == 0.0) "${rate.toInt()}%" else String.format(Locale.KOREA, "%.1f%%", rate)
    }

    return if (percentText == null) {
        "${normalizedMenuName}의 원가율을 확인해보세요"
    } else {
        "${normalizedMenuName}의 원가율 ${percentText}"
    }
}
