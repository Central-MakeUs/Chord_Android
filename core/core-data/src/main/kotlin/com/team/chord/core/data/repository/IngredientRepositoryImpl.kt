package com.team.chord.core.data.repository

import com.team.chord.core.data.datasource.IngredientDataSource
import com.team.chord.core.data.datasource.RecentSearchDataSource
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.model.ingredient.IngredientCategory
import com.team.chord.core.domain.model.ingredient.IngredientSearchResult
import com.team.chord.core.domain.model.ingredient.PriceHistoryItem
import com.team.chord.core.domain.model.ingredient.RecentSearch
import com.team.chord.core.domain.repository.IngredientRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IngredientRepositoryImpl @Inject constructor(
    private val ingredientDataSource: IngredientDataSource,
    private val recentSearchDataSource: RecentSearchDataSource,
) : IngredientRepository {

    override fun getIngredientList(categoryCode: String?): Flow<List<Ingredient>> =
        ingredientDataSource.getIngredientList(categoryCode)

    override suspend fun getIngredientDetail(ingredientId: Long): Ingredient? =
        ingredientDataSource.getIngredientDetail(ingredientId)

    override suspend fun getPriceHistory(ingredientId: Long): List<PriceHistoryItem> =
        ingredientDataSource.getPriceHistory(ingredientId)

    override fun getCategories(): Flow<List<IngredientCategory>> =
        ingredientDataSource.getCategories()

    override suspend fun updateIngredient(
        ingredientId: Long,
        categoryCode: String,
        price: Int,
        amount: Int,
        unitCode: String,
    ): Result<Unit> = runCatching {
        ingredientDataSource.updateIngredient(ingredientId, categoryCode, price, amount, unitCode)
    }

    override suspend fun updateSupplier(ingredientId: Long, supplier: String): Result<Unit> = runCatching {
        ingredientDataSource.updateSupplier(ingredientId, supplier)
    }

    override suspend fun setFavorite(ingredientId: Long, favorite: Boolean): Result<Unit> = runCatching {
        ingredientDataSource.toggleFavorite(ingredientId, favorite)
    }

    override suspend fun deleteIngredient(ingredientId: Long): Result<Unit> = runCatching {
        ingredientDataSource.deleteIngredient(ingredientId)
    }

    override suspend fun createIngredient(
        categoryCode: String,
        ingredientName: String,
        unitCode: String,
        price: Int,
        amount: Int,
        supplier: String?,
    ): Result<Ingredient> = runCatching {
        ingredientDataSource.createIngredient(categoryCode, ingredientName, unitCode, price, amount, supplier)
    }

    override fun searchIngredients(query: String): Flow<List<IngredientSearchResult>> =
        ingredientDataSource.searchIngredients(query)

    override suspend fun checkDuplicate(name: String): Result<Unit> = runCatching {
        ingredientDataSource.checkDuplicate(name)
    }

    override fun searchMyIngredients(query: String): Flow<List<Ingredient>> =
        ingredientDataSource.searchMyIngredients(query)

    override fun getRecentSearches(): Flow<List<RecentSearch>> =
        recentSearchDataSource.getRecentSearches()

    override suspend fun addRecentSearch(query: String) =
        recentSearchDataSource.addRecentSearch(query)

    override suspend fun deleteRecentSearch(id: Long) =
        recentSearchDataSource.deleteRecentSearch(id)

    override suspend fun clearRecentSearches() =
        recentSearchDataSource.clearRecentSearches()

    private inline fun <T> runCatching(block: () -> T): Result<T> {
        return try {
            Result.Success(block())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
