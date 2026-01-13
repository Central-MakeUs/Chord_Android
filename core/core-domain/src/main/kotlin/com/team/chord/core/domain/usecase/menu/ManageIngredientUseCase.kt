package com.team.chord.core.domain.usecase.menu

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.menu.Menu
import com.team.chord.core.domain.model.menu.MenuIngredient
import com.team.chord.core.domain.repository.MenuRepository
import javax.inject.Inject

class AddIngredientUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    suspend operator fun invoke(menuId: Long, ingredient: MenuIngredient): Result<Menu> =
        menuRepository.addIngredientToMenu(menuId, ingredient)
}

class UpdateIngredientUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    suspend operator fun invoke(menuId: Long, ingredient: MenuIngredient): Result<Menu> =
        menuRepository.updateMenuIngredient(menuId, ingredient)
}

class RemoveIngredientsUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    suspend operator fun invoke(menuId: Long, ingredientIds: List<Long>): Result<Menu> =
        menuRepository.removeIngredientsFromMenu(menuId, ingredientIds)
}
