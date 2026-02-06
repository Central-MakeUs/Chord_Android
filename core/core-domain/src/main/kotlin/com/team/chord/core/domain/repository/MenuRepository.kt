package com.team.chord.core.domain.repository

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.domain.model.menu.CheckDupResult
import com.team.chord.core.domain.model.menu.Menu
import com.team.chord.core.domain.model.menu.MenuRecipe
import com.team.chord.core.domain.model.menu.MenuTemplate
import kotlinx.coroutines.flow.Flow

interface MenuRepository {
    fun getMenuList(categoryCode: String? = null): Flow<List<Menu>>
    suspend fun getMenuDetail(menuId: Long): Menu?
    suspend fun createMenu(
        categoryCode: String,
        menuName: String,
        sellingPrice: Int,
        workTime: Int,
        recipes: List<MenuRecipe>? = null,
    ): Result<Unit>
    suspend fun updateMenuName(menuId: Long, name: String): Result<Unit>
    suspend fun updateMenuPrice(menuId: Long, price: Int): Result<Unit>
    suspend fun updateMenuPreparationTime(menuId: Long, seconds: Int): Result<Unit>
    suspend fun updateMenuCategory(menuId: Long, categoryCode: String): Result<Unit>
    suspend fun deleteMenu(menuId: Long): Result<Unit>
    suspend fun getMenuRecipes(menuId: Long): List<MenuRecipe>
    suspend fun addExistingRecipe(menuId: Long, ingredientId: Long, amount: Int): Result<Unit>
    suspend fun addNewRecipe(
        menuId: Long,
        amount: Int,
        price: Int,
        unitCode: String,
        ingredientCategoryCode: String,
        ingredientName: String,
        supplier: String? = null,
    ): Result<Unit>
    suspend fun updateRecipeAmount(menuId: Long, recipeId: Long, amount: Int): Result<Unit>
    suspend fun deleteRecipes(menuId: Long, recipeIds: List<Long>): Result<Unit>
    fun getCategories(): Flow<List<Category>>
    fun searchMenuTemplates(query: String): Flow<List<MenuTemplate>>
    suspend fun getTemplateBasic(templateId: Long): MenuTemplate?
    suspend fun checkMenuDuplicate(menuName: String, ingredientNames: List<String>? = null): CheckDupResult
}
