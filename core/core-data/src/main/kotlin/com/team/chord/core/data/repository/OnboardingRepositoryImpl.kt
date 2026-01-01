package com.team.chord.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.team.chord.core.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OnboardingRepositoryImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : OnboardingRepository {
        override fun isOnboardingCompleted(): Flow<Boolean> =
            dataStore.data.map { preferences ->
                preferences[KEY_ONBOARDING_COMPLETED] ?: false
            }

        override suspend fun setOnboardingCompleted() {
            dataStore.edit { preferences ->
                preferences[KEY_ONBOARDING_COMPLETED] = true
            }
        }

        private companion object {
            val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        }
    }
