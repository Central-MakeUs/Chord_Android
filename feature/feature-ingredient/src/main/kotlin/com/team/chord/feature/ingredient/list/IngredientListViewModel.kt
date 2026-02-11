package com.team.chord.feature.ingredient.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.ingredient.IngredientFilter
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

                val currentFilters = _uiState.value.activeFilters
                _uiState.value = IngredientListUiState(
                    activeFilters = currentFilters,
                    ingredients = getIngredientsForFilters(currentFilters),
                )
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
