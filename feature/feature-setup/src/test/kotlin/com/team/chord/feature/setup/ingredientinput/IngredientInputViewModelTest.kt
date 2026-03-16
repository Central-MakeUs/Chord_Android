package com.team.chord.feature.setup.ingredientinput

import androidx.lifecycle.SavedStateHandle
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.model.ingredient.IngredientCategory
import com.team.chord.core.domain.model.ingredient.IngredientSearchResult
import com.team.chord.core.domain.model.ingredient.PriceHistoryItem
import com.team.chord.core.domain.model.ingredient.RecentSearch
import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.domain.model.menu.CheckDupResult
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.domain.model.menu.Menu
import com.team.chord.core.domain.model.menu.MenuRecipe
import com.team.chord.core.domain.model.menu.MenuTemplate
import com.team.chord.core.domain.model.menu.NewRecipeInfo
import com.team.chord.core.domain.model.menu.TemplateIngredient
import com.team.chord.core.domain.repository.IngredientRepository
import com.team.chord.core.domain.repository.MenuRepository
import com.team.chord.core.domain.usecase.ingredient.SearchIngredientUseCase
import com.team.chord.core.domain.usecase.menu.GetTemplateIngredientsUseCase
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IngredientInputViewModelTest {

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
    fun `new ingredient form sanitizes numeric inputs and allows empty supplier`() = runTest {
        val viewModel = createViewModel()

        viewModel.onSearchQueryChanged("새 시럽")
        viewModel.onAddNewIngredient()
        viewModel.onBottomSheetPriceChanged("1,200원")
        viewModel.onBottomSheetPurchaseAmountChanged("100ml")
        viewModel.onBottomSheetAmountChanged("10ml")

        val bottomSheetState = viewModel.uiState.value.bottomSheetIngredient
        requireNotNull(bottomSheetState)
        assertEquals("1200", bottomSheetState.price)
        assertEquals("100", bottomSheetState.purchaseAmount)
        assertEquals("10", bottomSheetState.amount)
        assertTrue(bottomSheetState.supplier.isEmpty())
        assertTrue(bottomSheetState.isAddEnabled)
    }

    @Test
    fun `editing local ingredient updates draft fields`() = runTest {
        val viewModel = createViewModel()

        viewModel.onSearchQueryChanged("새 토핑")
        viewModel.onAddNewIngredient()
        viewModel.onBottomSheetPriceChanged("5000")
        viewModel.onBottomSheetPurchaseAmountChanged("100")
        viewModel.onBottomSheetAmountChanged("20")
        viewModel.onConfirmIngredient()
        advanceUntilIdle()

        val added = viewModel.uiState.value.selectedIngredients.single()

        viewModel.onEditIngredient(added)
        viewModel.onBottomSheetCategoryChanged("MATERIALS")
        viewModel.onBottomSheetUnitChanged(IngredientUnit.ML)
        viewModel.onBottomSheetPriceChanged("7500")
        viewModel.onBottomSheetPurchaseAmountChanged("40")
        viewModel.onBottomSheetAmountChanged("3")
        viewModel.onBottomSheetSupplierChanged("쿠팡")
        viewModel.onConfirmIngredient()

        val updated = viewModel.uiState.value.selectedIngredients.single()
        assertEquals("MATERIALS", updated.categoryCode)
        assertEquals(IngredientUnit.ML, updated.unit)
        assertEquals(7500, updated.price)
        assertEquals(7500, updated.unitPrice)
        assertEquals(40, updated.baseQuantity)
        assertEquals(3, updated.amount)
        assertEquals("쿠팡", updated.supplier)
    }

    private fun createViewModel(): IngredientInputViewModel {
        val ingredientRepository = FakeIngredientRepository()
        val menuRepository = FakeMenuRepository()

        return IngredientInputViewModel(
            savedStateHandle = SavedStateHandle(),
            searchIngredientUseCase = SearchIngredientUseCase(ingredientRepository),
            getTemplateIngredientsUseCase = GetTemplateIngredientsUseCase(menuRepository),
        )
    }
}

private class FakeMenuRepository : MenuRepository {
    override fun getMenuList(categoryCode: String?): Flow<List<Menu>> = emptyFlow()

    override suspend fun getMenuDetail(menuId: Long): Menu? = null

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

    override suspend fun getMenuRecipes(menuId: Long): List<MenuRecipe> = emptyList()

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

    override suspend fun updateRecipeAmount(menuId: Long, recipeId: Long, amount: Double): Result<Unit> =
        Result.Success(Unit)

    override suspend fun deleteRecipes(menuId: Long, recipeIds: List<Long>): Result<Unit> = Result.Success(Unit)

    override fun getCategories(): Flow<List<Category>> = emptyFlow()

    override fun searchMenuTemplates(query: String): Flow<List<MenuTemplate>> = emptyFlow()

    override suspend fun getTemplateBasic(templateId: Long): MenuTemplate? = null

    override suspend fun getTemplateIngredients(templateId: Long): List<TemplateIngredient> = emptyList()

    override suspend fun checkMenuDuplicate(menuName: String, ingredientNames: List<String>?): CheckDupResult =
        CheckDupResult(
            menuNameDuplicate = false,
            dupIngredientNames = emptyList(),
        )
}

private class FakeIngredientRepository : IngredientRepository {
    override fun getIngredientList(categoryCode: String?): Flow<List<Ingredient>> = emptyFlow()

    override suspend fun getIngredientDetail(ingredientId: Long): Ingredient? = null

    override suspend fun getPriceHistory(ingredientId: Long): List<PriceHistoryItem> = emptyList()

    override fun getCategories(): Flow<List<IngredientCategory>> = emptyFlow()

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
    ): Result<Ingredient> = Result.Error(IllegalStateException("createIngredient should not be called in setup test"))

    override fun searchIngredients(query: String): Flow<List<IngredientSearchResult>> = flowOf(emptyList())

    override suspend fun checkDuplicate(name: String): Result<Unit> = Result.Success(Unit)

    override fun searchMyIngredients(query: String): Flow<List<Ingredient>> = emptyFlow()

    override fun getRecentSearches(): Flow<List<RecentSearch>> = emptyFlow()

    override suspend fun addRecentSearch(query: String) = Unit

    override suspend fun deleteRecentSearch(id: Long) = Unit

    override suspend fun clearRecentSearches() = Unit
}
