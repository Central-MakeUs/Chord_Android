package com.team.chord.feature.ingredient.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.ingredient.IngredientFilter
import com.team.chord.core.domain.usecase.ingredient.AddIngredientToListUseCase
import com.team.chord.core.domain.usecase.ingredient.DeleteIngredientUseCase
import com.team.chord.core.domain.usecase.ingredient.GetIngredientListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientListViewModel @Inject constructor(
    private val getIngredientListUseCase: GetIngredientListUseCase,
    private val addIngredientToListUseCase: AddIngredientToListUseCase,
    private val deleteIngredientUseCase: DeleteIngredientUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(IngredientListUiState())
    val uiState: StateFlow<IngredientListUiState> = _uiState.asStateFlow()

    private var ingredientsByFilter: Map<IngredientFilter, List<IngredientListItemUi>> = emptyMap()
    private var loadJob: kotlinx.coroutines.Job? = null

    init {
        loadData()
    }

    fun refresh() {
        loadData(isRefresh = true)
    }

    fun onFilterToggle(filter: IngredientFilter) {
        val currentFilters = _uiState.value.activeFilters
        val newFilters = if (currentFilters.contains(filter)) {
            currentFilters - filter
        } else {
            currentFilters + filter
        }
        _uiState.update {
            it.copy(
                activeFilters = newFilters,
                ingredients = getIngredientsForFilters(newFilters),
            )
        }
    }

    fun onFilterRemove(filter: IngredientFilter) {
        val newFilters = _uiState.value.activeFilters - filter
        _uiState.update {
            it.copy(
                activeFilters = newFilters,
                ingredients = getIngredientsForFilters(newFilters),
            )
        }
    }

    // region More 메뉴

    fun onMoreClick() {
        _uiState.update { it.copy(showMoreMenu = true) }
    }

    fun onDismissMoreMenu() {
        _uiState.update { it.copy(showMoreMenu = false) }
    }

    // endregion

    // region 삭제 모드

    fun enterDeleteMode() {
        _uiState.update { it.copy(isDeleteMode = true, selectedIds = emptySet(), showMoreMenu = false) }
    }

    fun exitDeleteMode() {
        _uiState.update { it.copy(isDeleteMode = false, selectedIds = emptySet()) }
    }

    fun toggleSelection(id: Long) {
        _uiState.update { state ->
            val newIds = if (state.selectedIds.contains(id)) {
                state.selectedIds - id
            } else {
                state.selectedIds + id
            }
            state.copy(selectedIds = newIds)
        }
    }

    fun deleteSelected() {
        val selectedIds = _uiState.value.selectedIds
        if (selectedIds.isEmpty()) return
        viewModelScope.launch {
            val count = selectedIds.size
            var allSuccess = true
            selectedIds.forEach { id ->
                val result = deleteIngredientUseCase(id)
                if (result is Result.Error) {
                    allSuccess = false
                }
            }
            if (allSuccess) {
                _uiState.update {
                    it.copy(
                        isDeleteMode = false,
                        selectedIds = emptySet(),
                        showToast = true,
                        toastMessage = "${count}개 재료가 삭제되었어요",
                    )
                }
                loadData(isRefresh = true)
            }
        }
    }

    // endregion

    // region 추가 플로우

    fun onAddClick() {
        _uiState.update { it.copy(showAddNameSheet = true, showMoreMenu = false) }
    }

    fun onAddNameChange(name: String) {
        _uiState.update { it.copy(addIngredientName = name) }
    }

    fun onAddNameConfirm() {
        _uiState.update { it.copy(showAddNameSheet = false, showAddDetailSheet = true) }
    }

    fun onDismissAddNameSheet() {
        _uiState.update { it.copy(showAddNameSheet = false, addIngredientName = "") }
    }

    fun onDismissAddDetailSheet() {
        _uiState.update { it.copy(showAddDetailSheet = false) }
    }

    fun onAddIngredient(
        categoryCode: String,
        unitCode: String,
        price: Int,
        amount: Int,
        supplier: String?,
    ) {
        viewModelScope.launch {
            val result = addIngredientToListUseCase(
                categoryCode = categoryCode,
                ingredientName = _uiState.value.addIngredientName,
                unitCode = unitCode,
                price = price,
                amount = amount,
                supplier = supplier,
            )
            if (result is Result.Success) {
                _uiState.update {
                    it.copy(
                        showAddDetailSheet = false,
                        addIngredientName = "",
                        showToast = true,
                        toastMessage = "재료가 추가되었어요",
                    )
                }
                loadData(isRefresh = true)
            }
        }
    }

    // endregion

    // region Toast

    fun onToastDismissed() {
        _uiState.update { it.copy(showToast = false, toastMessage = "") }
    }

    // endregion

    private fun loadData(isRefresh: Boolean = false) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update {
                if (isRefresh) it.copy(isRefreshing = true)
                else it.copy(isLoading = true)
            }

            try {
                val results = coroutineScope {
                    IngredientFilter.entries.map { filter ->
                        async {
                            filter to getIngredientListUseCase(filter.categoryCode).first()
                        }
                    }.awaitAll().toMap()
                }

                ingredientsByFilter = results.mapValues { (_, ingredients) ->
                    ingredients.map { ingredient ->
                        IngredientListItemUi(
                            id = ingredient.id,
                            name = ingredient.name,
                            price = ingredient.currentUnitPrice,
                            usage = "사용량 ${ingredient.baseQuantity}${ingredient.unit.displayName}",
                        )
                    }
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        ingredients = getIngredientsForFilters(it.activeFilters),
                        errorMessage = null,
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            }
        }
    }

    private fun getIngredientsForFilters(filters: Set<IngredientFilter>): List<IngredientListItemUi> {
        val items = if (filters.isEmpty()) {
            val defaultFilters = setOf(IngredientFilter.FOOD_INGREDIENT, IngredientFilter.OPERATIONAL_SUPPLY)
            defaultFilters.flatMap { ingredientsByFilter[it].orEmpty() }
        } else {
            filters.flatMap { ingredientsByFilter[it].orEmpty() }
        }
        return items.distinctBy { it.id }
    }
}
