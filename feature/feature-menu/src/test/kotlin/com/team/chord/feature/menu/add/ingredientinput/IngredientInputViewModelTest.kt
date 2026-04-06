package com.team.chord.feature.menu.add.ingredientinput

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
import com.team.chord.core.domain.usecase.ingredient.CheckIngredientDuplicateUseCase
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
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
    fun `next button remains enabled when ingredient list is empty`() = runTest {
        val viewModel = createViewModel(
            savedStateHandle = SavedStateHandle(),
        )

        assertTrue(viewModel.uiState.value.isNextEnabled)
        assertTrue(viewModel.getSelectedIngredients().isEmpty())
    }

    @Test
    fun `cancel delete mode clears selected targets`() = runTest {
        val viewModel = createViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "isTemplateApplied" to true,
                    "templateId" to 1L,
                ),
            ),
        )

        advanceUntilIdle()
        val firstIngredientId = viewModel.uiState.value.selectedIngredients.first().id

        viewModel.enterDeleteMode()
        viewModel.toggleIngredientSelectionForDeletion(firstIngredientId)
        assertTrue(viewModel.uiState.value.isDeleteMode)
        assertTrue(firstIngredientId in viewModel.uiState.value.selectedIngredientIdsForDeletion)

        viewModel.cancelDeleteMode()

        assertFalse(viewModel.uiState.value.isDeleteMode)
        assertTrue(viewModel.uiState.value.selectedIngredientIdsForDeletion.isEmpty())
        assertFalse(viewModel.uiState.value.showDeleteConfirmDialog)
    }

    @Test
    fun `confirm delete removes selected ingredients and exits delete mode`() = runTest {
        val viewModel = createViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "isTemplateApplied" to true,
                    "templateId" to 1L,
                ),
            ),
        )

        advanceUntilIdle()
        val initialIngredients = viewModel.uiState.value.selectedIngredients
        val firstIngredientId = initialIngredients.first().id

        viewModel.enterDeleteMode()
        viewModel.toggleIngredientSelectionForDeletion(firstIngredientId)
        viewModel.showDeleteConfirmDialog()

        assertTrue(viewModel.uiState.value.showDeleteConfirmDialog)

        viewModel.confirmDeleteSelectedIngredients()

        assertEquals(initialIngredients.size - 1, viewModel.uiState.value.selectedIngredients.size)
        assertFalse(viewModel.uiState.value.isDeleteMode)
        assertTrue(viewModel.uiState.value.selectedIngredientIdsForDeletion.isEmpty())
        assertFalse(viewModel.uiState.value.showDeleteConfirmDialog)
        assertTrue(viewModel.uiState.value.showCompletionToast)
        assertEquals("선택한 재료를 삭제했어요.", viewModel.uiState.value.completionToastMessage)
    }

    @Test
    fun `template ingredients preserve metadata when loaded`() = runTest {
        val viewModel = createViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "isTemplateApplied" to true,
                    "templateId" to 1L,
                ),
            ),
        )

        advanceUntilIdle()

        val selectedIngredients = viewModel.uiState.value.selectedIngredients
        val first = selectedIngredients.first()
        val second = selectedIngredients.last()

        assertTrue(first.id < 0L)
        assertNull(first.serverIngredientId)
        assertEquals(30, first.amount)
        assertEquals(800, first.price)
        assertEquals(16000, first.unitPrice)
        assertEquals(1000, first.baseQuantity)
        assertEquals("INGREDIENTS", first.categoryCode)

        assertTrue(second.id < 0L)
        assertEquals(1002L, second.serverIngredientId)
        assertEquals(250, second.amount)
        assertEquals(120, second.price)
        assertEquals(0, second.unitPrice)
        assertEquals(1000, second.baseQuantity)
        assertEquals("INGREDIENTS", second.categoryCode)
    }

    @Test
    fun `new ingredient stays local draft and ignores empty supplier when required fields are filled`() = runTest {
        val viewModel = createViewModel(
            savedStateHandle = SavedStateHandle(),
        )

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

        viewModel.onConfirmIngredient()
        advanceUntilIdle()

        val added = viewModel.uiState.value.selectedIngredients.single()
        assertEquals(IngredientSourceType.NEW, added.sourceType)
        assertNull(added.serverIngredientId)
        assertEquals(1200, added.price)
        assertEquals(100, added.baseQuantity)
        assertEquals(10, added.amount)
        assertEquals(IngredientUnit.G, added.unit)
    }

    @Test
    fun `editing local new ingredient updates editable fields`() = runTest {
        val viewModel = createViewModel(
            savedStateHandle = SavedStateHandle(),
        )

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
        viewModel.onBottomSheetUnitChanged(IngredientUnit.EA)
        viewModel.onBottomSheetPriceChanged("7500")
        viewModel.onBottomSheetPurchaseAmountChanged("40")
        viewModel.onBottomSheetAmountChanged("3")
        viewModel.onBottomSheetSupplierChanged("쿠팡")
        viewModel.onConfirmIngredient()

        val updated = viewModel.uiState.value.selectedIngredients.single()
        assertEquals(IngredientSourceType.NEW, updated.sourceType)
        assertNull(updated.serverIngredientId)
        assertEquals("MATERIALS", updated.categoryCode)
        assertEquals(IngredientUnit.EA, updated.unit)
        assertEquals(7500, updated.price)
        assertEquals(7500, updated.unitPrice)
        assertEquals(40, updated.baseQuantity)
        assertEquals(3, updated.amount)
        assertEquals("쿠팡", updated.supplier)
    }

    @Test
    fun `existing ingredient edit modal locks non-usage fields`() = runTest {
        val viewModel = createViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "isTemplateApplied" to true,
                    "templateId" to 1L,
                ),
            ),
        )

        advanceUntilIdle()

        viewModel.onEditIngredient(viewModel.uiState.value.selectedIngredients.first())

        val bottomSheetState = viewModel.uiState.value.bottomSheetIngredient
        requireNotNull(bottomSheetState)

        assertTrue(bottomSheetState.isExistingIngredientLayout)
        assertFalse(bottomSheetState.isCategoryEditable)
        assertFalse(bottomSheetState.isPriceEditable)
        assertFalse(bottomSheetState.isPurchaseAmountEditable)
        assertFalse(bottomSheetState.isUnitEditable)
        assertFalse(bottomSheetState.isSupplierEditable)
        assertEquals("사용량", bottomSheetState.usageLabel)
        assertEquals("1000g당 16,000원", bottomSheetState.unitPriceText)
        assertEquals("-", bottomSheetState.supplierText)
    }

    @Test
    fun `editing existing ingredient only updates usage amount`() = runTest {
        val viewModel = createViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "isTemplateApplied" to true,
                    "templateId" to 1L,
                ),
            ),
        )

        advanceUntilIdle()

        val original = viewModel.uiState.value.selectedIngredients.first()

        viewModel.onEditIngredient(original)
        viewModel.onBottomSheetCategoryChanged("MATERIALS")
        viewModel.onBottomSheetPriceChanged("9999")
        viewModel.onBottomSheetPurchaseAmountChanged("500")
        viewModel.onBottomSheetUnitChanged(IngredientUnit.EA)
        viewModel.onBottomSheetSupplierChanged("새 공급처")
        viewModel.onBottomSheetAmountChanged("45")
        viewModel.onConfirmIngredient()

        val updated = viewModel.uiState.value.selectedIngredients.first()
        assertEquals(45, updated.amount)
        assertEquals(original.categoryCode, updated.categoryCode)
        assertEquals(original.price, updated.price)
        assertEquals(original.baseQuantity, updated.baseQuantity)
        assertEquals(original.unit, updated.unit)
        assertEquals(original.supplier, updated.supplier)
        assertEquals(original.unitPrice, updated.unitPrice)
    }

    private fun createViewModel(
        savedStateHandle: SavedStateHandle,
    ): IngredientInputViewModel {
        val ingredientRepository = FakeIngredientRepository()
        val menuRepository = FakeMenuRepository()

        return IngredientInputViewModel(
            savedStateHandle = savedStateHandle,
            searchIngredientUseCase = SearchIngredientUseCase(ingredientRepository),
            getTemplateIngredientsUseCase = GetTemplateIngredientsUseCase(menuRepository),
            checkIngredientDuplicateUseCase = CheckIngredientDuplicateUseCase(ingredientRepository),
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

    override suspend fun getTemplateIngredients(templateId: Long): List<TemplateIngredient> {
        if (templateId != 1L) return emptyList()
        return listOf(
            TemplateIngredient(
                ingredientId = null,
                ingredientName = "원두",
                usageAmount = 30.0,
                defaultCost = 800,
                unitPrice = 16000,
                baseQuantity = 1000,
                unitCode = "G",
                ingredientCategoryCode = "INGREDIENTS",
            ),
            TemplateIngredient(
                ingredientId = 1002L,
                ingredientName = "정수물",
                usageAmount = 250.0,
                defaultCost = 120,
                unitPrice = 0,
                baseQuantity = 1000,
                unitCode = "ML",
                ingredientCategoryCode = "INGREDIENTS",
            ),
        )
    }

    override suspend fun checkMenuDuplicate(menuName: String, ingredientNames: List<String>?): CheckDupResult =
        CheckDupResult(
            menuNameDuplicate = false,
            dupIngredientNames = emptyList(),
        )
}

private class FakeIngredientRepository : IngredientRepository {
    private var createdId = 5000L

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
    ): Result<Ingredient> {
        createdId += 1
        return Result.Success(
            Ingredient(
                id = createdId,
                name = ingredientName,
                categoryCode = categoryCode,
                unit = IngredientUnit.entries.find { it.name == unitCode } ?: IngredientUnit.G,
                baseQuantity = amount,
                currentUnitPrice = price,
                supplier = supplier,
                isFavorite = false,
                originalAmount = null,
                originalPrice = null,
                usedMenus = emptyList(),
            ),
        )
    }

    override fun searchIngredients(query: String): Flow<List<IngredientSearchResult>> = flowOf(emptyList())

    override suspend fun checkDuplicate(name: String): Result<Unit> = Result.Success(Unit)

    override fun searchMyIngredients(query: String): Flow<List<Ingredient>> = emptyFlow()

    override fun getRecentSearches(): Flow<List<RecentSearch>> = emptyFlow()

    override suspend fun addRecentSearch(query: String) = Unit

    override suspend fun deleteRecentSearch(id: Long) = Unit

    override suspend fun clearRecentSearches() = Unit
}
