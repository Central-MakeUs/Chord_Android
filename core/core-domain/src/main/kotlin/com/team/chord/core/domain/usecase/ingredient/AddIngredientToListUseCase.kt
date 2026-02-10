package com.team.chord.core.domain.usecase.ingredient

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.repository.IngredientRepository
import javax.inject.Inject

class AddIngredientToListUseCase @Inject constructor(
    private val ingredientRepository: IngredientRepository,
) {
    suspend operator fun invoke(
        categoryCode: String,
        ingredientName: String,
        unitCode: String,
        price: Int,
        amount: Int,
        supplier: String? = null,
    ): Result<Ingredient> = ingredientRepository.createIngredient(
        categoryCode = categoryCode,
        ingredientName = ingredientName,
        unitCode = unitCode,
        price = price,
        amount = amount,
        supplier = supplier,
    )
}
