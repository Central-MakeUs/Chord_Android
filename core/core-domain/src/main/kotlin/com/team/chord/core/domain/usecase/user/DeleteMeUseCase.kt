package com.team.chord.core.domain.usecase.user

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.repository.UserRepository
import javax.inject.Inject

class DeleteMeUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Result<Unit> = userRepository.deleteMe()
}
