package com.team.chord.core.data.datasource

import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.model.ingredient.IngredientCategory
import com.team.chord.core.domain.model.ingredient.IngredientFilter
import com.team.chord.core.domain.model.menu.IngredientUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeIngredientDataSource @Inject constructor() : IngredientDataSource {

    private val myIngredients = MutableStateFlow(createInitialMyIngredients())
    private val allIngredients = MutableStateFlow(createAllIngredients())

    override fun getIngredientList(): Flow<List<Ingredient>> = myIngredients

    override fun getIngredientListByFilters(filters: Set<IngredientFilter>): Flow<List<Ingredient>> {
        return myIngredients.map { ingredients ->
            if (filters.isEmpty()) {
                ingredients
            } else {
                ingredients.filter { ingredient ->
                    filters.all { filter ->
                        when (filter) {
                            IngredientFilter.FAVORITE -> ingredient.isFavorite
                            IngredientFilter.FOOD_INGREDIENT -> ingredient.category == IngredientCategory.FOOD_MATERIAL
                            IngredientFilter.OPERATIONAL_SUPPLY -> ingredient.category == IngredientCategory.OPERATIONAL
                        }
                    }
                }
            }
        }
    }

    override suspend fun getIngredientDetail(ingredientId: Long): Ingredient? {
        return myIngredients.value.find { it.id == ingredientId }
            ?: allIngredients.value.find { it.id == ingredientId }
    }

    override suspend fun updateIngredient(ingredient: Ingredient): Ingredient {
        myIngredients.update { list ->
            list.map { if (it.id == ingredient.id) ingredient else it }
        }
        allIngredients.update { list ->
            list.map { if (it.id == ingredient.id) ingredient else it }
        }
        return ingredient
    }

    override suspend fun deleteIngredient(ingredientId: Long) {
        myIngredients.update { list -> list.filter { it.id != ingredientId } }
    }

    override suspend fun addIngredientToList(ingredientId: Long): Ingredient? {
        val ingredientToAdd = allIngredients.value.find { it.id == ingredientId }
        if (ingredientToAdd != null) {
            val alreadyExists = myIngredients.value.any { it.id == ingredientId }
            if (!alreadyExists) {
                myIngredients.update { list -> list + ingredientToAdd }
            }
        }
        return ingredientToAdd
    }

    override fun searchIngredients(query: String): Flow<List<Ingredient>> {
        return allIngredients.map { list ->
            if (query.isBlank()) {
                emptyList()
            } else {
                list.filter { ingredient ->
                    ingredient.name.contains(query, ignoreCase = true)
                }
            }
        }
    }

    override fun getAllIngredients(): Flow<List<Ingredient>> = allIngredients

    private fun createInitialMyIngredients(): List<Ingredient> = listOf(
        Ingredient(
            id = 1L,
            name = "우유",
            price = 4500,
            unitAmount = 1000,
            unit = IngredientUnit.ML,
            supplier = "서울우유",
            isFavorite = true,
            category = IngredientCategory.FOOD_MATERIAL,
        ),
        Ingredient(
            id = 2L,
            name = "설탕",
            price = 3000,
            unitAmount = 1000,
            unit = IngredientUnit.G,
            supplier = "CJ제일제당",
            isFavorite = false,
            category = IngredientCategory.FOOD_MATERIAL,
        ),
        Ingredient(
            id = 3L,
            name = "시럽",
            price = 8000,
            unitAmount = 500,
            unit = IngredientUnit.ML,
            supplier = "모닌",
            isFavorite = true,
            category = IngredientCategory.FOOD_MATERIAL,
        ),
        Ingredient(
            id = 4L,
            name = "초콜릿 가루",
            price = 12000,
            unitAmount = 500,
            unit = IngredientUnit.G,
            supplier = "기라델리",
            isFavorite = false,
            category = IngredientCategory.FOOD_MATERIAL,
        ),
        Ingredient(
            id = 5L,
            name = "생크림",
            price = 6500,
            unitAmount = 500,
            unit = IngredientUnit.ML,
            supplier = "서울우유",
            isFavorite = false,
            category = IngredientCategory.FOOD_MATERIAL,
        ),
        Ingredient(
            id = 6L,
            name = "바닐라 엑스트랙",
            price = 15000,
            unitAmount = 100,
            unit = IngredientUnit.ML,
            supplier = "매콜믹",
            isFavorite = true,
            category = IngredientCategory.FOOD_MATERIAL,
        ),
        Ingredient(
            id = 7L,
            name = "종이컵",
            price = 5000,
            unitAmount = 100,
            unit = IngredientUnit.EA,
            supplier = "컵월드",
            isFavorite = false,
            category = IngredientCategory.OPERATIONAL,
        ),
        Ingredient(
            id = 8L,
            name = "컵 홀더",
            price = 3000,
            unitAmount = 100,
            unit = IngredientUnit.EA,
            supplier = "컵월드",
            isFavorite = false,
            category = IngredientCategory.OPERATIONAL,
        ),
    )

    private fun createAllIngredients(): List<Ingredient> {
        val myList = createInitialMyIngredients()
        val additionalIngredients = listOf(
            Ingredient(
                id = 9L,
                name = "딸기",
                price = 15000,
                unitAmount = 500,
                unit = IngredientUnit.G,
                supplier = "농협",
                isFavorite = false,
                category = IngredientCategory.FOOD_MATERIAL,
            ),
            Ingredient(
                id = 10L,
                name = "원두",
                price = 25000,
                unitAmount = 1000,
                unit = IngredientUnit.G,
                supplier = "스타벅스",
                isFavorite = false,
                category = IngredientCategory.FOOD_MATERIAL,
            ),
            Ingredient(
                id = 11L,
                name = "아몬드 가루",
                price = 18000,
                unitAmount = 500,
                unit = IngredientUnit.G,
                supplier = "오뚜기",
                isFavorite = false,
                category = IngredientCategory.FOOD_MATERIAL,
            ),
            Ingredient(
                id = 12L,
                name = "카라멜 소스",
                price = 9500,
                unitAmount = 500,
                unit = IngredientUnit.ML,
                supplier = "토라니",
                isFavorite = false,
                category = IngredientCategory.FOOD_MATERIAL,
            ),
            Ingredient(
                id = 13L,
                name = "빨대",
                price = 2000,
                unitAmount = 200,
                unit = IngredientUnit.EA,
                supplier = "에코스토어",
                isFavorite = false,
                category = IngredientCategory.OPERATIONAL,
            ),
            Ingredient(
                id = 14L,
                name = "플라스틱 컵",
                price = 8000,
                unitAmount = 100,
                unit = IngredientUnit.EA,
                supplier = "컵월드",
                isFavorite = false,
                category = IngredientCategory.OPERATIONAL,
            ),
            Ingredient(
                id = 15L,
                name = "녹차 가루",
                price = 22000,
                unitAmount = 200,
                unit = IngredientUnit.G,
                supplier = "오설록",
                isFavorite = false,
                category = IngredientCategory.FOOD_MATERIAL,
            ),
            Ingredient(
                id = 16L,
                name = "꿀",
                price = 12000,
                unitAmount = 500,
                unit = IngredientUnit.ML,
                supplier = "꿀마을",
                isFavorite = false,
                category = IngredientCategory.FOOD_MATERIAL,
            ),
        )
        return myList + additionalIngredients
    }
}
