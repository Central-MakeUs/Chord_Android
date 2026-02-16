package com.team.chord.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.team.chord.core.domain.repository.SetupRepository
import com.team.chord.core.network.api.UserApi
import com.team.chord.core.network.dto.user.OnboardingRequestDto
import com.team.chord.core.network.util.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SetupRepositoryImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
        private val userApi: UserApi,
    ) : SetupRepository {
        override fun isSetupCompleted(): Flow<Boolean> =
            dataStore.data.map { preferences ->
                preferences[KEY_SETUP_COMPLETED] ?: false
            }

        override suspend fun setSetupCompleted(isCompleted: Boolean) {
            dataStore.edit { preferences ->
                preferences[KEY_SETUP_COMPLETED] = isCompleted
            }
        }

        override suspend fun completeOnboarding(
            name: String,
            employees: Int,
            laborCost: Int,
            rentCost: Int?,
            includeWeeklyHolidayPay: Boolean,
        ) {
            safeApiCall {
                userApi.completeOnboarding(
                    OnboardingRequestDto(
                        name = name,
                        employees = employees,
                        laborCost = laborCost,
                        rentCost = rentCost,
                        includeWeeklyHolidayPay = includeWeeklyHolidayPay,
                    ),
                )
            }
        }

        private companion object {
            val KEY_SETUP_COMPLETED = booleanPreferencesKey("onboarding_completed")
        }
    }
