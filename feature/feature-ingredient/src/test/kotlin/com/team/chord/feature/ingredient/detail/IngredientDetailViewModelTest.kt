package com.team.chord.feature.ingredient.detail

import androidx.lifecycle.SavedStateHandle
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.model.ingredient.IngredientCategory
import com.team.chord.core.domain.model.ingredient.IngredientSearchResult
import com.team.chord.core.domain.model.ingredient.PriceHistoryItem
import com.team.chord.core.domain.model.ingredient.RecentSearch
import com.team.chord.core.domain.model.ingredient.UsedMenu
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.domain.repository.IngredientRepository
import com.team.chord.core.domain.usecase.ingredient.DeleteIngredientUseCase
import com.team.chord.core.domain.usecase.ingredient.GetIngredientDetailUseCase
import com.team.chord.core.domain.usecase.ingredient.GetIngredientPriceHistoryUseCase
import com.team.chord.core.domain.usecase.ingredient.UpdateIngredientUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
class IngredientDetailViewModelTest {

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
    fun `onUpdatePriceInfo updates price fields without supplier mutation`() = runTest {
        val repository = FakeIngredientRepository()
        val viewModel = createViewModel(repository)

        advanceUntilIdle()
        viewModel.onUpdatePriceInfo(
            category = com.team.chord.core.domain.model.ingredient.IngredientFilter.FOOD_INGREDIENT,
            price = 7000,
            unitAmount = 120,
            unit = IngredientUnit.G,
        )
        advanceUntilIdle()

        val state = viewModel.uiState.value as IngredientDetailUiState.Success
        assertEquals(1, repository.updateIngredientCallCount)
        assertEquals(0, repository.updateSupplierCallCount)
        assertEquals(7000, state.ingredientDetail.price)
        assertEquals(120, state.ingredientDetail.unitAmount)
        assertEquals("쿠팡", state.ingredientDetail.supplier)
        assertEquals(2, state.ingredientDetail.priceHistory.size)
        assertEquals(7000, state.ingredientDetail.priceHistory.first().price)
        assertEquals(120, state.ingredientDetail.priceHistory.first().unitAmount)
        assertEquals("g", state.ingredientDetail.priceHistory.first().unitDisplayName)
        assertEquals("재료 정보가 수정됐어요", state.toastMessage)
        assertTrue(viewModel.hasChanges())
    }

    @Test
    fun `onUpdateSupplier unchanged does nothing`() = runTest {
        val repository = FakeIngredientRepository()
        val viewModel = createViewModel(repository)

        advanceUntilIdle()
        viewModel.onUpdateSupplier("쿠팡")
        advanceUntilIdle()

        val state = viewModel.uiState.value as IngredientDetailUiState.Success
        assertEquals(0, repository.updateSupplierCallCount)
        assertNull(state.toastMessage)
        assertFalse(viewModel.hasChanges())
    }

    @Test
    fun `onUpdateSupplier changed updates supplier and emits toast`() = runTest {
        val repository = FakeIngredientRepository()
        val viewModel = createViewModel(repository)

        advanceUntilIdle()
        viewModel.onUpdateSupplier("마켓컬리")
        advanceUntilIdle()

        val state = viewModel.uiState.value as IngredientDetailUiState.Success
        assertEquals(1, repository.updateSupplierCallCount)
        assertEquals("마켓컬리", state.ingredientDetail.supplier)
        assertEquals("공급업체가 수정됐어요", state.toastMessage)
        assertTrue(viewModel.hasChanges())
    }

    private fun createViewModel(
        repository: FakeIngredientRepository,
    ): IngredientDetailViewModel {
        return IngredientDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("ingredientId" to 1L)),
            getIngredientDetailUseCase = GetIngredientDetailUseCase(repository),
            getIngredientPriceHistoryUseCase = GetIngredientPriceHistoryUseCase(repository),
            updateIngredientUseCase = UpdateIngredientUseCase(repository),
            deleteIngredientUseCase = DeleteIngredientUseCase(repository),
        )
    }
}

private class FakeIngredientRepository : IngredientRepository {
    private var ingredient = Ingredient(
        id = 1L,
        name = "원두",
        categoryCode = "INGREDIENTS",
        unit = IngredientUnit.G,
        baseQuantity = 100,
        currentUnitPrice = 5000,
        supplier = "쿠팡",
        isFavorite = false,
        usedMenus = listOf(UsedMenu(id = 10L, name = "아메리카노", usageAmount = "30g")),
    )
    private val priceHistoryItems = mutableListOf(
        PriceHistoryItem(
            id = 1L,
            date = "2025-11-12",
            price = ingredient.currentUnitPrice,
            unitAmount = ingredient.baseQuantity,
            unit = ingredient.unit,
        ),
    )

    var updateIngredientCallCount = 0
    var updateSupplierCallCount = 0

    override fun getIngredientList(categoryCode: String?): Flow<List<Ingredient>> = flowOf(emptyList())

    override suspend fun getIngredientDetail(ingredientId: Long): Ingredient? = ingredient

    override suspend fun getPriceHistory(ingredientId: Long): List<PriceHistoryItem> = priceHistoryItems.toList()

    override fun getCategories(): Flow<List<IngredientCategory>> = flowOf(emptyList())

    override suspend fun updateIngredient(
        ingredientId: Long,
        categoryCode: String,
        price: Int,
        amount: Int,
        unitCode: String,
    ): Result<Unit> {
        updateIngredientCallCount += 1
        ingredient = ingredient.copy(
            categoryCode = categoryCode,
            currentUnitPrice = price,
            baseQuantity = amount,
            unit = IngredientUnit.valueOf(unitCode),
        )
        priceHistoryItems.add(
            0,
            PriceHistoryItem(
                id = (priceHistoryItems.maxOfOrNull { it.id } ?: 0L) + 1L,
                date = "2026-04-10",
                price = price,
                unitAmount = amount,
                unit = ingredient.unit,
            ),
        )
        return Result.Success(Unit)
    }

    override suspend fun updateSupplier(ingredientId: Long, supplier: String): Result<Unit> {
        updateSupplierCallCount += 1
        ingredient = ingredient.copy(supplier = supplier)
        return Result.Success(Unit)
    }

    override suspend fun setFavorite(ingredientId: Long, favorite: Boolean): Result<Unit> {
        ingredient = ingredient.copy(isFavorite = favorite)
        return Result.Success(Unit)
    }

    override suspend fun deleteIngredient(ingredientId: Long): Result<Unit> = Result.Success(Unit)

    override suspend fun createIngredient(
        categoryCode: String,
        ingredientName: String,
        unitCode: String,
        price: Int,
        amount: Int,
        supplier: String?,
    ): Result<Ingredient> = Result.Success(ingredient)

    override fun searchIngredients(query: String): Flow<List<IngredientSearchResult>> = flowOf(emptyList())

    override suspend fun checkDuplicate(name: String): Result<Unit> = Result.Success(Unit)

    override fun searchMyIngredients(query: String): Flow<List<Ingredient>> = flowOf(emptyList())

    override fun getRecentSearches(): Flow<List<RecentSearch>> = flowOf(emptyList())

    override suspend fun addRecentSearch(query: String) = Unit

    override suspend fun deleteRecentSearch(id: Long) = Unit

    override suspend fun clearRecentSearches() = Unit
}
