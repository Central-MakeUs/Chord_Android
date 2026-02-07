package com.team.chord.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface SetupRepository {
    fun isSetupCompleted(): Flow<Boolean>

    suspend fun setSetupCompleted()

    suspend fun completeOnboarding(
        name: String,
        employees: Int,
        laborCost: Int,
        includeWeeklyHolidayPay: Boolean,
    )
}
