package com.team.chord.core.data.datasource

import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.domain.model.menu.Menu
import com.team.chord.core.domain.model.menu.MenuIngredient
import kotlinx.coroutines.flow.Flow

interface MenuDataSource {
    fun getMenuList(): Flow<List<Menu>>
    fun getMenuListByCategory(categoryId: Long): Flow<List<Menu>>
    suspend fun getMenuDetail(menuId: Long): Menu?
    suspend fun updateMenu(menu: Menu): Menu
    suspend fun deleteMenu(menuId: Long)
    suspend fun addIngredientToMenu(menuId: Long, ingredient: MenuIngredient): Menu?
    suspend fun updateIngredientInMenu(menuId: Long, ingredient: MenuIngredient): Menu?
    suspend fun removeIngredientsFromMenu(menuId: Long, ingredientIds: List<Long>): Menu?
    fun getCategories(): Flow<List<Category>>
}
