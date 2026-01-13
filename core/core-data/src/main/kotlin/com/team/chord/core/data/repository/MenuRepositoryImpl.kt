package com.team.chord.core.data.repository

import com.team.chord.core.data.datasource.MenuDataSource
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.domain.model.menu.Menu
import com.team.chord.core.domain.model.menu.MenuIngredient
import com.team.chord.core.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuRepositoryImpl @Inject constructor(
    private val menuDataSource: MenuDataSource,
) : MenuRepository {

    override fun getMenuList(): Flow<List<Menu>> =
        menuDataSource.getMenuList()

    override fun getMenuListByCategory(categoryId: Long): Flow<List<Menu>> =
        menuDataSource.getMenuListByCategory(categoryId)

    override suspend fun getMenuDetail(menuId: Long): Menu? =
        menuDataSource.getMenuDetail(menuId)

    override suspend fun updateMenuName(menuId: Long, name: String): Result<Menu> =
        updateMenuField(menuId) { it.copy(name = name) }

    override suspend fun updateMenuPrice(menuId: Long, price: Int): Result<Menu> =
        updateMenuField(menuId) { it.copy(price = price) }

    override suspend fun updateMenuPreparationTime(menuId: Long, seconds: Int): Result<Menu> =
        updateMenuField(menuId) { it.copy(preparationTimeSeconds = seconds) }

    override suspend fun updateMenuCategory(menuId: Long, categoryId: Long): Result<Menu> {
        return try {
            val menu = menuDataSource.getMenuDetail(menuId)
                ?: return Result.Error(NoSuchElementException("Menu not found: $menuId"))

            val categories = mutableListOf<Category>()
            menuDataSource.getCategories().collect { categories.addAll(it) }
            val category = categories.find { it.id == categoryId }
                ?: return Result.Error(NoSuchElementException("Category not found: $categoryId"))

            val updatedMenu = menuDataSource.updateMenu(menu.copy(category = category))
            Result.Success(updatedMenu)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteMenu(menuId: Long): Result<Unit> {
        return try {
            menuDataSource.deleteMenu(menuId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun addIngredientToMenu(
        menuId: Long,
        ingredient: MenuIngredient,
    ): Result<Menu> {
        return try {
            val updatedMenu = menuDataSource.addIngredientToMenu(menuId, ingredient)
                ?: return Result.Error(NoSuchElementException("Menu not found: $menuId"))
            Result.Success(updatedMenu)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateMenuIngredient(
        menuId: Long,
        ingredient: MenuIngredient,
    ): Result<Menu> {
        return try {
            val updatedMenu = menuDataSource.updateIngredientInMenu(menuId, ingredient)
                ?: return Result.Error(NoSuchElementException("Menu not found: $menuId"))
            Result.Success(updatedMenu)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun removeIngredientsFromMenu(
        menuId: Long,
        ingredientIds: List<Long>,
    ): Result<Menu> {
        return try {
            val updatedMenu = menuDataSource.removeIngredientsFromMenu(menuId, ingredientIds)
                ?: return Result.Error(NoSuchElementException("Menu not found: $menuId"))
            Result.Success(updatedMenu)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getCategories(): Flow<List<Category>> =
        menuDataSource.getCategories()

    private suspend fun updateMenuField(
        menuId: Long,
        transform: (Menu) -> Menu,
    ): Result<Menu> {
        return try {
            val menu = menuDataSource.getMenuDetail(menuId)
                ?: return Result.Error(NoSuchElementException("Menu not found: $menuId"))

            val updatedMenu = menuDataSource.updateMenu(transform(menu))
            Result.Success(updatedMenu)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
