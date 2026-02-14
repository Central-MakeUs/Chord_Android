package com.team.chord.core.domain.repository

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.Store

interface UserRepository {
    suspend fun getStore(): Result<Store>

    suspend fun deleteMe(): Result<Unit>
    suspend fun updateStore(
        name: String,
        employees: Int,
        laborCost: Int,
        rentCost: Int? = null,
        includeWeeklyHolidayPay: Boolean,
    ): Result<Unit>
}
