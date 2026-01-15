package com.team.chord.core.domain.usecase.ingredient

import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.model.ingredient.IngredientFilter
import com.team.chord.core.domain.repository.IngredientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetIngredientListUseCase @Inject constructor(
    private val ingredientRepository: IngredientRepository,
) {
    operator fun invoke(): Flow<List<Ingredient>> =
        ingredientRepository.getIngredientList()

    operator fun invoke(filters: Set<IngredientFilter>): Flow<List<Ingredient>> =
        ingredientRepository.getIngredientListByFilters(filters)
}
