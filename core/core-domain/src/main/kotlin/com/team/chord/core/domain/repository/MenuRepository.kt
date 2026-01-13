package com.team.chord.core.domain.repository

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.domain.model.menu.Menu
import com.team.chord.core.domain.model.menu.MenuIngredient
import kotlinx.coroutines.flow.Flow

interface MenuRepository {
    // 메뉴 목록
    fun getMenuList(): Flow<List<Menu>>
    fun getMenuListByCategory(categoryId: Long): Flow<List<Menu>>

    // 메뉴 상세
    suspend fun getMenuDetail(menuId: Long): Menu?

    // 메뉴 수정
    suspend fun updateMenuName(menuId: Long, name: String): Result<Menu>
    suspend fun updateMenuPrice(menuId: Long, price: Int): Result<Menu>
    suspend fun updateMenuPreparationTime(menuId: Long, seconds: Int): Result<Menu>
    suspend fun updateMenuCategory(menuId: Long, categoryId: Long): Result<Menu>

    // 메뉴 삭제
    suspend fun deleteMenu(menuId: Long): Result<Unit>

    // 재료 관리
    suspend fun addIngredientToMenu(menuId: Long, ingredient: MenuIngredient): Result<Menu>
    suspend fun updateMenuIngredient(menuId: Long, ingredient: MenuIngredient): Result<Menu>
    suspend fun removeIngredientsFromMenu(menuId: Long, ingredientIds: List<Long>): Result<Menu>

    // 카테고리
    fun getCategories(): Flow<List<Category>>
}
