package com.team.chord.feature.menu.ingredient

import androidx.lifecycle.SavedStateHandle
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.model.ingredient.IngredientCategory
import com.team.chord.core.domain.model.ingredient.IngredientSearchResult
import com.team.chord.core.domain.model.ingredient.PriceHistoryItem
import com.team.chord.core.domain.model.ingredient.RecentSearch
import com.team.chord.core.domain.model.ingredient.UsedMenu
import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.domain.model.menu.CheckDupResult
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.domain.model.menu.MarginGrade
import com.team.chord.core.domain.model.menu.Menu
import com.team.chord.core.domain.model.menu.MenuRecipe
import com.team.chord.core.domain.model.menu.MenuTemplate
import com.team.chord.core.domain.model.menu.NewRecipeInfo
import com.team.chord.core.domain.model.menu.TemplateIngredient
import com.team.chord.core.domain.repository.IngredientRepository
import com.team.chord.core.domain.repository.MenuRepository
import com.team.chord.core.domain.usecase.ingredient.GetIngredientDetailUseCase
import com.team.chord.core.domain.usecase.menu.GetMenuDetailUseCase
import com.team.chord.core.domain.usecase.menu.GetMenuRecipesUseCase
import com.team.chord.core.domain.usecase.menu.UpdateRecipeAmountUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IngredientEditViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `amount unchanged keeps submit disabled and changed amount enables submit`() = runTest {
        val menuRepository = FakeMenuRepository()
        val ingredientRepository = FakeIngredientRepository()
        val viewModel = createViewModel(menuRepository, ingredientRepository)

        advanceUntilIdle()

        val recipe = viewModel.uiState.value.recipes.first()
        viewModel.showEditBottomSheet(recipe)
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.sheetState?.isSubmitEnabled ?: true)

        viewModel.onAmountInputChange("100")
        assertTrue(viewModel.uiState.value.sheetState?.isSubmitEnabled ?: false)
    }

    @Test
    fun `submit success closes sheet updates list and emits success toast`() = runTest {
        val menuRepository = FakeMenuRepository(
            updateResult = Result.Success(Unit),
        )
        val ingredientRepository = FakeIngredientRepository()
        val viewModel = createViewModel(menuRepository, ingredientRepository)

        advanceUntilIdle()

        val recipe = viewModel.uiState.value.recipes.first()
        viewModel.showEditBottomSheet(recipe)
        advanceUntilIdle()
        viewModel.onAmountInputChange("100")

        viewModel.submitAmountUpdate()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.sheetState)
        assertEquals("재료의 사용량이 수정됐어요", viewModel.uiState.value.toastMessage)
        assertEquals(100.0, viewModel.uiState.value.recipes.first().amount, 0.0)
        assertEquals(100.0, menuRepository.lastUpdatedAmount ?: -1.0, 0.0)
    }

    @Test
    fun `submit failure keeps sheet open and exposes error message`() = runTest {
        val menuRepository = FakeMenuRepository(
            updateResult = Result.Error(IllegalStateException("network fail")),
        )
        val ingredientRepository = FakeIngredientRepository()
        val viewModel = createViewModel(menuRepository, ingredientRepository)

        advanceUntilIdle()

        val recipe = viewModel.uiState.value.recipes.first()
        viewModel.showEditBottomSheet(recipe)
        advanceUntilIdle()
        viewModel.onAmountInputChange("100")

        viewModel.submitAmountUpdate()
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.sheetState)
        assertEquals("network fail", viewModel.uiState.value.errorMessage)
        assertEquals(30.0, viewModel.uiState.value.recipes.first().amount, 0.0)
    }

    private fun createViewModel(
        menuRepository: MenuRepository,
        ingredientRepository: IngredientRepository,
    ): IngredientEditViewModel {
        return IngredientEditViewModel(
            savedStateHandle = SavedStateHandle(mapOf("menuId" to 1L)),
            getMenuDetailUseCase = GetMenuDetailUseCase(menuRepository),
            getMenuRecipesUseCase = GetMenuRecipesUseCase(menuRepository),
            updateRecipeAmountUseCase = UpdateRecipeAmountUseCase(menuRepository),
            getIngredientDetailUseCase = GetIngredientDetailUseCase(ingredientRepository),
        )
    }
}

private class FakeMenuRepository(
    private val updateResult: Result<Unit> = Result.Success(Unit),
) : MenuRepository {

    var lastUpdatedAmount: Double? = null

    override fun getMenuList(categoryCode: String?): Flow<List<Menu>> = flowOf(emptyList())

    override suspend fun getMenuDetail(menuId: Long): Menu? =
        Menu(
            id = menuId,
            name = "돌체라떼",
            price = 5000,
            categoryCode = "BEVERAGE",
            preparationTimeSeconds = 120,
            ingredients = emptyList(),
            totalCost = 1000,
            costRatio = 20f,
            marginRatio = 80f,
            contributionProfit = 4000,
            marginGrade = MarginGrade("A", "안정", ""),
            recommendedPrice = null,
            recommendedPriceMessage = null,
        )

    override suspend fun createMenu(
        categoryCode: String,
        menuName: String,
        sellingPrice: Int,
        workTime: Int,
        recipes: List<MenuRecipe>?,
        newRecipes: List<NewRecipeInfo>?,
    ): Result<Unit> = Result.Success(Unit)

    override suspend fun updateMenuName(menuId: Long, name: String): Result<Unit> = Result.Success(Unit)

    override suspend fun updateMenuPrice(menuId: Long, price: Int): Result<Unit> = Result.Success(Unit)

    override suspend fun updateMenuPreparationTime(menuId: Long, seconds: Int): Result<Unit> = Result.Success(Unit)

    override suspend fun updateMenuCategory(menuId: Long, categoryCode: String): Result<Unit> = Result.Success(Unit)

    override suspend fun deleteMenu(menuId: Long): Result<Unit> = Result.Success(Unit)

    override suspend fun getMenuRecipes(menuId: Long): List<MenuRecipe> = listOf(
        MenuRecipe(
            recipeId = 10L,
            menuId = menuId,
            ingredientId = 101L,
            ingredientName = "원두",
            amount = 30.0,
            unitCode = "G",
            price = 20000,
        ),
    )

    override suspend fun addExistingRecipe(menuId: Long, ingredientId: Long, amount: Int): Result<Unit> =
        Result.Success(Unit)

    override suspend fun addNewRecipe(
        menuId: Long,
        amount: Int,
        price: Int,
        unitCode: String,
        ingredientCategoryCode: String,
        ingredientName: String,
        supplier: String?,
    ): Result<Unit> = Result.Success(Unit)

    override suspend fun updateRecipeAmount(menuId: Long, recipeId: Long, amount: Double): Result<Unit> {
        lastUpdatedAmount = amount
        return updateResult
    }

    override suspend fun deleteRecipes(menuId: Long, recipeIds: List<Long>): Result<Unit> = Result.Success(Unit)

    override fun getCategories(): Flow<List<Category>> = flowOf(emptyList())

    override fun searchMenuTemplates(query: String): Flow<List<MenuTemplate>> = flowOf(emptyList())

    override suspend fun getTemplateBasic(templateId: Long): MenuTemplate? = null

    override suspend fun getTemplateIngredients(templateId: Long): List<TemplateIngredient> = emptyList()

    override suspend fun checkMenuDuplicate(menuName: String, ingredientNames: List<String>?): CheckDupResult =
        CheckDupResult(
            menuNameDuplicate = false,
            dupIngredientNames = emptyList(),
        )
}

private class FakeIngredientRepository : IngredientRepository {
    override fun getIngredientList(categoryCode: String?): Flow<List<Ingredient>> = flowOf(emptyList())

    override suspend fun getIngredientDetail(ingredientId: Long): Ingredient? =
        Ingredient(
            id = ingredientId,
            name = "원두",
            categoryCode = "INGREDIENTS",
            unit = IngredientUnit.G,
            baseQuantity = 100,
            currentUnitPrice = 5000,
            supplier = "쿠팡",
            isFavorite = false,
            originalAmount = null,
            originalPrice = null,
            usedMenus = listOf(UsedMenu(id = 1L, name = "돌체라떼", usageAmount = "30g")),
        )

    override suspend fun getPriceHistory(ingredientId: Long): List<PriceHistoryItem> = emptyList()

    override fun getCategories(): Flow<List<IngredientCategory>> = flowOf(emptyList())

    override suspend fun updateIngredient(
        ingredientId: Long,
        categoryCode: String,
        price: Int,
        amount: Int,
        unitCode: String,
    ): Result<Unit> = Result.Success(Unit)

    override suspend fun updateSupplier(ingredientId: Long, supplier: String): Result<Unit> = Result.Success(Unit)

    override suspend fun setFavorite(ingredientId: Long, favorite: Boolean): Result<Unit> = Result.Success(Unit)

    override suspend fun deleteIngredient(ingredientId: Long): Result<Unit> = Result.Success(Unit)

    override suspend fun createIngredient(
        categoryCode: String,
        ingredientName: String,
        unitCode: String,
        price: Int,
        amount: Int,
        supplier: String?,
    ): Result<Ingredient> = Result.Error(UnsupportedOperationException())

    override fun searchIngredients(query: String): Flow<List<IngredientSearchResult>> = flowOf(emptyList())

    override suspend fun checkDuplicate(name: String): Result<Unit> = Result.Success(Unit)

    override fun searchMyIngredients(query: String): Flow<List<Ingredient>> = flowOf(emptyList())

    override fun getRecentSearches(): Flow<List<RecentSearch>> = emptyFlow()

    override suspend fun addRecentSearch(query: String) = Unit

    override suspend fun deleteRecentSearch(id: Long) = Unit

    override suspend fun clearRecentSearches() = Unit
}
