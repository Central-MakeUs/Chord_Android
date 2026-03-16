package com.team.chord.feature.ingredient.list

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.model.ingredient.IngredientCategory
import com.team.chord.core.domain.model.ingredient.IngredientSearchResult
import com.team.chord.core.domain.model.ingredient.PriceHistoryItem
import com.team.chord.core.domain.model.ingredient.RecentSearch
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.domain.repository.IngredientRepository
import com.team.chord.core.domain.usecase.ingredient.AddIngredientToListUseCase
import com.team.chord.core.domain.usecase.ingredient.CheckIngredientDuplicateUseCase
import com.team.chord.core.domain.usecase.ingredient.DeleteIngredientUseCase
import com.team.chord.core.domain.usecase.ingredient.GetIngredientListUseCase
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IngredientListViewModelTest {

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
    fun `duplicate ingredient name shows tooltip state`() = runTest {
        val repository = FakeIngredientRepository(duplicateNames = setOf("흑임자 토핑"))
        val viewModel = createViewModel(repository)

        viewModel.onAddClick()
        viewModel.onAddNameChange("흑임자 토핑")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isAddIngredientNameDuplicate)
    }

    @Test
    fun `dismissing add name sheet clears duplicate tooltip state`() = runTest {
        val repository = FakeIngredientRepository(duplicateNames = setOf("흑임자 토핑"))
        val viewModel = createViewModel(repository)

        viewModel.onAddClick()
        viewModel.onAddNameChange("흑임자 토핑")
        advanceUntilIdle()

        viewModel.onDismissAddNameSheet()

        assertFalse(viewModel.uiState.value.isAddIngredientNameDuplicate)
        assertTrue(viewModel.uiState.value.addIngredientName.isEmpty())
    }

    private fun createViewModel(repository: FakeIngredientRepository): IngredientListViewModel =
        IngredientListViewModel(
            getIngredientListUseCase = GetIngredientListUseCase(repository),
            addIngredientToListUseCase = AddIngredientToListUseCase(repository),
            checkIngredientDuplicateUseCase = CheckIngredientDuplicateUseCase(repository),
            deleteIngredientUseCase = DeleteIngredientUseCase(repository),
        )
}

private class FakeIngredientRepository(
    private val duplicateNames: Set<String> = emptySet(),
) : IngredientRepository {

    override fun getIngredientList(categoryCode: String?): Flow<List<Ingredient>> = flowOf(emptyList())

    override suspend fun getIngredientDetail(ingredientId: Long): Ingredient? = null

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
    ): Result<Ingredient> = Result.Success(
        Ingredient(
            id = 1L,
            name = ingredientName,
            categoryCode = categoryCode,
            unit = IngredientUnit.entries.find { it.name == unitCode } ?: IngredientUnit.G,
            baseQuantity = amount,
            currentUnitPrice = price,
            supplier = supplier,
        ),
    )

    override fun searchIngredients(query: String): Flow<List<IngredientSearchResult>> = flowOf(emptyList())

    override suspend fun checkDuplicate(name: String): Result<Unit> =
        if (duplicateNames.contains(name)) Result.Error(IllegalStateException("duplicate"))
        else Result.Success(Unit)

    override fun searchMyIngredients(query: String): Flow<List<Ingredient>> = flowOf(emptyList())

    override fun getRecentSearches(): Flow<List<RecentSearch>> = flowOf(emptyList())

    override suspend fun addRecentSearch(query: String) = Unit

    override suspend fun deleteRecentSearch(id: Long) = Unit

    override suspend fun clearRecentSearches() = Unit
}
