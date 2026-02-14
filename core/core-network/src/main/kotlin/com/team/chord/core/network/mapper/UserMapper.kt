package com.team.chord.core.network.mapper

import com.team.chord.core.domain.model.Store
import com.team.chord.core.network.dto.user.StoreResponseDto
import kotlin.math.roundToInt

fun StoreResponseDto.toDomain(): Store =
    Store(
        name = name.orEmpty(),
        employees = employees ?: 0,
        laborCost = laborCost?.roundToInt() ?: 0,
        rentCost = rentCost?.roundToInt(),
        includeWeeklyHolidayPay = includeWeeklyHolidayPay ?: false,
    )
