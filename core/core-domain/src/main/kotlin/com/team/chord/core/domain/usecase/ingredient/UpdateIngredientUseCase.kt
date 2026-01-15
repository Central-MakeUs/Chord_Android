package com.team.chord.core.domain.usecase.ingredient

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.repository.IngredientRepository
import javax.inject.Inject

class UpdateIngredientUseCase @Inject constructor(
    private val ingredientRepository: IngredientRepository,
) {
    suspend fun updatePrice(ingredientId: Long, price: Int, unitAmount: Int): Result<Ingredient> =
        ingredientRepository.updateIngredientPrice(ingredientId, price, unitAmount)

    suspend fun updateSupplier(ingredientId: Long, supplier: String): Result<Ingredient> =
        ingredientRepository.updateIngredientSupplier(ingredientId, supplier)

    suspend fun toggleFavorite(ingredientId: Long): Result<Ingredient> =
        ingredientRepository.toggleFavorite(ingredientId)
}
