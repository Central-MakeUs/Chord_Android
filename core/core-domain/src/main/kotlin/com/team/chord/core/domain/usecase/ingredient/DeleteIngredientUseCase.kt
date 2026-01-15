package com.team.chord.core.domain.usecase.ingredient

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.repository.IngredientRepository
import javax.inject.Inject

class DeleteIngredientUseCase @Inject constructor(
    private val ingredientRepository: IngredientRepository,
) {
    suspend operator fun invoke(ingredientId: Long): Result<Unit> =
        ingredientRepository.deleteIngredient(ingredientId)
}
