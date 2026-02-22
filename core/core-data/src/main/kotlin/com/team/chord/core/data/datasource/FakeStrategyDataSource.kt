package com.team.chord.core.data.datasource

import com.team.chord.core.domain.model.strategy.Strategy
import com.team.chord.core.domain.model.strategy.StrategyDetail
import com.team.chord.core.domain.model.strategy.StrategyProgressStatus
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeStrategyDataSource @Inject constructor() : StrategyDataSource {

    private val lock = Any()
    private val recordsByMonth = mutableMapOf<YearMonth, MutableList<StrategyRecord>>()

    override suspend fun getWeeklyStrategies(year: Int, month: Int, weekOfMonth: Int): List<Strategy> =
        synchronized(lock) {
            val normalizedWeek = weekOfMonth.coerceIn(1, MAX_WEEKS_IN_MONTH)
            ensureMonthData(year, month)
                .asSequence()
                .filter { it.weekOfMonth == normalizedWeek }
                .sortedBy { it.id }
                .map { it.toStrategy() }
                .toList()
        }

    override suspend fun getSavedStrategies(year: Int, month: Int, isCompleted: Boolean): List<Strategy> =
        synchronized(lock) {
            ensureMonthData(year, month)
                .asSequence()
                .filter { it.isSaved }
                .filter { record ->
                    if (isCompleted) {
                        record.status == StrategyProgressStatus.COMPLETED
                    } else {
                        record.status != StrategyProgressStatus.COMPLETED
                    }
                }
                .sortedWith(compareByDescending<StrategyRecord> { it.weekOfMonth }.thenBy { it.id })
                .map { it.toStrategy() }
                .toList()
        }

    override suspend fun getStrategyDetail(strategyId: Long, type: String): StrategyDetail =
        synchronized(lock) {
            val record = findRecord(strategyId) ?: throw IllegalArgumentException("전략을 찾을 수 없어요.")
            if (!record.type.equals(type, ignoreCase = true)) {
                throw IllegalArgumentException("전략 타입이 올바르지 않아요.")
            }
            record.toDetail()
        }

    override suspend fun startStrategy(strategyId: Long, type: String) {
        synchronized(lock) {
            val target = findRecord(strategyId) ?: return
            if (!target.type.equals(type, ignoreCase = true)) return
            if (target.status == StrategyProgressStatus.NOT_STARTED) {
                target.status = StrategyProgressStatus.IN_PROGRESS
            }
        }
    }

    override suspend fun completeStrategy(strategyId: Long, type: String): String =
        synchronized(lock) {
            val target = findRecord(strategyId) ?: throw IllegalArgumentException("전략을 찾을 수 없어요.")
            if (!target.type.equals(type, ignoreCase = true)) {
                throw IllegalArgumentException("전략 타입이 올바르지 않아요.")
            }
            target.status = StrategyProgressStatus.COMPLETED
            completionPhraseByType(target.type)
        }

    override suspend fun saveStrategy(strategyId: Long, type: String, isSaved: Boolean) {
        synchronized(lock) {
            val target = findRecord(strategyId) ?: return
            if (!target.type.equals(type, ignoreCase = true)) return
            target.isSaved = isSaved
        }
    }

    private fun ensureMonthData(year: Int, month: Int): MutableList<StrategyRecord> {
        val yearMonth = YearMonth.of(year, month)
        return recordsByMonth.getOrPut(yearMonth) { createInitialRecords(yearMonth) }
    }

    private fun findRecord(strategyId: Long): StrategyRecord? =
        recordsByMonth.values
            .asSequence()
            .flatten()
            .firstOrNull { it.id == strategyId }

    private fun createInitialRecords(yearMonth: YearMonth): MutableList<StrategyRecord> {
        val records = mutableListOf<StrategyRecord>()

        for (week in 1..MAX_WEEKS_IN_MONTH) {
            val createdAt = yearMonth.atDay(((week - 1) * 7 + 1).coerceAtMost(yearMonth.lengthOfMonth())).atTime(0, 0)

            records += StrategyRecord(
                id = buildStrategyId(yearMonth.year, yearMonth.monthValue, week, 1),
                weekOfMonth = week,
                title = "흑임자 두존쿠",
                description = DANGER_DESCRIPTION,
                status = if (week % 2 == 0) StrategyProgressStatus.NOT_STARTED else StrategyProgressStatus.IN_PROGRESS,
                type = TYPE_DANGER,
                isSaved = true,
                createdAt = createdAt,
                diagnosisHeadline = "흑임자 두존쿠의 원가율 67%",
                diagnosisBody = "1잔 판매 시 실제 남는 금액은 1,200원이에요. 판매량이 있어도 구조적으로 남는 돈이 거의 없어요.",
                guideBody = "판매 가격을 6,500원으로 인상해 공헌이익을 높여 보세요. 고객에게 가격 인상 안내를 먼저 하는 것이 좋아요.",
                expectedEffectBody = "권장 가격 6,500원으로 설정하면 1잔당 남는 금액이 1,000원 증가해요.",
                menuName = "흑임자 두존쿠",
                costRate = 67.0,
            )

            records += StrategyRecord(
                id = buildStrategyId(yearMonth.year, yearMonth.monthValue, week, 2),
                weekOfMonth = week,
                title = "${yearMonth.monthValue}월 ${week}주차 고마진 메뉴",
                description = HIGH_MARGIN_DESCRIPTION,
                status = if (week == 3) StrategyProgressStatus.COMPLETED else StrategyProgressStatus.NOT_STARTED,
                type = TYPE_HIGH_MARGIN,
                isSaved = true,
                createdAt = createdAt.plusHours(1),
                diagnosisHeadline = "현재 고마진 메뉴 5개",
                diagnosisBody = "고마진 메뉴 5개가 존재하며 이 메뉴들의 판매가 전체 수익에 긍정적인 영향을 주고 있어요.",
                guideBody = "마진율이 높은 메뉴를 홍보하고, 세트 메뉴 구성 또는 추천 메뉴로 제안해 판매를 늘려 보세요.",
                expectedEffectBody = "고마진 메뉴 판매 증대로 카페 전체 수익성을 향상시키고 고객 만족도를 높일 수 있어요.",
                menuNames = listOf("카페라떼", "아메리카노", "고구마 케이크", "딸기 버블티", "버블티"),
            )

            records += StrategyRecord(
                id = buildStrategyId(yearMonth.year, yearMonth.monthValue, week, 3),
                weekOfMonth = week,
                title = "치즈케이크",
                description = CAUTION_DESCRIPTION,
                status = if (week <= 2) StrategyProgressStatus.COMPLETED else StrategyProgressStatus.NOT_STARTED,
                type = TYPE_CAUTION,
                isSaved = true,
                createdAt = createdAt.plusHours(2),
                diagnosisHeadline = "치즈케이크의 원가율을 확인해보세요",
                diagnosisBody = "원가율이 주의 구간에 있어요. 당장 급한 위험 상태는 아니지만 개선이 필요해요.",
                guideBody = "재료 사용량과 판매가를 점검해 원가율을 낮춰 보세요.",
                expectedEffectBody = "원가율을 안정 구간으로 낮추면 월간 수익이 꾸준히 개선돼요.",
                menuName = "치즈케이크",
                costRate = 43.0,
            )
        }

        return records
    }

    private fun completionPhraseByType(type: String): String =
        when (type.uppercase(Locale.ROOT)) {
            TYPE_HIGH_MARGIN -> "고마진 메뉴의 판매 비중이 높아지면 더 빠르게 수익이 쌓이고, 카페 전체 수익구조가 좋아져요."
            TYPE_DANGER -> "원가율 위험 메뉴를 빠르게 조정하면 손실 위험을 줄일 수 있어요."
            else -> "주의 메뉴를 꾸준히 관리하면 안정적인 수익 구조를 만들 수 있어요."
        }

    private fun buildStrategyId(year: Int, month: Int, week: Int, index: Int): Long =
        year.toLong() * 100_000L + month.toLong() * 1_000L + week.toLong() * 10L + index

    private data class StrategyRecord(
        val id: Long,
        val weekOfMonth: Int,
        val title: String,
        val description: String,
        var status: StrategyProgressStatus,
        val type: String,
        var isSaved: Boolean,
        val createdAt: LocalDateTime,
        val diagnosisHeadline: String,
        val diagnosisBody: String,
        val guideBody: String,
        val expectedEffectBody: String,
        val menuNames: List<String> = emptyList(),
        val menuName: String? = null,
        val costRate: Double? = null,
    ) {
        fun toStrategy(): Strategy =
            Strategy(
                id = id,
                title = title,
                description = description,
                weekLabel = "${createdAt.monthValue}월 ${weekOfMonth}주차",
                status = status,
                type = type,
                isSaved = isSaved,
                createdAt = createdAt,
            )

        fun toDetail(): StrategyDetail =
            StrategyDetail(
                strategyId = id,
                type = type,
                title = when (type.uppercase(Locale.ROOT)) {
                    TYPE_DANGER -> "${menuName.orEmpty()} 메뉴의 마진율 개선이 필요해요"
                    TYPE_CAUTION -> "${menuName.orEmpty()} 메뉴의 원가율 관리가 필요해요"
                    else -> "고마진 메뉴 판매 집중 전략"
                },
                weekLabel = "${createdAt.monthValue}월 ${weekOfMonth}주차",
                status = status,
                diagnosisHeadline = diagnosisHeadline,
                diagnosisBody = diagnosisBody,
                guideBody = guideBody,
                expectedEffectBody = expectedEffectBody,
                menuNames = menuNames,
                menuName = menuName,
                costRate = costRate,
            )
    }

    private companion object {
        const val MAX_WEEKS_IN_MONTH = 5

        const val TYPE_DANGER = "DANGER"
        const val TYPE_HIGH_MARGIN = "HIGH_MARGIN"
        const val TYPE_CAUTION = "CAUTION"

        const val DANGER_DESCRIPTION = "원가를 위험 메뉴 확인"
        const val HIGH_MARGIN_DESCRIPTION = "우리 카페 고마진 메뉴 확인"
        const val CAUTION_DESCRIPTION = "원가율 주의 메뉴 확인"
    }
}
