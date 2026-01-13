package com.team.chord.feature.menu.ingredient

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.domain.model.menu.MenuIngredient
import com.team.chord.core.domain.model.onSuccess
import com.team.chord.core.domain.usecase.menu.AddIngredientUseCase
import com.team.chord.core.domain.usecase.menu.GetMenuDetailUseCase
import com.team.chord.core.domain.usecase.menu.RemoveIngredientsUseCase
import com.team.chord.core.domain.usecase.menu.UpdateIngredientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMenuDetailUseCase: GetMenuDetailUseCase,
    private val addIngredientUseCase: AddIngredientUseCase,
    private val updateIngredientUseCase: UpdateIngredientUseCase,
    private val removeIngredientsUseCase: RemoveIngredientsUseCase,
) : ViewModel() {

    private val menuId: Long = savedStateHandle.get<Long>("menuId") ?: 0L

    private val _uiState = MutableStateFlow(IngredientEditUiState())
    val uiState: StateFlow<IngredientEditUiState> = _uiState.asStateFlow()

    private var nextIngredientId = 100L

    init {
        loadData()
    }

    fun toggleDeleteMode() {
        _uiState.update {
            it.copy(
                isDeleteMode = !it.isDeleteMode,
                selectedIngredientIds = emptySet(),
            )
        }
    }

    fun toggleIngredientSelection(ingredientId: Long) {
        _uiState.update { state ->
            val newSelection = if (ingredientId in state.selectedIngredientIds) {
                state.selectedIngredientIds - ingredientId
            } else {
                state.selectedIngredientIds + ingredientId
            }
            state.copy(selectedIngredientIds = newSelection)
        }
    }

    fun deleteSelectedIngredients() {
        viewModelScope.launch {
            val selectedIds = _uiState.value.selectedIngredientIds.toList()
            if (selectedIds.isNotEmpty()) {
                removeIngredientsUseCase(menuId, selectedIds).onSuccess { menu ->
                    _uiState.update {
                        it.copy(
                            ingredients = menu.ingredients,
                            selectedIngredientIds = emptySet(),
                            isDeleteMode = false,
                        )
                    }
                }
            }
        }
    }

    fun showEditBottomSheet(ingredient: MenuIngredient) {
        _uiState.update {
            it.copy(
                showEditBottomSheet = true,
                editingIngredient = ingredient,
            )
        }
    }

    fun hideEditBottomSheet() {
        _uiState.update {
            it.copy(
                showEditBottomSheet = false,
                editingIngredient = null,
            )
        }
    }

    fun showAddBottomSheet() {
        _uiState.update { it.copy(showAddBottomSheet = true) }
    }

    fun hideAddBottomSheet() {
        _uiState.update { it.copy(showAddBottomSheet = false) }
    }

    fun updateIngredient(
        ingredientId: Long,
        name: String,
        unitPrice: Int,
        quantity: Double,
        unit: IngredientUnit,
    ) {
        viewModelScope.launch {
            val totalPrice = (unitPrice * quantity).toInt()
            val updatedIngredient = MenuIngredient(
                id = ingredientId,
                name = name,
                quantity = quantity,
                unit = unit,
                unitPrice = unitPrice,
                totalPrice = totalPrice,
            )

            updateIngredientUseCase(menuId, updatedIngredient).onSuccess { menu ->
                _uiState.update {
                    it.copy(
                        ingredients = menu.ingredients,
                        showEditBottomSheet = false,
                        editingIngredient = null,
                    )
                }
            }
        }
    }

    fun addIngredient(
        name: String,
        unitPrice: Int,
        quantity: Double,
        unit: IngredientUnit,
    ) {
        viewModelScope.launch {
            val totalPrice = (unitPrice * quantity).toInt()
            val newIngredient = MenuIngredient(
                id = nextIngredientId++,
                name = name,
                quantity = quantity,
                unit = unit,
                unitPrice = unitPrice,
                totalPrice = totalPrice,
            )

            addIngredientUseCase(menuId, newIngredient).onSuccess { menu ->
                _uiState.update {
                    it.copy(
                        ingredients = menu.ingredients,
                        showAddBottomSheet = false,
                    )
                }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val menu = getMenuDetailUseCase(menuId)
            if (menu != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        menuId = menu.id,
                        menuName = menu.name,
                        ingredients = menu.ingredients,
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "메뉴를 찾을 수 없습니다",
                    )
                }
            }
        }
    }
}
