package com.team.chord.core.domain.usecase.menu

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.menu.Menu
import com.team.chord.core.domain.repository.MenuRepository
import javax.inject.Inject

class UpdateMenuNameUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    suspend operator fun invoke(menuId: Long, name: String): Result<Menu> =
        menuRepository.updateMenuName(menuId, name)
}

class UpdateMenuPriceUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    suspend operator fun invoke(menuId: Long, price: Int): Result<Menu> =
        menuRepository.updateMenuPrice(menuId, price)
}

class UpdateMenuPreparationTimeUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    suspend operator fun invoke(menuId: Long, seconds: Int): Result<Menu> =
        menuRepository.updateMenuPreparationTime(menuId, seconds)
}

class UpdateMenuCategoryUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    suspend operator fun invoke(menuId: Long, categoryId: Long): Result<Menu> =
        menuRepository.updateMenuCategory(menuId, categoryId)
}
