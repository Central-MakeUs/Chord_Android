package com.team.chord.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.team.chord.core.domain.repository.SetupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SetupRepositoryImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : SetupRepository {
        override fun isSetupCompleted(): Flow<Boolean> =
            dataStore.data.map { preferences ->
                preferences[KEY_SETUP_COMPLETED] ?: false
            }

        override suspend fun setSetupCompleted() {
            dataStore.edit { preferences ->
                preferences[KEY_SETUP_COMPLETED] = true
            }
        }

        private companion object {
            val KEY_SETUP_COMPLETED = booleanPreferencesKey("onboarding_completed")
        }
    }
