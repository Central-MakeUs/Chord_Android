package com.team.chord.feature.setup.menuconfirm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MenuConfirmViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuConfirmUiState())
    val uiState: StateFlow<MenuConfirmUiState> = _uiState.asStateFlow()

    init {
        loadRegisteredMenus()
    }

    private fun loadRegisteredMenus() {
        // TODO: Load registered menus from SavedStateHandle or shared state
        // For now, using mock data for UI development
        _uiState.update {
            it.copy(
                registeredMenus = listOf(
                    RegisteredMenuSummary(
                        index = 1,
                        name = "흑임자 라떼",
                        price = 4500,
                        ingredients = listOf(
                            IngredientSummary(name = "원두", amount = "30g", price = 800),
                            IngredientSummary(name = "물", amount = "250ml", price = 150),
                            IngredientSummary(name = "종이컵", amount = "1개", price = 100),
                            IngredientSummary(name = "테이크아웃 홀더", amount = "1개", price = 150),
                            IngredientSummary(name = "흑임자 토핑", amount = "1개", price = 150),
                        ),
                    ),
                ),
            )
        }
    }

    fun addMenu(menu: RegisteredMenuSummary) {
        _uiState.update { state ->
            val newIndex = state.registeredMenus.size + 1
            val menuWithIndex = menu.copy(index = newIndex)
            state.copy(registeredMenus = state.registeredMenus + menuWithIndex)
        }
    }

    fun removeMenu(index: Int) {
        _uiState.update { state ->
            val updatedMenus = state.registeredMenus
                .filter { it.index != index }
                .mapIndexed { idx, menu -> menu.copy(index = idx + 1) }
            state.copy(registeredMenus = updatedMenus)
        }
    }
}
