package com.team.chord.core.domain.usecase.ingredient

import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.repository.IngredientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchMyIngredientsUseCase @Inject constructor(
    private val ingredientRepository: IngredientRepository,
) {
    operator fun invoke(query: String): Flow<List<Ingredient>> =
        ingredientRepository.searchMyIngredients(query)
}
