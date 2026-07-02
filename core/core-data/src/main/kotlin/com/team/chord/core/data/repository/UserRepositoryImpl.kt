package com.team.chord.core.data.repository

import com.team.chord.core.data.datasource.UserDataSource
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.Store
import com.team.chord.core.domain.repository.UserRepository
import com.team.chord.core.network.auth.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val tokenManager: TokenManager,
) : UserRepository {
    override suspend fun getStore(): Result<Store> =
        try {
            Result.Success(userDataSource.getStore())
        } catch (e: Exception) {
            Result.Error(e)
        }

    override suspend fun deleteMe(): Result<Unit> =
        try {
            userDataSource.deleteMe(tokenManager.getSocialWithdrawalToken())
            tokenManager.clearSocialWithdrawalToken()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }

    override suspend fun updateStore(
        name: String,
        employees: Int,
        laborCost: Int,
        rentCost: Int?,
        includeWeeklyHolidayPay: Boolean,
    ): Result<Unit> =
        try {
            userDataSource.updateStore(
                name = name,
                employees = employees,
                laborCost = laborCost,
                rentCost = rentCost,
                includeWeeklyHolidayPay = includeWeeklyHolidayPay,
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
}
