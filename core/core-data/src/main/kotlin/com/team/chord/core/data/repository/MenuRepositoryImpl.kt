package com.team.chord.core.data.repository

import com.team.chord.core.data.datasource.MenuDataSource
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.domain.model.menu.CheckDupResult
import com.team.chord.core.domain.model.menu.Menu
import com.team.chord.core.domain.model.menu.MenuRecipe
import com.team.chord.core.domain.model.menu.MenuTemplate
import com.team.chord.core.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuRepositoryImpl @Inject constructor(
    private val menuDataSource: MenuDataSource,
) : MenuRepository {

    override fun getMenuList(categoryCode: String?): Flow<List<Menu>> =
        menuDataSource.getMenuList(categoryCode)

    override suspend fun getMenuDetail(menuId: Long): Menu? =
        menuDataSource.getMenuDetail(menuId)

    override suspend fun createMenu(
        categoryCode: String,
        menuName: String,
        sellingPrice: Int,
        workTime: Int,
        recipes: List<MenuRecipe>?,
    ): Result<Unit> = runCatching {
        val recipesList = recipes?.map { (it.ingredientId as Long?) to it.amount }
        menuDataSource.createMenu(categoryCode, menuName, sellingPrice, workTime, recipesList, null)
    }

    override suspend fun updateMenuName(menuId: Long, name: String): Result<Unit> = runCatching {
        menuDataSource.updateMenuName(menuId, name)
    }

    override suspend fun updateMenuPrice(menuId: Long, price: Int): Result<Unit> = runCatching {
        menuDataSource.updateMenuPrice(menuId, price)
    }

    override suspend fun updateMenuPreparationTime(menuId: Long, seconds: Int): Result<Unit> = runCatching {
        menuDataSource.updateMenuWorktime(menuId, seconds)
    }

    override suspend fun updateMenuCategory(menuId: Long, categoryCode: String): Result<Unit> = runCatching {
        menuDataSource.updateMenuCategory(menuId, categoryCode)
    }

    override suspend fun deleteMenu(menuId: Long): Result<Unit> = runCatching {
        menuDataSource.deleteMenu(menuId)
    }

    override suspend fun getMenuRecipes(menuId: Long): List<MenuRecipe> =
        menuDataSource.getMenuRecipes(menuId).first

    override suspend fun addExistingRecipe(menuId: Long, ingredientId: Long, amount: Int): Result<Unit> = runCatching {
        menuDataSource.addExistingRecipe(menuId, ingredientId, amount)
    }

    override suspend fun addNewRecipe(
        menuId: Long,
        amount: Int,
        price: Int,
        unitCode: String,
        ingredientCategoryCode: String,
        ingredientName: String,
        supplier: String?,
    ): Result<Unit> = runCatching {
        menuDataSource.addNewRecipe(menuId, amount, price, unitCode, ingredientCategoryCode, ingredientName, supplier)
    }

    override suspend fun updateRecipeAmount(menuId: Long, recipeId: Long, amount: Int): Result<Unit> = runCatching {
        menuDataSource.updateRecipeAmount(menuId, recipeId, amount)
    }

    override suspend fun deleteRecipes(menuId: Long, recipeIds: List<Long>): Result<Unit> = runCatching {
        menuDataSource.deleteRecipes(menuId, recipeIds)
    }

    override fun getCategories(): Flow<List<Category>> =
        menuDataSource.getCategories()

    override fun searchMenuTemplates(query: String): Flow<List<MenuTemplate>> =
        menuDataSource.searchMenuTemplates(query)

    override suspend fun getTemplateBasic(templateId: Long): MenuTemplate? =
        menuDataSource.getTemplateBasic(templateId)

    override suspend fun checkMenuDuplicate(
        menuName: String,
        ingredientNames: List<String>?,
    ): CheckDupResult = menuDataSource.checkMenuDuplicate(menuName, ingredientNames)

    private inline fun runCatching(block: () -> Unit): Result<Unit> {
        return try {
            block()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
