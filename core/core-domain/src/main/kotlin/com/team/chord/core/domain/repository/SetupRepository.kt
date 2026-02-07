package com.team.chord.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface SetupRepository {
    fun isSetupCompleted(): Flow<Boolean>

    suspend fun setSetupCompleted()
}
