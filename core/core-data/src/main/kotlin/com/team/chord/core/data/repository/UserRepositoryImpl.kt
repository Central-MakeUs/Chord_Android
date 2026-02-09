package com.team.chord.core.data.repository

import com.team.chord.core.data.datasource.UserDataSource
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
) : UserRepository {
    override suspend fun deleteMe(): Result<Unit> =
        try {
            userDataSource.deleteMe()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
}
