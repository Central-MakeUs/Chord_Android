package com.team.chord.feature.ingredient.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.ingredient.PriceHistoryItem
import com.team.chord.core.domain.model.ingredient.UsedMenu
import com.team.chord.core.domain.usecase.ingredient.DeleteIngredientUseCase
import com.team.chord.core.domain.usecase.ingredient.GetIngredientDetailUseCase
import com.team.chord.core.domain.usecase.ingredient.UpdateIngredientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getIngredientDetailUseCase: GetIngredientDetailUseCase,
    private val updateIngredientUseCase: UpdateIngredientUseCase,
    private val deleteIngredientUseCase: DeleteIngredientUseCase,
) : ViewModel() {

    private val ingredientId: Long = savedStateHandle.get<Long>("ingredientId") ?: 0L

    private val _uiState = MutableStateFlow<IngredientDetailUiState>(IngredientDetailUiState.Loading)
    val uiState: StateFlow<IngredientDetailUiState> = _uiState.asStateFlow()

    init {
        loadIngredientDetail()
    }

    fun onFavoriteToggle() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState !is IngredientDetailUiState.Success) return@launch

            when (val result = updateIngredientUseCase.toggleFavorite(ingredientId)) {
                is Result.Success -> {
                    _uiState.value = currentState.copy(
                        ingredientDetail = currentState.ingredientDetail.copy(
                            isFavorite = result.data.isFavorite,
                        ),
                    )
                }
                is Result.Error -> {
                    // Keep current state, optionally show error
                }
                is Result.Loading -> {
                    // No-op: Loading state is handled elsewhere
                }
            }
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState !is IngredientDetailUiState.Success) return@launch

            when (deleteIngredientUseCase(ingredientId)) {
                is Result.Success -> {
                    _uiState.value = currentState.copy(
                        ingredientDetail = currentState.ingredientDetail.copy(
                            isDeleted = true,
                        ),
                    )
                }
                is Result.Error -> {
                    // Keep current state, optionally show error
                }
                is Result.Loading -> {
                    // No-op: Loading state is handled elsewhere
                }
            }
        }
    }

    private fun loadIngredientDetail() {
        viewModelScope.launch {
            _uiState.value = IngredientDetailUiState.Loading

            val ingredient = getIngredientDetailUseCase(ingredientId)
            if (ingredient != null) {
                // TODO: Fetch used menus and price history from respective use cases when available
                val usedMenus = getFakeUsedMenus()
                val priceHistory = getFakePriceHistory()

                _uiState.value = IngredientDetailUiState.Success(
                    IngredientDetailUi(
                        id = ingredient.id,
                        name = ingredient.name,
                        price = ingredient.price,
                        unitAmount = ingredient.unitAmount,
                        unit = ingredient.unit,
                        supplier = ingredient.supplier,
                        isFavorite = ingredient.isFavorite,
                        usedMenus = usedMenus.map { usedMenu ->
                            UsedMenuUi(
                                id = usedMenu.id,
                                name = usedMenu.name,
                            )
                        },
                        priceHistory = priceHistory.map { historyItem ->
                            PriceHistoryUi(
                                id = historyItem.id,
                                date = historyItem.date,
                                price = historyItem.price,
                                unitAmount = historyItem.unitAmount,
                                unitDisplayName = historyItem.unitType.displayName,
                            )
                        },
                    ),
                )
            } else {
                _uiState.value = IngredientDetailUiState.Error("재료를 찾을 수 없습니다")
            }
        }
    }

    // TODO: Replace with actual use case calls when domain layer is implemented
    private fun getFakeUsedMenus(): List<UsedMenu> = listOf(
        UsedMenu(id = 1L, name = "아메리카노"),
        UsedMenu(id = 2L, name = "카페라떼"),
        UsedMenu(id = 3L, name = "돌체라떼"),
        UsedMenu(id = 4L, name = "아인슈페너"),
    )

    private fun getFakePriceHistory(): List<PriceHistoryItem> = listOf(
        PriceHistoryItem(
            id = 1L,
            date = "25.11.12",
            price = 5000,
            unitAmount = 100,
            unitType = com.team.chord.core.domain.model.ingredient.IngredientUnitType.GRAM,
        ),
        PriceHistoryItem(
            id = 2L,
            date = "25.11.09",
            price = 5000,
            unitAmount = 100,
            unitType = com.team.chord.core.domain.model.ingredient.IngredientUnitType.GRAM,
        ),
        PriceHistoryItem(
            id = 3L,
            date = "25.10.11",
            price = 5000,
            unitAmount = 100,
            unitType = com.team.chord.core.domain.model.ingredient.IngredientUnitType.GRAM,
        ),
        PriceHistoryItem(
            id = 4L,
            date = "25.09.08",
            price = 4800,
            unitAmount = 100,
            unitType = com.team.chord.core.domain.model.ingredient.IngredientUnitType.GRAM,
        ),
    )
}
