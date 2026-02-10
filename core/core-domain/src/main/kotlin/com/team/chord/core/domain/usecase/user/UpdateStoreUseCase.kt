package com.team.chord.core.domain.usecase.user

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.repository.UserRepository
import javax.inject.Inject

class UpdateStoreUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        name: String,
        employees: Int,
        laborCost: Int,
        rentCost: Int? = null,
        includeWeeklyHolidayPay: Boolean,
    ): Result<Unit> =
        userRepository.updateStore(
            name = name,
            employees = employees,
            laborCost = laborCost,
            rentCost = rentCost,
            includeWeeklyHolidayPay = includeWeeklyHolidayPay,
        )
}
