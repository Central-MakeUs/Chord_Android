package com.team.chord.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.team.chord.core.data.datasource.AuthDataSource
import com.team.chord.core.data.datasource.remote.RemoteAuthDataSource
import com.team.chord.core.data.repository.AuthRepositoryImpl
import com.team.chord.core.data.repository.SetupRepositoryImpl
import com.team.chord.core.domain.repository.AuthRepository
import com.team.chord.core.domain.repository.SetupRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindSetupRepository(impl: SetupRepositoryImpl): SetupRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindAuthDataSource(impl: RemoteAuthDataSource): AuthDataSource

    companion object {
        @Provides
        @Singleton
        fun providePreferencesDataStore(
            @ApplicationContext context: Context,
        ): DataStore<Preferences> =
            PreferenceDataStoreFactory.create {
                context.preferencesDataStoreFile("chord_preferences")
            }
    }
}
