package com.team.chord.core.domain.repository

import com.team.chord.core.domain.model.Result

interface UserRepository {
    suspend fun deleteMe(): Result<Unit>
    suspend fun updateStore(
        name: String,
        employees: Int,
        laborCost: Int,
        rentCost: Int? = null,
        includeWeeklyHolidayPay: Boolean,
    ): Result<Unit>
}
