package com.team.chord.core.data.di

import com.team.chord.core.data.datasource.FakeMenuDataSource
import com.team.chord.core.data.datasource.MenuDataSource
import com.team.chord.core.data.repository.MenuRepositoryImpl
import com.team.chord.core.domain.repository.MenuRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MenuDataModule {

    @Binds
    @Singleton
    abstract fun bindMenuRepository(impl: MenuRepositoryImpl): MenuRepository

    @Binds
    @Singleton
    abstract fun bindMenuDataSource(impl: FakeMenuDataSource): MenuDataSource
}
