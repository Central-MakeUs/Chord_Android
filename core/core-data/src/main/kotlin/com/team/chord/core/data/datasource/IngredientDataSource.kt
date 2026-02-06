package com.team.chord.core.data.datasource

import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.model.ingredient.IngredientCategory
import com.team.chord.core.domain.model.ingredient.IngredientSearchResult
import com.team.chord.core.domain.model.ingredient.PriceHistoryItem
import kotlinx.coroutines.flow.Flow

interface IngredientDataSource {
    fun getIngredientList(categoryCode: String? = null): Flow<List<Ingredient>>
    suspend fun getIngredientDetail(ingredientId: Long): Ingredient?
    suspend fun getPriceHistory(ingredientId: Long): List<PriceHistoryItem>
    fun searchIngredients(query: String): Flow<List<IngredientSearchResult>>
    suspend fun checkDuplicate(name: String)
    fun searchMyIngredients(query: String): Flow<List<Ingredient>>
    suspend fun createIngredient(
        categoryCode: String,
        ingredientName: String,
        unitCode: String,
        price: Int,
        amount: Int,
        supplier: String?,
    )
    suspend fun toggleFavorite(ingredientId: Long, favorite: Boolean)
    suspend fun updateSupplier(ingredientId: Long, supplier: String?)
    suspend fun updateIngredient(ingredientId: Long, category: String, price: Int, amount: Int, unitCode: String)
    suspend fun deleteIngredient(ingredientId: Long)
    fun getCategories(): Flow<List<IngredientCategory>>
}
