package com.team.chord.core.network.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UpdateStoreRequestDto(
    val name: String,
    val employees: Int,
    val laborCost: Int,
    val rentCost: Int? = null,
    val includeWeeklyHolidayPay: Boolean,
)
