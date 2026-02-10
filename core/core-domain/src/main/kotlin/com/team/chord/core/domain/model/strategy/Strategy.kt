package com.team.chord.core.domain.model.strategy

data class Strategy(
    val id: Long,
    val title: String,
    val description: String,
    val weekLabel: String? = null,
    val status: StrategyProgressStatus,
    val type: String,
    val isSaved: Boolean,
)

enum class StrategyProgressStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
}
