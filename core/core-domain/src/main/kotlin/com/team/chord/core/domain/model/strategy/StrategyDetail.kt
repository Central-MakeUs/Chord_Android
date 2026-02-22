package com.team.chord.core.domain.model.strategy

data class StrategyDetail(
    val strategyId: Long,
    val type: String,
    val title: String,
    val weekLabel: String? = null,
    val status: StrategyProgressStatus,
    val diagnosisHeadline: String,
    val diagnosisBody: String,
    val guideBody: String,
    val expectedEffectBody: String,
    val menuNames: List<String> = emptyList(),
    val menuName: String? = null,
    val costRate: Double? = null,
)
