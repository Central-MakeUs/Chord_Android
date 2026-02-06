package com.team.chord.core.domain.repository

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.model.ingredient.IngredientCategory
import com.team.chord.core.domain.model.ingredient.IngredientSearchResult
import com.team.chord.core.domain.model.ingredient.PriceHistoryItem
import com.team.chord.core.domain.model.ingredient.RecentSearch
import kotlinx.coroutines.flow.Flow

interface IngredientRepository {
    fun getIngredientList(categoryCode: String? = null): Flow<List<Ingredient>>
    suspend fun getIngredientDetail(ingredientId: Long): Ingredient?
    suspend fun getPriceHistory(ingredientId: Long): List<PriceHistoryItem>
    fun getCategories(): Flow<List<IngredientCategory>>
    suspend fun updateIngredient(
        ingredientId: Long,
        categoryCode: String,
        price: Int,
        amount: Int,
        unitCode: String,
    ): Result<Unit>
    suspend fun updateSupplier(ingredientId: Long, supplier: String): Result<Unit>
    suspend fun setFavorite(ingredientId: Long, favorite: Boolean): Result<Unit>
    suspend fun deleteIngredient(ingredientId: Long): Result<Unit>
    suspend fun createIngredient(
        categoryCode: String,
        ingredientName: String,
        unitCode: String,
        price: Int,
        amount: Int,
        supplier: String? = null,
    ): Result<Unit>
    fun searchIngredients(query: String): Flow<List<IngredientSearchResult>>
    suspend fun checkDuplicate(name: String): Result<Unit>
    fun searchMyIngredients(query: String): Flow<List<Ingredient>>
    fun getRecentSearches(): Flow<List<RecentSearch>>
    suspend fun addRecentSearch(query: String)
    suspend fun deleteRecentSearch(id: Long)
    suspend fun clearRecentSearches()
}
