package com.team.chord.core.domain.repository

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.model.ingredient.IngredientFilter
import com.team.chord.core.domain.model.ingredient.RecentSearch
import kotlinx.coroutines.flow.Flow

interface IngredientRepository {
    // 재료 목록
    fun getIngredientList(): Flow<List<Ingredient>>
    fun getIngredientListByFilters(filters: Set<IngredientFilter>): Flow<List<Ingredient>>

    // 재료 상세
    suspend fun getIngredientDetail(ingredientId: Long): Ingredient?

    // 재료 수정
    suspend fun updateIngredientPrice(ingredientId: Long, price: Int, unitAmount: Int): Result<Ingredient>
    suspend fun updateIngredientSupplier(ingredientId: Long, supplier: String): Result<Ingredient>
    suspend fun toggleFavorite(ingredientId: Long): Result<Ingredient>

    // 재료 삭제
    suspend fun deleteIngredient(ingredientId: Long): Result<Unit>

    // 재료 추가 (검색 결과에서)
    suspend fun addIngredientToList(ingredientId: Long): Result<Ingredient>

    // 검색
    fun searchIngredients(query: String): Flow<List<Ingredient>>

    // 최근 검색
    fun getRecentSearches(): Flow<List<RecentSearch>>
    suspend fun addRecentSearch(query: String)
    suspend fun deleteRecentSearch(id: Long)
    suspend fun clearRecentSearches()
}
