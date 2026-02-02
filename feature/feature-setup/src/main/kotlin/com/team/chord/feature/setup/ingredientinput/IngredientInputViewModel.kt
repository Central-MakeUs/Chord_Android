package com.team.chord.feature.setup.ingredientinput

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.ingredient.IngredientCategory
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.domain.usecase.ingredient.SearchIngredientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientInputViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchIngredientUseCase: SearchIngredientUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(IngredientInputUiState())
    val uiState: StateFlow<IngredientInputUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    private val menuName: String = savedStateHandle.get<String>("menuName") ?: ""
    private val isTemplateApplied: Boolean = savedStateHandle.get<Boolean>("isTemplateApplied") ?: false

    init {
        // If template is applied, we can load pre-filled ingredients
        if (isTemplateApplied) {
            loadTemplateIngredients()
        }
    }

    private fun loadTemplateIngredients() {
        // TODO: Load template ingredients based on menu name
        // For now, this is a placeholder for template-based ingredient loading
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }

        searchJob?.cancel()

        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            searchIngredients(query)
        }
    }

    private fun searchIngredients(query: String) {
        searchIngredientUseCase(query)
            .onStart {
                _uiState.update { it.copy(isSearching = true) }
            }
            .onEach { ingredients ->
                val suggestions = ingredients.map { ingredient ->
                    IngredientSuggestion(
                        id = ingredient.id,
                        name = ingredient.name,
                        hasTemplate = ingredient.price > 0 && ingredient.unitAmount > 0,
                        suggestedPrice = if (ingredient.price > 0) ingredient.price else null,
                        suggestedAmount = if (ingredient.unitAmount > 0) ingredient.unitAmount else null,
                        suggestedUnit = ingredient.unit,
                    )
                }
                _uiState.update {
                    it.copy(
                        searchResults = suggestions,
                        isSearching = false,
                    )
                }
            }
            .catch { _ ->
                _uiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
            }
            .launchIn(viewModelScope)
    }

    fun onSuggestionClicked(suggestion: IngredientSuggestion) {
        // Open bottom sheet with suggestion data
        val bottomSheetState = IngredientBottomSheetState(
            id = suggestion.id,
            name = suggestion.name,
            hasTemplate = suggestion.hasTemplate,
            suggestedPrice = suggestion.suggestedPrice,
            suggestedAmount = suggestion.suggestedAmount,
            suggestedUnit = suggestion.suggestedUnit,
            unit = suggestion.suggestedUnit ?: IngredientUnit.G,
            price = suggestion.suggestedPrice?.toString() ?: "",
            amount = suggestion.suggestedAmount?.toString() ?: "",
        )

        _uiState.update {
            it.copy(
                showAddBottomSheet = true,
                bottomSheetIngredient = bottomSheetState,
            )
        }
    }

    fun onAddNewIngredient() {
        // Open bottom sheet for new ingredient with current search query as name
        val bottomSheetState = IngredientBottomSheetState(
            id = null,
            name = _uiState.value.searchQuery,
            hasTemplate = false,
        )

        _uiState.update {
            it.copy(
                showAddBottomSheet = true,
                bottomSheetIngredient = bottomSheetState,
            )
        }
    }

    fun onBottomSheetDismissed() {
        _uiState.update {
            it.copy(
                showAddBottomSheet = false,
                bottomSheetIngredient = null,
            )
        }
    }

    fun onBottomSheetCategoryChanged(category: IngredientCategory) {
        _uiState.update { state ->
            state.copy(
                bottomSheetIngredient = state.bottomSheetIngredient?.copy(category = category),
            )
        }
    }

    fun onBottomSheetPriceChanged(price: String) {
        val filteredPrice = price.filter { it.isDigit() }
        _uiState.update { state ->
            state.copy(
                bottomSheetIngredient = state.bottomSheetIngredient?.copy(price = filteredPrice),
            )
        }
    }

    fun onBottomSheetAmountChanged(amount: String) {
        _uiState.update { state ->
            state.copy(
                bottomSheetIngredient = state.bottomSheetIngredient?.copy(amount = amount),
            )
        }
    }

    fun onBottomSheetUnitChanged(unit: IngredientUnit) {
        _uiState.update { state ->
            state.copy(
                bottomSheetIngredient = state.bottomSheetIngredient?.copy(unit = unit),
            )
        }
    }

    fun onBottomSheetSupplierChanged(supplier: String) {
        _uiState.update { state ->
            state.copy(
                bottomSheetIngredient = state.bottomSheetIngredient?.copy(supplier = supplier),
            )
        }
    }

    fun onAddIngredient() {
        val bottomSheetState = _uiState.value.bottomSheetIngredient ?: return

        if (!bottomSheetState.isAddEnabled) return

        val newIngredient = SelectedIngredient(
            id = bottomSheetState.id ?: System.currentTimeMillis(),
            name = bottomSheetState.name,
            amount = bottomSheetState.amount.toIntOrNull() ?: 0,
            unit = bottomSheetState.unit,
            price = bottomSheetState.price.toIntOrNull() ?: 0,
            category = bottomSheetState.category,
            supplier = bottomSheetState.supplier,
        )

        _uiState.update { state ->
            val updatedIngredients = state.selectedIngredients + newIngredient
            state.copy(
                selectedIngredients = updatedIngredients,
                showAddBottomSheet = false,
                bottomSheetIngredient = null,
                searchQuery = "",
                searchResults = emptyList(),
            )
        }
    }

    fun onRemoveIngredient(ingredientId: Long) {
        _uiState.update { state ->
            val updatedIngredients = state.selectedIngredients.filter { it.id != ingredientId }
            state.copy(
                selectedIngredients = updatedIngredients,
            )
        }
    }

    fun getSelectedIngredients(): List<SelectedIngredient> = _uiState.value.selectedIngredients

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 300L
    }
}
