package com.team.chord.feature.menu.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.onSuccess
import com.team.chord.core.domain.usecase.menu.DeleteMenuUseCase
import com.team.chord.core.domain.usecase.menu.GetMenuDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMenuDetailUseCase: GetMenuDetailUseCase,
    private val deleteMenuUseCase: DeleteMenuUseCase,
) : ViewModel() {

    private val menuId: Long = savedStateHandle.get<Long>("menuId") ?: 0L

    private val _uiState = MutableStateFlow<MenuDetailUiState>(MenuDetailUiState.Loading)
    val uiState: StateFlow<MenuDetailUiState> = _uiState.asStateFlow()

    init {
        loadMenuDetail()
    }

    fun getMenuId(): Long = menuId

    fun refresh() {
        loadMenuDetail()
    }

    fun showDropdownMenu() {
        updateSuccessState { it.copy(showDropdownMenu = true) }
    }

    fun hideDropdownMenu() {
        updateSuccessState { it.copy(showDropdownMenu = false) }
    }

    fun showDeleteDialog() {
        updateSuccessState { it.copy(showDropdownMenu = false, showDeleteDialog = true) }
    }

    fun hideDeleteDialog() {
        updateSuccessState { it.copy(showDeleteDialog = false) }
    }

    fun hideDeleteSuccessDialog() {
        updateSuccessState { it.copy(showDeleteSuccessDialog = false) }
    }

    fun deleteMenu() {
        viewModelScope.launch {
            deleteMenuUseCase(menuId).onSuccess {
                updateSuccessState { it.copy(showDeleteDialog = false, showDeleteSuccessDialog = true) }
            }
        }
    }

    private fun updateSuccessState(transform: (MenuDetailUiState.Success) -> MenuDetailUiState.Success) {
        val current = _uiState.value
        if (current is MenuDetailUiState.Success) {
            _uiState.value = transform(current)
        }
    }

    private fun loadMenuDetail() {
        viewModelScope.launch {
            _uiState.value = MenuDetailUiState.Loading

            try {
                val menu = getMenuDetailUseCase(menuId)
                _uiState.value = if (menu != null) {
                    MenuDetailUiState.Success(
                        MenuDetailUi(
                            id = menu.id,
                            name = menu.name,
                            sellingPrice = menu.price,
                            preparationTimeSeconds = menu.preparationTimeSeconds,
                            totalCost = menu.totalCost,
                            costRatio = menu.costRatio,
                            marginRatio = menu.marginRatio,
                            contributionProfit = menu.contributionProfit,
                            marginGrade = menu.marginGrade,
                            recommendedPrice = menu.recommendedPrice,
                            recommendedPriceMessage = menu.recommendedPriceMessage,
                            ingredients = menu.ingredients.map { ingredient ->
                                IngredientUi(
                                    id = ingredient.id,
                                    name = ingredient.name,
                                    quantity = ingredient.quantity,
                                    unit = ingredient.unit,
                                    price = ingredient.totalPrice,
                                )
                            },
                        ),
                    )
                } else {
                    MenuDetailUiState.Error("메뉴를 찾을 수 없습니다")
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = MenuDetailUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다")
            }
        }
    }
}
