package com.team.chord.core.domain.usecase.ingredient

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.repository.IngredientRepository
import javax.inject.Inject

class UpdateIngredientUseCase @Inject constructor(
    private val ingredientRepository: IngredientRepository,
) {
    suspend fun updateIngredient(
        ingredientId: Long,
        categoryCode: String,
        price: Int,
        amount: Int,
        unitCode: String,
    ): Result<Unit> = ingredientRepository.updateIngredient(ingredientId, categoryCode, price, amount, unitCode)

    suspend fun updateSupplier(ingredientId: Long, supplier: String): Result<Unit> =
        ingredientRepository.updateSupplier(ingredientId, supplier)

    suspend fun setFavorite(ingredientId: Long, favorite: Boolean): Result<Unit> =
        ingredientRepository.setFavorite(ingredientId, favorite)
}
