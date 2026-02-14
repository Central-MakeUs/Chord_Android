package com.team.chord.core.network.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class StoreResponseDto(
    val name: String? = null,
    val employees: Int? = null,
    val laborCost: Double? = null,
    val rentCost: Double? = null,
    val includeWeeklyHolidayPay: Boolean? = null,
)
