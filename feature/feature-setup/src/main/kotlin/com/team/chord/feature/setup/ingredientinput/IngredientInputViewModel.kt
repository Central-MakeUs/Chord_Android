package com.team.chord.feature.setup.ingredientinput

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.domain.usecase.ingredient.SearchIngredientUseCase
import com.team.chord.core.domain.usecase.menu.GetTemplateIngredientsUseCase
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
    private val getTemplateIngredientsUseCase: GetTemplateIngredientsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(IngredientInputUiState())
    val uiState: StateFlow<IngredientInputUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    private val menuName: String = savedStateHandle.get<String>("menuName") ?: ""
    private val isTemplateApplied: Boolean = savedStateHandle.get<Boolean>("isTemplateApplied") ?: false
    private val templateId: Long = savedStateHandle.get<Long>("templateId") ?: 0L

    init {
        _uiState.update { it.copy(isTemplateApplied = isTemplateApplied) }
        if (isTemplateApplied && templateId > 0L) {
            loadTemplateIngredients()
        }
    }

    private fun loadTemplateIngredients() {
        viewModelScope.launch {
            try {
                val recipes = getTemplateIngredientsUseCase(templateId)
                val selectedIngredients = recipes.map { recipe ->
                    SelectedIngredient(
                        id = recipe.ingredientId,
                        name = recipe.ingredientName,
                        amount = recipe.amount,
                        unit = recipe.unitCode.toIngredientUnit(),
                        price = recipe.price,
                        sourceType = IngredientSourceType.TEMPLATE,
                        templateRecipeId = recipe.recipeId,
                        baseQuantity = recipe.amount,
                        unitPrice = recipe.price,
                    )
                }
                _uiState.update { it.copy(selectedIngredients = selectedIngredients) }
            } catch (_: Exception) {
                // Failed to load template ingredients - continue with empty list
            }
        }
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
            .onEach { searchResults ->
                val suggestions = searchResults.map { result ->
                    IngredientSuggestion(
                        ingredientId = result.ingredientId,
                        templateId = result.templateId,
                        name = result.ingredientName,
                        sourceType = when {
                            result.ingredientId != null && !result.isTemplate -> IngredientSourceType.SAVED
                            result.isTemplate -> IngredientSourceType.TEMPLATE
                            else -> IngredientSourceType.NEW
                        },
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
        val bottomSheetState = when (suggestion.sourceType) {
            IngredientSourceType.SAVED -> IngredientBottomSheetState(
                id = suggestion.ingredientId,
                name = suggestion.name,
                sourceType = IngredientSourceType.SAVED,
                categoryCode = suggestion.categoryCode ?: "FOOD_MATERIAL",
                price = suggestion.unitPrice?.toString() ?: "",
                purchaseAmount = suggestion.baseQuantity?.toString() ?: "",
                unit = suggestion.unitCode?.toIngredientUnit() ?: IngredientUnit.G,
                supplier = suggestion.supplier ?: "-",
            )
            IngredientSourceType.TEMPLATE -> IngredientBottomSheetState(
                id = suggestion.ingredientId,
                name = suggestion.name,
                sourceType = IngredientSourceType.TEMPLATE,
                categoryCode = suggestion.categoryCode ?: "FOOD_MATERIAL",
                price = suggestion.unitPrice?.toString() ?: "",
                purchaseAmount = suggestion.baseQuantity?.toString() ?: "",
                unit = suggestion.unitCode?.toIngredientUnit() ?: IngredientUnit.G,
            )
            IngredientSourceType.NEW -> IngredientBottomSheetState(
                id = suggestion.ingredientId,
                name = suggestion.name,
                sourceType = IngredientSourceType.NEW,
            )
        }

        _uiState.update {
            it.copy(
                showBottomSheet = true,
                bottomSheetIngredient = bottomSheetState,
            )
        }
    }

    fun onAddNewIngredient() {
        val bottomSheetState = IngredientBottomSheetState(
            id = null,
            name = _uiState.value.searchQuery,
            sourceType = IngredientSourceType.NEW,
        )

        _uiState.update {
            it.copy(
                showBottomSheet = true,
                bottomSheetIngredient = bottomSheetState,
            )
        }
    }

    fun onEditIngredient(ingredient: SelectedIngredient) {
        val bottomSheetState = IngredientBottomSheetState(
            id = ingredient.id,
            name = ingredient.name,
            sourceType = ingredient.sourceType,
            categoryCode = ingredient.categoryCode,
            price = ingredient.price.toString(),
            purchaseAmount = ingredient.baseQuantity.toString(),
            amount = ingredient.amount.toString(),
            unit = ingredient.unit,
            supplier = ingredient.supplier,
            isEditMode = true,
            editingIngredientId = ingredient.id,
        )

        _uiState.update {
            it.copy(
                showBottomSheet = true,
                bottomSheetIngredient = bottomSheetState,
            )
        }
    }

    fun onBottomSheetDismissed() {
        _uiState.update {
            it.copy(
                showBottomSheet = false,
                bottomSheetIngredient = null,
            )
        }
    }

    fun onBottomSheetCategoryChanged(categoryCode: String) {
        _uiState.update { state ->
            state.copy(
                bottomSheetIngredient = state.bottomSheetIngredient?.copy(categoryCode = categoryCode),
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

    fun onBottomSheetPurchaseAmountChanged(purchaseAmount: String) {
        _uiState.update { state ->
            state.copy(
                bottomSheetIngredient = state.bottomSheetIngredient?.copy(purchaseAmount = purchaseAmount),
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

    fun onConfirmIngredient() {
        val bottomSheetState = _uiState.value.bottomSheetIngredient ?: return
        if (!bottomSheetState.isAddEnabled) return

        if (bottomSheetState.isEditMode) {
            // Update existing ingredient
            _uiState.update { state ->
                val updatedIngredients = state.selectedIngredients.map { ingredient ->
                    if (ingredient.id == bottomSheetState.editingIngredientId) {
                        ingredient.copy(
                            amount = bottomSheetState.amount.toIntOrNull() ?: ingredient.amount,
                            supplier = bottomSheetState.supplier,
                            categoryCode = bottomSheetState.categoryCode,
                            price = bottomSheetState.price.toIntOrNull() ?: ingredient.price,
                        )
                    } else {
                        ingredient
                    }
                }
                state.copy(
                    selectedIngredients = updatedIngredients,
                    showBottomSheet = false,
                    bottomSheetIngredient = null,
                    showCompletionToast = true,
                    completionToastMessage = "재료 수정이 완료되었어요!",
                )
            }
        } else {
            // Add new ingredient
            val newIngredient = SelectedIngredient(
                id = bottomSheetState.id ?: System.currentTimeMillis(),
                name = bottomSheetState.name,
                amount = bottomSheetState.amount.toIntOrNull() ?: 0,
                unit = bottomSheetState.unit,
                price = bottomSheetState.price.toIntOrNull() ?: 0,
                categoryCode = bottomSheetState.categoryCode,
                supplier = bottomSheetState.supplier,
                sourceType = bottomSheetState.sourceType,
                baseQuantity = bottomSheetState.purchaseAmount.toIntOrNull() ?: 0,
                unitPrice = bottomSheetState.price.toIntOrNull() ?: 0,
            )

            _uiState.update { state ->
                val updatedIngredients = state.selectedIngredients + newIngredient
                state.copy(
                    selectedIngredients = updatedIngredients,
                    showBottomSheet = false,
                    bottomSheetIngredient = null,
                    searchQuery = "",
                    searchResults = emptyList(),
                    showCompletionToast = true,
                    completionToastMessage = "재료 추가가 완료되었어요!",
                )
            }
        }
    }

    fun onToastDismissed() {
        _uiState.update { it.copy(showCompletionToast = false) }
    }

    fun onRemoveIngredient(ingredientId: Long) {
        _uiState.update { state ->
            val updatedIngredients = state.selectedIngredients.filter { it.id != ingredientId }
            state.copy(selectedIngredients = updatedIngredients)
        }
    }

    fun getSelectedIngredients(): List<SelectedIngredient> = _uiState.value.selectedIngredients

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 300L
    }
}

private fun String.toIngredientUnit(): IngredientUnit = when (this.uppercase()) {
    "ML" -> IngredientUnit.ML
    "G" -> IngredientUnit.G
    "KG" -> IngredientUnit.KG
    "EA" -> IngredientUnit.EA
    else -> IngredientUnit.G
}
