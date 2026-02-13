package com.team.chord.core.data.di

import com.team.chord.core.data.datasource.HomeDataSource
import com.team.chord.core.data.datasource.remote.RemoteHomeDataSource
import com.team.chord.core.data.repository.HomeRepositoryImpl
import com.team.chord.core.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeDataModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository

    @Binds
    @Singleton
    abstract fun bindHomeDataSource(impl: RemoteHomeDataSource): HomeDataSource
}
