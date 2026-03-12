package com.team.chord.feature.setup

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.domain.model.menu.CheckDupResult
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.domain.model.menu.Menu
import com.team.chord.core.domain.model.menu.MenuRecipe
import com.team.chord.core.domain.model.menu.MenuTemplate
import com.team.chord.core.domain.model.menu.NewRecipeInfo
import com.team.chord.core.domain.repository.MenuRepository
import com.team.chord.feature.setup.ingredientinput.IngredientSourceType
import com.team.chord.feature.setup.ingredientinput.SelectedIngredient
import com.team.chord.feature.setup.menudetail.MenuCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OnboardingMenuViewModelTest {

    @Test
    fun registerMenus_treatsNewIngredientsWithTemporaryIdsAsNewRecipes() = runBlocking {
        val repository = FakeMenuRepository()
        val viewModel = OnboardingMenuViewModel(repository)

        viewModel.startNewMenu(
            name = "카페 라떼",
            isTemplateApplied = false,
            categoryCode = "BEVERAGE",
        )
        viewModel.updateMenuDetail(
            price = 6000,
            category = MenuCategory.BEVERAGE,
            preparationSeconds = 120,
        )
        viewModel.addIngredients(
            listOf(
                SelectedIngredient(
                    id = 1L,
                    name = "원두",
                    amount = 30,
                    unit = IngredientUnit.G,
                    price = 2000,
                    sourceType = IngredientSourceType.SAVED,
                ),
                SelectedIngredient(
                    id = 1773328030991L,
                    name = "새 시럽",
                    amount = 10,
                    unit = IngredientUnit.ML,
                    price = 1000,
                    categoryCode = "INGREDIENTS",
                    supplier = "납품사",
                    sourceType = IngredientSourceType.NEW,
                    baseQuantity = 100,
                ),
            ),
        )
        viewModel.completeCurrentMenu()

        val result = viewModel.registerMenus()

        assertTrue(result is Result.Success)
        assertEquals(1, repository.createMenuCalls.size)
        val call = repository.createMenuCalls.first()
        assertEquals(1, call.recipes?.size)
        assertEquals(1, call.newRecipes?.size)
        assertEquals("새 시럽", call.newRecipes?.first()?.ingredientName)
    }
}

private class FakeMenuRepository : MenuRepository {
    data class CreateMenuCall(
        val categoryCode: String,
        val menuName: String,
        val sellingPrice: Int,
        val workTime: Int,
        val recipes: List<MenuRecipe>?,
        val newRecipes: List<NewRecipeInfo>?,
    )

    val createMenuCalls = mutableListOf<CreateMenuCall>()

    override fun getMenuList(categoryCode: String?): Flow<List<Menu>> = emptyFlow()

    override suspend fun getMenuDetail(menuId: Long): Menu? = null

    override suspend fun createMenu(
        categoryCode: String,
        menuName: String,
        sellingPrice: Int,
        workTime: Int,
        recipes: List<MenuRecipe>?,
        newRecipes: List<NewRecipeInfo>?,
    ): Result<Unit> {
        createMenuCalls += CreateMenuCall(categoryCode, menuName, sellingPrice, workTime, recipes, newRecipes)
        return Result.Success(Unit)
    }

    override suspend fun updateMenuName(menuId: Long, name: String): Result<Unit> = Result.Success(Unit)

    override suspend fun updateMenuPrice(menuId: Long, price: Int): Result<Unit> = Result.Success(Unit)

    override suspend fun updateMenuPreparationTime(menuId: Long, seconds: Int): Result<Unit> = Result.Success(Unit)

    override suspend fun updateMenuCategory(menuId: Long, categoryCode: String): Result<Unit> = Result.Success(Unit)

    override suspend fun deleteMenu(menuId: Long): Result<Unit> = Result.Success(Unit)

    override suspend fun getMenuRecipes(menuId: Long): List<MenuRecipe> = emptyList()

    override suspend fun addExistingRecipe(menuId: Long, ingredientId: Long, amount: Int): Result<Unit> = Result.Success(Unit)

    override suspend fun addNewRecipe(
        menuId: Long,
        amount: Int,
        price: Int,
        unitCode: String,
        ingredientCategoryCode: String,
        ingredientName: String,
        supplier: String?,
    ): Result<Unit> = Result.Success(Unit)

    override suspend fun updateRecipeAmount(menuId: Long, recipeId: Long, amount: Double): Result<Unit> = Result.Success(Unit)

    override suspend fun deleteRecipes(menuId: Long, recipeIds: List<Long>): Result<Unit> = Result.Success(Unit)

    override fun getCategories(): Flow<List<Category>> = emptyFlow()

    override fun searchMenuTemplates(query: String): Flow<List<MenuTemplate>> = emptyFlow()

    override suspend fun getTemplateBasic(templateId: Long): MenuTemplate? = null

    override suspend fun getTemplateIngredients(templateId: Long): List<MenuRecipe> = emptyList()

    override suspend fun checkMenuDuplicate(menuName: String, ingredientNames: List<String>?): CheckDupResult {
        return CheckDupResult(
            menuNameDuplicate = false,
            dupIngredientNames = emptyList(),
        )
    }
}
