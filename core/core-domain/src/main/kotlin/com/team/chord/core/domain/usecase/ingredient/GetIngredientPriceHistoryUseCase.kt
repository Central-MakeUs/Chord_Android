package com.team.chord.core.domain.usecase.ingredient

import com.team.chord.core.domain.model.ingredient.PriceHistoryItem
import com.team.chord.core.domain.repository.IngredientRepository
import javax.inject.Inject

class GetIngredientPriceHistoryUseCase @Inject constructor(
    private val ingredientRepository: IngredientRepository,
) {
    suspend operator fun invoke(ingredientId: Long): List<PriceHistoryItem> =
        ingredientRepository.getPriceHistory(ingredientId)
}
