package com.team.chord.core.network.dto.strategy

import kotlinx.serialization.Serializable

@Serializable
data class StrategyDto(
    val strategyId: Long,
    val title: String? = null,
    val summary: String? = null,
    val detail: String? = null,
    val weekLabel: String? = null,
    val state: String? = null,
    val type: String? = null,
    val year: Int? = null,
    val month: Int? = null,
    val weekOfMonth: Int? = null,
    val createdAt: String? = null,
    val completionDate: String? = null,
    val isSaved: Boolean? = null,
)

@Serializable
data class CompletionPhraseResponseDto(
    val completionPhrase: String? = null,
)

@Serializable
data class NeedManagementResponseDto(
    val strategyDate: String? = null,
    val menus: List<NeedManagementMenuDto>? = null,
)

@Serializable
data class NeedManagementMenuDto(
    val strategyId: Long? = null,
    val menuId: Long? = null,
    val menuName: String? = null,
    val costRate: Double? = null,
    val marginRate: Double? = null,
    val marginGradeCode: String? = null,
    val state: String? = null,
)

@Serializable
data class HighMarginMenuStrategyDetailResponseDto(
    val strategyId: Long,
    val summary: String? = null,
    val detail: String? = null,
    val guide: String? = null,
    val expectedEffect: String? = null,
    val state: String? = null,
    val saved: Boolean? = null,
    val startDate: String? = null,
    val completionDate: String? = null,
    val type: String? = null,
    val year: Int? = null,
    val month: Int? = null,
    val weekOfMonth: Int? = null,
    val menuNames: List<String>? = null,
)

@Serializable
data class DangerMenuStrategyDetailResponseDto(
    val strategyId: Long,
    val summary: String? = null,
    val detail: String? = null,
    val guide: String? = null,
    val expectedEffect: String? = null,
    val state: String? = null,
    val saved: Boolean? = null,
    val startDate: String? = null,
    val completionDate: String? = null,
    val menuId: Long? = null,
    val menuName: String? = null,
    val costRate: Double? = null,
    val type: String? = null,
    val year: Int? = null,
    val month: Int? = null,
    val weekOfMonth: Int? = null,
)

@Serializable
data class CautionMenuStrategyDetailResponseDto(
    val strategyId: Long,
    val summary: String? = null,
    val detail: String? = null,
    val guide: String? = null,
    val expectedEffect: String? = null,
    val state: String? = null,
    val saved: Boolean? = null,
    val startDate: String? = null,
    val completionDate: String? = null,
    val menuId: Long? = null,
    val menuName: String? = null,
    val type: String? = null,
    val year: Int? = null,
    val month: Int? = null,
    val weekOfMonth: Int? = null,
)
