package com.team.chord.feature.menu.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.usecase.menu.GetMenuDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMenuDetailUseCase: GetMenuDetailUseCase,
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

    private fun loadMenuDetail() {
        viewModelScope.launch {
            _uiState.value = MenuDetailUiState.Loading

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
        }
    }
}
