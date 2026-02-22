package com.team.chord.feature.aicoach.strategy.detail

import com.team.chord.core.domain.model.strategy.StrategyProgressStatus

data class StrategyDetailUiState(
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val detail: StrategyDetailUi? = null,
    val startedMessage: String? = null,
    val completionPhrase: String? = null,
)

data class StrategyDetailUi(
    val strategyId: Long,
    val type: String,
    val weekLabel: String,
    val title: String,
    val status: StrategyProgressStatus,
    val diagnosisHeadline: String,
    val diagnosisBody: String,
    val guideBody: String,
    val expectedEffectBody: String,
    val menuNames: List<String>,
)
