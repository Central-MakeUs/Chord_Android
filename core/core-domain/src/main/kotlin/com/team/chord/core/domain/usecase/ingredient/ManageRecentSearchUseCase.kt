package com.team.chord.core.domain.usecase.ingredient

import com.team.chord.core.domain.model.ingredient.RecentSearch
import com.team.chord.core.domain.repository.IngredientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManageRecentSearchUseCase @Inject constructor(
    private val ingredientRepository: IngredientRepository,
) {
    fun getRecentSearches(): Flow<List<RecentSearch>> =
        ingredientRepository.getRecentSearches()

    suspend fun addRecentSearch(query: String) =
        ingredientRepository.addRecentSearch(query)

    suspend fun deleteRecentSearch(id: Long) =
        ingredientRepository.deleteRecentSearch(id)
}
