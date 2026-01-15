package com.team.chord.core.data.datasource

import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.model.ingredient.IngredientFilter
import kotlinx.coroutines.flow.Flow

interface IngredientDataSource {
    fun getIngredientList(): Flow<List<Ingredient>>
    fun getIngredientListByFilters(filters: Set<IngredientFilter>): Flow<List<Ingredient>>
    suspend fun getIngredientDetail(ingredientId: Long): Ingredient?
    suspend fun updateIngredient(ingredient: Ingredient): Ingredient
    suspend fun deleteIngredient(ingredientId: Long)
    suspend fun addIngredientToList(ingredientId: Long): Ingredient?
    fun searchIngredients(query: String): Flow<List<Ingredient>>
    fun getAllIngredients(): Flow<List<Ingredient>>
}
