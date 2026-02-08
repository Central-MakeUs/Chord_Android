package com.team.chord.feature.menu.ingredient

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.domain.model.menu.MenuIngredient
import com.team.chord.core.domain.usecase.menu.AddNewRecipeUseCase
import com.team.chord.core.domain.usecase.menu.DeleteRecipesUseCase
import com.team.chord.core.domain.usecase.menu.GetMenuDetailUseCase
import com.team.chord.core.domain.usecase.menu.GetMenuRecipesUseCase
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
    private val getMenuRecipesUseCase: GetMenuRecipesUseCase,
    private val addNewRecipeUseCase: AddNewRecipeUseCase,
    private val deleteRecipesUseCase: DeleteRecipesUseCase,
) : ViewModel() {

    private val menuId: Long = savedStateHandle.get<Long>("menuId") ?: 0L

    private val _uiState = MutableStateFlow(IngredientEditUiState())
    val uiState: StateFlow<IngredientEditUiState> = _uiState.asStateFlow()

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
                deleteRecipesUseCase(menuId, selectedIds)
                // Reload recipes after deletion
                loadRecipes()
                _uiState.update {
                    it.copy(
                        selectedIngredientIds = emptySet(),
                        isDeleteMode = false,
                    )
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
        // TODO: Implement recipe update via API when endpoint is available
    }

    fun addIngredient(
        name: String,
        unitPrice: Int,
        quantity: Double,
        unit: IngredientUnit,
    ) {
        viewModelScope.launch {
            addNewRecipeUseCase(
                menuId = menuId,
                amount = quantity.toInt(),
                price = unitPrice,
                unitCode = unit.name,
                ingredientCategoryCode = "FOOD_MATERIAL",
                ingredientName = name,
            )
            loadRecipes()
            _uiState.update { it.copy(showAddBottomSheet = false) }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
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
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "알 수 없는 오류가 발생했습니다")
                }
            }
        }
    }

    private suspend fun loadRecipes() {
        try {
            val recipes = getMenuRecipesUseCase(menuId)
            val menuIngredients = recipes.map { recipe ->
                MenuIngredient(
                    id = recipe.recipeId,
                    name = recipe.ingredientName,
                    quantity = recipe.amount.toDouble(),
                    unit = try {
                        IngredientUnit.valueOf(recipe.unitCode.uppercase())
                    } catch (_: Exception) {
                        IngredientUnit.G
                    },
                    unitPrice = recipe.price,
                    totalPrice = recipe.price * recipe.amount,
                )
            }
            _uiState.update { it.copy(ingredients = menuIngredients) }
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = e.message ?: "레시피를 불러올 수 없습니다") }
        }
    }
}
