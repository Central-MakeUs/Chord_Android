package com.team.chord.core.domain.usecase.ingredient

import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.repository.IngredientRepository
import javax.inject.Inject

class GetIngredientDetailUseCase @Inject constructor(
    private val ingredientRepository: IngredientRepository,
) {
    suspend operator fun invoke(ingredientId: Long): Ingredient? =
        ingredientRepository.getIngredientDetail(ingredientId)
}
