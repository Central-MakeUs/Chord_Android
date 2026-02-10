package com.team.chord.core.data.datasource.remote

import com.team.chord.core.data.datasource.UserDataSource
import com.team.chord.core.network.api.UserApi
import com.team.chord.core.network.dto.user.UpdateStoreRequestDto
import com.team.chord.core.network.util.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteUserDataSource @Inject constructor(
    private val userApi: UserApi,
) : UserDataSource {
    override suspend fun deleteMe() {
        safeApiCall { userApi.deleteMe() }
    }

    override suspend fun updateStore(
        name: String,
        employees: Int,
        laborCost: Int,
        rentCost: Int?,
        includeWeeklyHolidayPay: Boolean,
    ) {
        safeApiCall {
            userApi.updateStore(
                UpdateStoreRequestDto(
                    name = name,
                    employees = employees,
                    laborCost = laborCost,
                    rentCost = rentCost,
                    includeWeeklyHolidayPay = includeWeeklyHolidayPay,
                ),
            )
        }
    }
}
