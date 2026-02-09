package com.team.chord.core.domain.usecase.menu

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.menu.MenuRecipe
import com.team.chord.core.domain.repository.MenuRepository
import javax.inject.Inject

class GetMenuRecipesUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    suspend operator fun invoke(menuId: Long): List<MenuRecipe> =
        menuRepository.getMenuRecipes(menuId)
}

class AddExistingRecipeUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    suspend operator fun invoke(menuId: Long, ingredientId: Long, amount: Int): Result<Unit> =
        menuRepository.addExistingRecipe(menuId, ingredientId, amount)
}

class AddNewRecipeUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    suspend operator fun invoke(
        menuId: Long,
        amount: Int,
        price: Int,
        unitCode: String,
        ingredientCategoryCode: String,
        ingredientName: String,
        supplier: String? = null,
    ): Result<Unit> = menuRepository.addNewRecipe(
        menuId, amount, price, unitCode, ingredientCategoryCode, ingredientName, supplier,
    )
}

class DeleteRecipesUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    suspend operator fun invoke(menuId: Long, recipeIds: List<Long>): Result<Unit> =
        menuRepository.deleteRecipes(menuId, recipeIds)
}

class UpdateRecipeAmountUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    suspend operator fun invoke(menuId: Long, recipeId: Long, amount: Int): Result<Unit> =
        menuRepository.updateRecipeAmount(menuId, recipeId, amount)
}
