package com.team.chord.core.domain.usecase.menu

import com.team.chord.core.domain.model.menu.Menu
import com.team.chord.core.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMenuListUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    operator fun invoke(categoryCode: String? = null): Flow<List<Menu>> =
        menuRepository.getMenuList(categoryCode)
}
