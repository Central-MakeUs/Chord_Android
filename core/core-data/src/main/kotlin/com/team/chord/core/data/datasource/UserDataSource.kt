package com.team.chord.core.data.datasource

import com.team.chord.core.domain.model.Store

interface UserDataSource {
    suspend fun getStore(): Store

    suspend fun deleteMe()

    suspend fun updateStore(
        name: String,
        employees: Int,
        laborCost: Int,
        rentCost: Int? = null,
        includeWeeklyHolidayPay: Boolean,
    )
}
