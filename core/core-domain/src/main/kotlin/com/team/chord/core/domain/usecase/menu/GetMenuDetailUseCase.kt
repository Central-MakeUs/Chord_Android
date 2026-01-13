package com.team.chord.core.domain.usecase.menu

import com.team.chord.core.domain.model.menu.Menu
import com.team.chord.core.domain.repository.MenuRepository
import javax.inject.Inject

class GetMenuDetailUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    suspend operator fun invoke(menuId: Long): Menu? = menuRepository.getMenuDetail(menuId)
}
