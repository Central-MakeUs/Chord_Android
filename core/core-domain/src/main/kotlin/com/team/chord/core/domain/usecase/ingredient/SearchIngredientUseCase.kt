package com.team.chord.core.domain.usecase.ingredient

import com.team.chord.core.domain.model.ingredient.IngredientSearchResult
import com.team.chord.core.domain.repository.IngredientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchIngredientUseCase @Inject constructor(
    private val ingredientRepository: IngredientRepository,
) {
    operator fun invoke(query: String): Flow<List<IngredientSearchResult>> =
        ingredientRepository.searchIngredients(query)
}
