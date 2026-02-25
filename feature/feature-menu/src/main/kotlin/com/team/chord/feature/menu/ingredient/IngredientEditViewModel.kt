package com.team.chord.feature.menu.ingredient

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.domain.model.menu.MenuRecipe
import com.team.chord.core.domain.usecase.ingredient.GetIngredientDetailUseCase
import com.team.chord.core.domain.usecase.menu.GetMenuDetailUseCase
import com.team.chord.core.domain.usecase.menu.GetMenuRecipesUseCase
import com.team.chord.core.domain.usecase.menu.UpdateRecipeAmountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class IngredientEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMenuDetailUseCase: GetMenuDetailUseCase,
    private val getMenuRecipesUseCase: GetMenuRecipesUseCase,
    private val updateRecipeAmountUseCase: UpdateRecipeAmountUseCase,
    private val getIngredientDetailUseCase: GetIngredientDetailUseCase,
) : ViewModel() {

    private val menuId: Long = savedStateHandle.get<Long>("menuId") ?: 0L
    private val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    private val _uiState = MutableStateFlow(IngredientEditUiState())
    val uiState: StateFlow<IngredientEditUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun showEditBottomSheet(recipe: EditableRecipeUi) {
        _uiState.update {
            it.copy(
                sheetState = EditIngredientSheetUi(
                    recipe = recipe,
                    amountInput = formatAmount(recipe.amount),
                    isIngredientInfoLoading = true,
                ),
                errorMessage = null,
            )
        }
        loadIngredientInfo(recipe.ingredientId)
    }

    fun hideEditBottomSheet() {
        _uiState.update { it.copy(sheetState = null) }
    }

    fun onAmountInputChange(input: String) {
        val normalizedInput = normalizeDecimalInput(input)
        _uiState.update { state ->
            val sheet = state.sheetState ?: return@update state
            state.copy(sheetState = sheet.copy(amountInput = normalizedInput))
        }
    }

    fun submitAmountUpdate() {
        val currentState = _uiState.value
        val sheet = currentState.sheetState ?: return
        val amount = sheet.parsedAmount ?: return
        if (!sheet.isSubmitEnabled || currentState.isSubmitting) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            when (val result = updateRecipeAmountUseCase(menuId, sheet.recipe.recipeId, amount)) {
                is Result.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            isSubmitting = false,
                            recipes = state.recipes.map { recipe ->
                                if (recipe.recipeId == sheet.recipe.recipeId) {
                                    recipe.copy(amount = amount)
                                } else {
                                    recipe
                                }
                            },
                            sheetState = null,
                            toastMessage = "재료의 사용량이 수정됐어요",
                            errorMessage = null,
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = result.exception.message ?: "재료 사용량 수정 중 오류가 발생했어요",
                        )
                    }
                }

                Result.Loading -> Unit
            }
        }
    }

    fun onToastShown() {
        _uiState.update { it.copy(toastMessage = null) }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val menu = getMenuDetailUseCase(menuId)
                if (menu == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "메뉴를 찾을 수 없어요",
                        )
                    }
                    return@launch
                }

                val recipes = getMenuRecipesUseCase(menuId).map(::toEditableRecipeUi)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        menuId = menu.id,
                        menuName = menu.name,
                        recipes = recipes,
                        errorMessage = null,
                    )
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "재료 목록을 불러오는 중 오류가 발생했어요",
                    )
                }
            }
        }
    }

    private fun loadIngredientInfo(ingredientId: Long) {
        viewModelScope.launch {
            try {
                val ingredientDetail = getIngredientDetailUseCase(ingredientId)
                val unitPriceText = ingredientDetail?.let {
                    "${numberFormat.format(it.baseQuantity)}${it.unit.displayName}당 ${numberFormat.format(it.currentUnitPrice)}원"
                } ?: "-"
                val supplierText = ingredientDetail?.supplier?.takeIf { it.isNotBlank() } ?: "-"

                _uiState.update { state ->
                    val sheet = state.sheetState ?: return@update state
                    if (sheet.recipe.ingredientId != ingredientId) return@update state
                    state.copy(
                        sheetState = sheet.copy(
                            unitPriceText = unitPriceText,
                            supplierText = supplierText,
                            isIngredientInfoLoading = false,
                        ),
                    )
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (_: Exception) {
                _uiState.update { state ->
                    val sheet = state.sheetState ?: return@update state
                    if (sheet.recipe.ingredientId != ingredientId) return@update state
                    state.copy(
                        sheetState = sheet.copy(
                            unitPriceText = "-",
                            supplierText = "-",
                            isIngredientInfoLoading = false,
                        ),
                    )
                }
            }
        }
    }

    private fun toEditableRecipeUi(recipe: MenuRecipe): EditableRecipeUi =
        EditableRecipeUi(
            recipeId = recipe.recipeId,
            ingredientId = recipe.ingredientId,
            name = recipe.ingredientName,
            amount = recipe.amount,
            unit = resolveUnit(recipe.unitCode),
            price = recipe.price,
        )

    private fun resolveUnit(unitCode: String): IngredientUnit {
        return IngredientUnit.entries.find { it.name.equals(unitCode, ignoreCase = true) } ?: IngredientUnit.G
    }

    private fun formatAmount(value: Double): String {
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString()
    }

    private fun normalizeDecimalInput(value: String): String {
        if (value.isBlank()) return ""

        val normalized = buildString {
            var hasDot = false
            value.forEach { char ->
                when {
                    char.isDigit() -> append(char)
                    char == '.' && !hasDot -> {
                        if (isEmpty()) append('0')
                        append(char)
                        hasDot = true
                    }
                }
            }
        }

        return normalized
    }
}
