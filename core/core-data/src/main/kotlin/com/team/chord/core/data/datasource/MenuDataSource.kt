package com.team.chord.core.data.datasource

import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.domain.model.menu.CheckDupResult
import com.team.chord.core.domain.model.menu.Menu
import com.team.chord.core.domain.model.menu.MenuRecipe
import com.team.chord.core.domain.model.menu.MenuTemplate
import kotlinx.coroutines.flow.Flow

interface MenuDataSource {
    fun getMenuList(categoryCode: String? = null): Flow<List<Menu>>
    suspend fun getMenuDetail(menuId: Long): Menu?
    suspend fun getMenuRecipes(menuId: Long): Pair<List<MenuRecipe>, Int>
    suspend fun checkMenuDuplicate(menuName: String, ingredientNames: List<String>?): CheckDupResult
    suspend fun createMenu(
        categoryCode: String,
        menuName: String,
        sellingPrice: Int,
        workTime: Int,
        recipes: List<Pair<Long?, Int>>?,
        newRecipes: List<NewRecipeInput>?,
    )
    suspend fun addExistingRecipe(menuId: Long, ingredientId: Long?, amount: Int)
    suspend fun addNewRecipe(
        menuId: Long,
        amount: Int,
        price: Int,
        unitCode: String,
        ingredientCategoryCode: String,
        ingredientName: String,
        supplier: String?,
    )
    suspend fun updateMenuName(menuId: Long, name: String)
    suspend fun updateMenuPrice(menuId: Long, price: Int)
    suspend fun updateMenuCategory(menuId: Long, categoryCode: String)
    suspend fun updateMenuWorktime(menuId: Long, workTime: Int)
    suspend fun updateRecipeAmount(menuId: Long, recipeId: Long, amount: Int)
    suspend fun deleteRecipes(menuId: Long, recipeIds: List<Long>?)
    suspend fun deleteMenu(menuId: Long)
    fun getCategories(): Flow<List<Category>>
    fun searchMenuTemplates(query: String): Flow<List<MenuTemplate>>
    suspend fun getTemplateBasic(templateId: Long): MenuTemplate
    suspend fun getTemplateIngredients(templateId: Long): List<MenuRecipe>
}

data class NewRecipeInput(
    val amount: Int,
    val price: Int,
    val unitCode: String,
    val ingredientCategoryCode: String,
    val ingredientName: String,
    val supplier: String? = null,
)
