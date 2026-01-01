package com.team.chord.feature.setup.menuentry

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MenuEntryViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(MenuEntryUiState())
        val uiState: StateFlow<MenuEntryUiState> = _uiState.asStateFlow()

        private val _registeredMenu = MutableStateFlow<RegisteredMenu?>(null)
        val registeredMenu: StateFlow<RegisteredMenu?> = _registeredMenu.asStateFlow()

        fun onCategorySelected(category: MenuCategory) {
            _uiState.update {
                it.copy(selectedCategory = category).updateRegisterEnabled()
            }
        }

        fun onMenuNameChanged(name: String) {
            _uiState.update {
                it.copy(menuName = name).updateRegisterEnabled()
            }
        }

        fun onMenuPriceChanged(price: String) {
            val filteredPrice = price.filter { it.isDigit() }
            _uiState.update {
                it.copy(menuPrice = filteredPrice).updateRegisterEnabled()
            }
        }

        fun onNewIngredientNameChanged(name: String) {
            _uiState.update {
                it.copy(newIngredientName = name)
            }
        }

        fun onAddIngredient() {
            val currentState = _uiState.value
            if (currentState.newIngredientName.isBlank()) return

            val newIngredient =
                Ingredient(
                    id = UUID.randomUUID().toString(),
                    name = currentState.newIngredientName,
                    isSelected = true,
                )
            _uiState.update {
                it
                    .copy(
                        ingredients = it.ingredients + newIngredient,
                        newIngredientName = "",
                    ).updateRegisterEnabled()
            }
        }

        fun onIngredientToggle(ingredientId: String) {
            _uiState.update { state ->
                state
                    .copy(
                        ingredients =
                            state.ingredients.map { ingredient ->
                                if (ingredient.id == ingredientId) {
                                    ingredient.copy(isSelected = !ingredient.isSelected)
                                } else {
                                    ingredient
                                }
                            },
                    ).updateRegisterEnabled()
            }
        }

        fun onRemoveIngredient(ingredientId: String) {
            _uiState.update { state ->
                state
                    .copy(
                        ingredients = state.ingredients.filter { it.id != ingredientId },
                    ).updateRegisterEnabled()
            }
        }

        fun onIngredientPriceChanged(price: String) {
            val filteredPrice = price.filter { it.isDigit() }
            _uiState.update {
                it.copy(ingredientPrice = filteredPrice).updateRegisterEnabled()
            }
        }

        fun onWeightChanged(weight: String) {
            val filteredWeight = weight.filter { it.isDigit() }
            _uiState.update {
                it.copy(weight = filteredWeight).updateRegisterEnabled()
            }
        }

        fun onWeightUnitSelected(unit: WeightUnit) {
            _uiState.update {
                it.copy(
                    weightUnit = unit,
                    isWeightUnitDropdownExpanded = false,
                )
            }
        }

        fun onWeightUnitDropdownToggle() {
            _uiState.update {
                it.copy(isWeightUnitDropdownExpanded = !it.isWeightUnitDropdownExpanded)
            }
        }

        fun onWeightUnitDropdownDismiss() {
            _uiState.update {
                it.copy(isWeightUnitDropdownExpanded = false)
            }
        }

        fun onPreparationMinutesChanged(minutes: String) {
            val filteredMinutes = minutes.filter { it.isDigit() }
            _uiState.update {
                it.copy(preparationMinutes = filteredMinutes).updateRegisterEnabled()
            }
        }

        fun onPreparationSecondsChanged(seconds: String) {
            val filteredSeconds = seconds.filter { it.isDigit() }
            _uiState.update {
                it.copy(preparationSeconds = filteredSeconds).updateRegisterEnabled()
            }
        }

        fun onRegisterClicked() {
            val currentState = _uiState.value
            if (!currentState.isRegisterEnabled) return

            _registeredMenu.value =
                RegisteredMenu(
                    id = UUID.randomUUID().toString(),
                    name = currentState.menuName,
                    price = currentState.menuPrice.toIntOrNull() ?: 0,
                    category = currentState.selectedCategory,
                )
        }

        fun consumeRegisteredMenu() {
            _registeredMenu.value = null
        }

        fun resetForm() {
            _uiState.value = MenuEntryUiState()
        }

        private fun MenuEntryUiState.updateRegisterEnabled(): MenuEntryUiState =
            copy(
                isRegisterEnabled = menuName.isNotBlank() && menuPrice.isNotBlank(),
            )
    }

data class RegisteredMenu(
    val id: String,
    val name: String,
    val price: Int,
    val category: MenuCategory,
)
