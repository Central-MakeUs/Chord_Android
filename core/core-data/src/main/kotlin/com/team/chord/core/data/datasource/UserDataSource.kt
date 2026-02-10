package com.team.chord.core.data.datasource

interface UserDataSource {
    suspend fun deleteMe()

    suspend fun updateStore(
        name: String,
        employees: Int,
        laborCost: Int,
        rentCost: Int? = null,
        includeWeeklyHolidayPay: Boolean,
    )
}
