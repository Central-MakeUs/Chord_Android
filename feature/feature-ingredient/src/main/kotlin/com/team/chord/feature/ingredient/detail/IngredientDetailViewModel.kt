package com.team.chord.feature.ingredient.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.ingredient.IngredientFilter
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.domain.usecase.ingredient.DeleteIngredientUseCase
import com.team.chord.core.domain.usecase.ingredient.GetIngredientDetailUseCase
import com.team.chord.core.domain.usecase.ingredient.GetIngredientPriceHistoryUseCase
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
    private val getIngredientPriceHistoryUseCase: GetIngredientPriceHistoryUseCase,
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

            val currentFavorite = currentState.ingredientDetail.isFavorite
            when (updateIngredientUseCase.setFavorite(ingredientId, !currentFavorite)) {
                is Result.Success -> {
                    _uiState.value = currentState.copy(
                        ingredientDetail = currentState.ingredientDetail.copy(
                            isFavorite = !currentFavorite,
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

    fun onUpdatePriceInfo(
        category: IngredientFilter,
        price: Int,
        unitAmount: Int,
        unit: IngredientUnit,
    ) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState !is IngredientDetailUiState.Success) return@launch

            val categoryCode = category.toCategoryCode()
            when (updateIngredientUseCase.updateIngredient(
                ingredientId = ingredientId,
                categoryCode = categoryCode,
                price = price,
                amount = unitAmount,
                unitCode = unit.name,
            )) {
                is Result.Success -> {
                    _uiState.value = currentState.copy(
                        ingredientDetail = currentState.ingredientDetail.copy(
                            category = category,
                            price = price,
                            unitAmount = unitAmount,
                            unit = unit,
                        ),
                    )
                }
                is Result.Error -> {
                    // Keep current state, optionally show error
                }
                is Result.Loading -> {
                    // No-op
                }
            }
        }
    }

    fun onUpdateSupplier(supplier: String) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState !is IngredientDetailUiState.Success) return@launch

            when (updateIngredientUseCase.updateSupplier(ingredientId, supplier)) {
                is Result.Success -> {
                    _uiState.value = currentState.copy(
                        ingredientDetail = currentState.ingredientDetail.copy(
                            supplier = supplier,
                        ),
                    )
                }
                is Result.Error -> {
                    // Keep current state, optionally show error
                }
                is Result.Loading -> {
                    // No-op
                }
            }
        }
    }

    private fun loadIngredientDetail() {
        viewModelScope.launch {
            _uiState.value = IngredientDetailUiState.Loading

            try {
                val ingredient = getIngredientDetailUseCase(ingredientId)
                if (ingredient != null) {
                    val priceHistory = getIngredientPriceHistoryUseCase(ingredientId)
                    _uiState.value = IngredientDetailUiState.Success(
                        IngredientDetailUi(
                            id = ingredient.id,
                            category = ingredient.categoryCode.toIngredientFilter(),
                            name = ingredient.name,
                            price = ingredient.currentUnitPrice,
                            unitAmount = ingredient.baseQuantity,
                            unit = ingredient.unit,
                            supplier = ingredient.supplier ?: "",
                            isFavorite = ingredient.isFavorite,
                            usedMenus = ingredient.usedMenus.map { usedMenu ->
                                UsedMenuUi(
                                    id = usedMenu.id,
                                    name = usedMenu.name,
                                    usageAmount = usedMenu.usageAmount,
                                )
                            },
                            priceHistory = priceHistory.map { history ->
                                PriceHistoryUi(
                                    id = history.id,
                                    date = history.date.toDisplayDate(),
                                    price = history.price,
                                    unitAmount = history.unitAmount,
                                    unitDisplayName = history.unit.displayName,
                                )
                            },
                        ),
                    )
                } else {
                    _uiState.value = IngredientDetailUiState.Error("재료를 찾을 수 없습니다")
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = IngredientDetailUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다")
            }
        }
    }

    private fun String.toIngredientFilter(): IngredientFilter = when (this) {
        "INGREDIENTS" -> IngredientFilter.FOOD_INGREDIENT
        "MATERIALS" -> IngredientFilter.OPERATIONAL_SUPPLY
        else -> IngredientFilter.FOOD_INGREDIENT
    }

    private fun IngredientFilter.toCategoryCode(): String = when (this) {
        IngredientFilter.FOOD_INGREDIENT -> "INGREDIENTS"
        IngredientFilter.OPERATIONAL_SUPPLY -> "MATERIALS"
        IngredientFilter.FAVORITE -> "INGREDIENTS"
    }

    private fun String.toDisplayDate(): String {
        if (length < 10 || !contains('-')) return this
        val year = substring(2, 4)
        val month = substring(5, 7)
        val day = substring(8, 10)
        return "$year.$month.$day"
    }
}
