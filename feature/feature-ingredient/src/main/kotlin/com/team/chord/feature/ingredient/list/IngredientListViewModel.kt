package com.team.chord.feature.ingredient.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.ingredient.IngredientFilter
import com.team.chord.core.domain.usecase.ingredient.GetIngredientListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientListViewModel @Inject constructor(
    private val getIngredientListUseCase: GetIngredientListUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(IngredientListUiState())
    val uiState: StateFlow<IngredientListUiState> = _uiState.asStateFlow()

    private val activeFilters = MutableStateFlow<Set<IngredientFilter>>(emptySet())

    init {
        loadData()
    }

    fun onFilterToggle(filter: IngredientFilter) {
        activeFilters.value = if (activeFilters.value.contains(filter)) {
            activeFilters.value - filter
        } else {
            activeFilters.value + filter
        }
    }

    fun onFilterRemove(filter: IngredientFilter) {
        activeFilters.value = activeFilters.value - filter
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                combine(
                    getIngredientListUseCase(),
                    activeFilters,
                ) { ingredients, filters ->
                    val filteredIngredients = if (filters.isEmpty()) {
                        ingredients
                    } else {
                        ingredients.filter { ingredient ->
                            filters.all { filter ->
                                when (filter) {
                                    IngredientFilter.FAVORITE -> ingredient.isFavorite
                                    IngredientFilter.FOOD_INGREDIENT -> ingredient.categoryCode == "INGREDIENTS"
                                    IngredientFilter.OPERATIONAL_SUPPLY -> ingredient.categoryCode == "MATERIALS"
                                }
                            }
                        }
                    }

                    IngredientListUiState(
                        isLoading = false,
                        ingredients = filteredIngredients.map { ingredient ->
                            IngredientListItemUi(
                                id = ingredient.id,
                                name = ingredient.name,
                                price = ingredient.currentUnitPrice,
                                usage = "사용량 ${ingredient.baseQuantity}${ingredient.unit.displayName}",
                            )
                        },
                        activeFilters = filters,
                    )
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = IngredientListUiState(isLoading = false, errorMessage = e.message)
            }
        }
    }
}
