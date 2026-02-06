package com.team.chord.core.data.datasource.remote

import com.team.chord.core.data.datasource.MenuDataSource
import com.team.chord.core.data.datasource.NewRecipeInput
import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.domain.model.menu.CheckDupResult
import com.team.chord.core.domain.model.menu.Menu
import com.team.chord.core.domain.model.menu.MenuRecipe
import com.team.chord.core.domain.model.menu.MenuTemplate
import com.team.chord.core.network.api.MenuApi
import com.team.chord.core.network.dto.menu.AmountUpdateDto
import com.team.chord.core.network.dto.menu.CheckDupRequestDto
import com.team.chord.core.network.dto.menu.DeleteRecipesDto
import com.team.chord.core.network.dto.menu.MenuCategoryUpdateDto
import com.team.chord.core.network.dto.menu.MenuCreateRequestDto
import com.team.chord.core.network.dto.menu.MenuNameUpdateDto
import com.team.chord.core.network.dto.menu.MenuPriceUpdateDto
import com.team.chord.core.network.dto.menu.MenuWorktimeUpdateDto
import com.team.chord.core.network.dto.menu.NewRecipeCreateRequestDto
import com.team.chord.core.network.dto.menu.RecipeCreateRequestDto
import com.team.chord.core.network.mapper.toDomain
import com.team.chord.core.network.util.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteMenuDataSource @Inject constructor(
    private val menuApi: MenuApi,
) : MenuDataSource {

    override fun getMenuList(categoryCode: String?): Flow<List<Menu>> = flow {
        val menus = safeApiCall { menuApi.getMenuList(categoryCode) }
        emit(menus.map { it.toDomain() })
    }

    override suspend fun getMenuDetail(menuId: Long): Menu? {
        return safeApiCall { menuApi.getMenuDetail(menuId) }.toDomain()
    }

    override suspend fun getMenuRecipes(menuId: Long): Pair<List<MenuRecipe>, Int> {
        val result = safeApiCall { menuApi.getMenuRecipes(menuId) }
        return result.recipes.map { it.toDomain() } to result.totalCost
    }

    override suspend fun checkMenuDuplicate(menuName: String, ingredientNames: List<String>?): CheckDupResult {
        return safeApiCall {
            menuApi.checkMenuDuplicate(CheckDupRequestDto(menuName, ingredientNames))
        }.toDomain()
    }

    override suspend fun createMenu(
        categoryCode: String,
        menuName: String,
        sellingPrice: Int,
        workTime: Int,
        recipes: List<Pair<Long?, Int>>?,
        newRecipes: List<NewRecipeInput>?,
    ) {
        safeApiCall {
            menuApi.createMenu(
                MenuCreateRequestDto(
                    menuCategoryCode = categoryCode,
                    menuName = menuName,
                    sellingPrice = sellingPrice,
                    workTime = workTime,
                    recipes = recipes?.map { RecipeCreateRequestDto(it.first, it.second) },
                    newRecipes = newRecipes?.map {
                        NewRecipeCreateRequestDto(
                            amount = it.amount,
                            price = it.price,
                            unitCode = it.unitCode,
                            ingredientCategoryCode = it.ingredientCategoryCode,
                            ingredientName = it.ingredientName,
                            supplier = it.supplier,
                        )
                    },
                )
            )
        }
    }

    override suspend fun addExistingRecipe(menuId: Long, ingredientId: Long?, amount: Int) {
        safeApiCall {
            menuApi.addExistingRecipe(menuId, RecipeCreateRequestDto(ingredientId, amount))
        }
    }

    override suspend fun addNewRecipe(
        menuId: Long,
        amount: Int,
        price: Int,
        unitCode: String,
        ingredientCategoryCode: String,
        ingredientName: String,
        supplier: String?,
    ) {
        safeApiCall {
            menuApi.addNewRecipe(
                menuId,
                NewRecipeCreateRequestDto(amount, price, unitCode, ingredientCategoryCode, ingredientName, supplier),
            )
        }
    }

    override suspend fun updateMenuName(menuId: Long, name: String) {
        safeApiCall { menuApi.updateMenuName(menuId, MenuNameUpdateDto(name)) }
    }

    override suspend fun updateMenuPrice(menuId: Long, price: Int) {
        safeApiCall { menuApi.updateMenuPrice(menuId, MenuPriceUpdateDto(price)) }
    }

    override suspend fun updateMenuCategory(menuId: Long, categoryCode: String) {
        safeApiCall { menuApi.updateMenuCategory(menuId, MenuCategoryUpdateDto(categoryCode)) }
    }

    override suspend fun updateMenuWorktime(menuId: Long, workTime: Int) {
        safeApiCall { menuApi.updateMenuWorktime(menuId, MenuWorktimeUpdateDto(workTime)) }
    }

    override suspend fun updateRecipeAmount(menuId: Long, recipeId: Long, amount: Int) {
        safeApiCall { menuApi.updateRecipeAmount(menuId, recipeId, AmountUpdateDto(amount)) }
    }

    override suspend fun deleteRecipes(menuId: Long, recipeIds: List<Long>?) {
        safeApiCall { menuApi.deleteRecipes(menuId, DeleteRecipesDto(recipeIds)) }
    }

    override suspend fun deleteMenu(menuId: Long) {
        safeApiCall { menuApi.deleteMenu(menuId) }
    }

    override fun getCategories(): Flow<List<Category>> = flow {
        val categories = safeApiCall { menuApi.getCategories() }
        emit(categories.map { it.toDomain() })
    }

    override fun searchMenuTemplates(query: String): Flow<List<MenuTemplate>> = flow {
        val templates = safeApiCall { menuApi.searchMenuTemplates(query) }
        emit(templates.map { it.toDomain() })
    }

    override suspend fun getTemplateBasic(templateId: Long): MenuTemplate {
        return safeApiCall { menuApi.getTemplateBasic(templateId) }.toDomain()
    }

    override suspend fun getTemplateIngredients(templateId: Long): List<MenuRecipe> {
        val recipeTemplates = safeApiCall { menuApi.getTemplateIngredients(templateId) }
        return recipeTemplates.map { recipe ->
            MenuRecipe(
                recipeId = 0,
                menuId = 0,
                ingredientId = 0,
                ingredientName = recipe.ingredientName,
                amount = recipe.defaultUsageAmount,
                unitCode = recipe.unitCode,
                price = recipe.defaultPrice,
            )
        }
    }
}
