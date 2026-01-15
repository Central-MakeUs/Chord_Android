package com.team.chord.core.data.di

import com.team.chord.core.data.datasource.FakeIngredientDataSource
import com.team.chord.core.data.datasource.FakeRecentSearchDataSource
import com.team.chord.core.data.datasource.IngredientDataSource
import com.team.chord.core.data.datasource.RecentSearchDataSource
import com.team.chord.core.data.repository.IngredientRepositoryImpl
import com.team.chord.core.domain.repository.IngredientRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class IngredientDataModule {

    @Binds
    @Singleton
    abstract fun bindIngredientRepository(impl: IngredientRepositoryImpl): IngredientRepository

    @Binds
    @Singleton
    abstract fun bindIngredientDataSource(impl: FakeIngredientDataSource): IngredientDataSource

    @Binds
    @Singleton
    abstract fun bindRecentSearchDataSource(impl: FakeRecentSearchDataSource): RecentSearchDataSource
}
