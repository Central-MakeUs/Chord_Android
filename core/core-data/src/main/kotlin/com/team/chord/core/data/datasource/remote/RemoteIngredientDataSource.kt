package com.team.chord.core.data.datasource.remote

import com.team.chord.core.data.datasource.IngredientDataSource
import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.model.ingredient.IngredientCategory
import com.team.chord.core.domain.model.ingredient.IngredientSearchResult
import com.team.chord.core.domain.model.ingredient.PriceHistoryItem
import com.team.chord.core.network.api.IngredientApi
import com.team.chord.core.network.dto.ingredient.IngredientCreateRequestDto
import com.team.chord.core.network.dto.ingredient.IngredientUpdateDto
import com.team.chord.core.network.dto.ingredient.SupplierUpdateDto
import com.team.chord.core.network.mapper.toDomain
import com.team.chord.core.network.util.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteIngredientDataSource @Inject constructor(
    private val ingredientApi: IngredientApi,
) : IngredientDataSource {

    override fun getIngredientList(categoryCode: String?): Flow<List<Ingredient>> = flow {
        val categories = categoryCode?.let(::listOf)
        val ingredients = safeApiCall { ingredientApi.getIngredientList(categories) }
        emit(ingredients.map { it.toDomain() })
    }

    override suspend fun getIngredientDetail(ingredientId: Long): Ingredient? {
        return safeApiCall { ingredientApi.getIngredientDetail(ingredientId) }.toDomain()
    }

    override suspend fun getPriceHistory(ingredientId: Long): List<PriceHistoryItem> {
        return safeApiCall { ingredientApi.getPriceHistory(ingredientId) }.map { it.toDomain() }
    }

    override fun searchIngredients(query: String): Flow<List<IngredientSearchResult>> = flow {
        val results = safeApiCall { ingredientApi.searchIngredients(query) }
        emit(results.map { it.toDomain() })
    }

    override suspend fun checkDuplicate(name: String) {
        safeApiCall { ingredientApi.checkDuplicate(name) }
    }

    override fun searchMyIngredients(query: String): Flow<List<Ingredient>> = flow {
        val results = safeApiCall { ingredientApi.searchMyIngredients(query) }
        emit(results.map { dto ->
            Ingredient(
                id = dto.ingredientId,
                name = dto.ingredientName,
                categoryCode = "",
                unit = com.team.chord.core.domain.model.menu.IngredientUnit.G,
                baseQuantity = 0,
                currentUnitPrice = 0,
            )
        })
    }

    override suspend fun createIngredient(
        categoryCode: String,
        ingredientName: String,
        unitCode: String,
        price: Int,
        amount: Int,
        supplier: String?,
    ) {
        safeApiCall {
            ingredientApi.createIngredient(
                IngredientCreateRequestDto(categoryCode, ingredientName, unitCode, price, amount, supplier)
            )
        }
    }

    override suspend fun toggleFavorite(ingredientId: Long, favorite: Boolean) {
        safeApiCall { ingredientApi.toggleFavorite(ingredientId, favorite) }
    }

    override suspend fun updateSupplier(ingredientId: Long, supplier: String?) {
        safeApiCall { ingredientApi.updateSupplier(ingredientId, SupplierUpdateDto(supplier)) }
    }

    override suspend fun updateIngredient(ingredientId: Long, category: String, price: Int, amount: Int, unitCode: String) {
        safeApiCall {
            ingredientApi.updateIngredient(ingredientId, IngredientUpdateDto(category, price, amount, unitCode))
        }
    }

    override suspend fun deleteIngredient(ingredientId: Long) {
        safeApiCall { ingredientApi.deleteIngredient(ingredientId) }
    }

    override fun getCategories(): Flow<List<IngredientCategory>> = flow {
        val categories = safeApiCall { ingredientApi.getCategories() }
        emit(categories.map { it.toDomain() })
    }
}
