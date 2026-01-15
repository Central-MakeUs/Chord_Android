package com.team.chord.core.data.repository

import com.team.chord.core.data.datasource.IngredientDataSource
import com.team.chord.core.data.datasource.RecentSearchDataSource
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.model.ingredient.IngredientFilter
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

    override fun getIngredientList(): Flow<List<Ingredient>> =
        ingredientDataSource.getIngredientList()

    override fun getIngredientListByFilters(filters: Set<IngredientFilter>): Flow<List<Ingredient>> =
        ingredientDataSource.getIngredientListByFilters(filters)

    override suspend fun getIngredientDetail(ingredientId: Long): Ingredient? =
        ingredientDataSource.getIngredientDetail(ingredientId)

    override suspend fun updateIngredientPrice(ingredientId: Long, price: Int, unitAmount: Int): Result<Ingredient> {
        return updateIngredientField(ingredientId) {
            it.copy(price = price, unitAmount = unitAmount)
        }
    }

    override suspend fun updateIngredientSupplier(ingredientId: Long, supplier: String): Result<Ingredient> {
        return updateIngredientField(ingredientId) {
            it.copy(supplier = supplier)
        }
    }

    override suspend fun toggleFavorite(ingredientId: Long): Result<Ingredient> {
        return updateIngredientField(ingredientId) {
            it.copy(isFavorite = !it.isFavorite)
        }
    }

    override suspend fun deleteIngredient(ingredientId: Long): Result<Unit> {
        return try {
            ingredientDataSource.deleteIngredient(ingredientId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun addIngredientToList(ingredientId: Long): Result<Ingredient> {
        return try {
            val added = ingredientDataSource.addIngredientToList(ingredientId)
                ?: return Result.Error(NoSuchElementException("Ingredient not found: $ingredientId"))
            Result.Success(added)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun searchIngredients(query: String): Flow<List<Ingredient>> =
        ingredientDataSource.searchIngredients(query)

    override fun getRecentSearches(): Flow<List<RecentSearch>> =
        recentSearchDataSource.getRecentSearches()

    override suspend fun addRecentSearch(query: String) =
        recentSearchDataSource.addRecentSearch(query)

    override suspend fun deleteRecentSearch(id: Long) =
        recentSearchDataSource.deleteRecentSearch(id)

    override suspend fun clearRecentSearches() =
        recentSearchDataSource.clearRecentSearches()

    private suspend fun updateIngredientField(
        ingredientId: Long,
        transform: (Ingredient) -> Ingredient,
    ): Result<Ingredient> {
        return try {
            val ingredient = ingredientDataSource.getIngredientDetail(ingredientId)
                ?: return Result.Error(NoSuchElementException("Ingredient not found: $ingredientId"))

            val updatedIngredient = ingredientDataSource.updateIngredient(transform(ingredient))
            Result.Success(updatedIngredient)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
