package com.team.chord.core.data.datasource

import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.model.ingredient.IngredientCategory
import com.team.chord.core.domain.model.ingredient.IngredientSearchResult
import com.team.chord.core.domain.model.ingredient.PriceHistoryItem
import com.team.chord.core.domain.model.menu.IngredientUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeIngredientDataSource @Inject constructor() : IngredientDataSource {

    private val myIngredients = MutableStateFlow(createInitialMyIngredients())

    override fun getIngredientList(categoryCode: String?): Flow<List<Ingredient>> =
        if (categoryCode == null) myIngredients
        else myIngredients.map { list -> list.filter { it.categoryCode == categoryCode } }

    override suspend fun getIngredientDetail(ingredientId: Long): Ingredient? =
        myIngredients.value.find { it.id == ingredientId }

    override suspend fun getPriceHistory(ingredientId: Long): List<PriceHistoryItem> = listOf(
        PriceHistoryItem(1L, "25.11.12", 5000, 100, IngredientUnit.G),
        PriceHistoryItem(2L, "25.10.11", 4800, 100, IngredientUnit.G),
    )

    override fun searchIngredients(query: String): Flow<List<IngredientSearchResult>> =
        if (query.isBlank()) flowOf(emptyList())
        else myIngredients.map { list ->
            list.filter { it.name.contains(query, ignoreCase = true) }
                .map { IngredientSearchResult(isTemplate = false, ingredientId = it.id, ingredientName = it.name) }
        }

    override suspend fun checkDuplicate(name: String) {
        // no-op for fake - server returns error on dup
    }

    override fun searchMyIngredients(query: String): Flow<List<Ingredient>> =
        if (query.isBlank()) flowOf(emptyList())
        else myIngredients.map { list -> list.filter { it.name.contains(query, ignoreCase = true) } }

    override suspend fun createIngredient(
        categoryCode: String, ingredientName: String, unitCode: String,
        price: Int, amount: Int, supplier: String?,
    ): Ingredient {
        val newId = (myIngredients.value.maxOfOrNull { it.id } ?: 0) + 1
        val ingredient = Ingredient(
            id = newId, name = ingredientName, categoryCode = categoryCode,
            unit = IngredientUnit.entries.find { it.name == unitCode } ?: IngredientUnit.G,
            baseQuantity = amount, currentUnitPrice = price, supplier = supplier,
        )
        myIngredients.update { it + ingredient }
        return ingredient
    }

    override suspend fun toggleFavorite(ingredientId: Long, favorite: Boolean) {
        myIngredients.update { list ->
            list.map { if (it.id == ingredientId) it.copy(isFavorite = favorite) else it }
        }
    }

    override suspend fun updateSupplier(ingredientId: Long, supplier: String?) {
        myIngredients.update { list ->
            list.map { if (it.id == ingredientId) it.copy(supplier = supplier) else it }
        }
    }

    override suspend fun updateIngredient(ingredientId: Long, category: String, price: Int, amount: Int, unitCode: String) {
        myIngredients.update { list ->
            list.map {
                if (it.id == ingredientId) it.copy(
                    categoryCode = category, currentUnitPrice = price, baseQuantity = amount,
                    unit = IngredientUnit.entries.find { u -> u.name == unitCode } ?: it.unit,
                ) else it
            }
        }
    }

    override suspend fun deleteIngredient(ingredientId: Long) {
        myIngredients.update { list -> list.filter { it.id != ingredientId } }
    }

    override fun getCategories(): Flow<List<IngredientCategory>> = flowOf(
        listOf(
            IngredientCategory(code = "FOOD_MATERIAL", name = "식재료", displayOrder = 1),
            IngredientCategory(code = "OPERATIONAL", name = "운영 재료", displayOrder = 2),
        )
    )

    private fun createInitialMyIngredients(): List<Ingredient> = listOf(
        Ingredient(1L, "우유", "FOOD_MATERIAL", IngredientUnit.ML, 1000, 4500, "서울우유", true),
        Ingredient(2L, "설탕", "FOOD_MATERIAL", IngredientUnit.G, 1000, 3000, "CJ제일제당", false),
        Ingredient(3L, "시럽", "FOOD_MATERIAL", IngredientUnit.ML, 500, 8000, "모닌", true),
        Ingredient(4L, "초콜릿 가루", "FOOD_MATERIAL", IngredientUnit.G, 500, 12000, "기라델리", false),
        Ingredient(5L, "생크림", "FOOD_MATERIAL", IngredientUnit.ML, 500, 6500, "서울우유", false),
        Ingredient(6L, "바닐라 엑스트랙", "FOOD_MATERIAL", IngredientUnit.ML, 100, 15000, "매콜믹", true),
        Ingredient(7L, "종이컵", "OPERATIONAL", IngredientUnit.EA, 100, 5000, "컵월드", false),
        Ingredient(8L, "컵 홀더", "OPERATIONAL", IngredientUnit.EA, 100, 3000, "컵월드", false),
    )
}
