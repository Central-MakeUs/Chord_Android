package com.team.chord.core.domain.usecase.menu

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.repository.MenuRepository
import javax.inject.Inject

class DeleteMenuUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    suspend operator fun invoke(menuId: Long): Result<Unit> =
        menuRepository.deleteMenu(menuId)
}
