package com.team.chord.core.network.dto.user

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class OnboardingRequestDto(
    val name: String,
    val employees: Int,
    val laborCost: Int,
    @EncodeDefault val includeWeeklyHolidayPay: Boolean = false,
)
