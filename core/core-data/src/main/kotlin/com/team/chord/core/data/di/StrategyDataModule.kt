package com.team.chord.core.data.di

import com.team.chord.core.data.datasource.FakeStrategyDataSource
import com.team.chord.core.data.datasource.StrategyDataSource
import com.team.chord.core.data.repository.StrategyRepositoryImpl
import com.team.chord.core.domain.repository.StrategyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StrategyDataModule {

    @Binds
    @Singleton
    abstract fun bindStrategyRepository(impl: StrategyRepositoryImpl): StrategyRepository

    @Binds
    @Singleton
    // TODO: Switch back to RemoteStrategyDataSource when strategy API returns production data.
    abstract fun bindStrategyDataSource(impl: FakeStrategyDataSource): StrategyDataSource
}
