package com.team.chord.feature.setup.menudetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MenuDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuDetailUiState())
    val uiState: StateFlow<MenuDetailUiState> = _uiState.asStateFlow()

    init {
        val menuName = savedStateHandle.get<String>("menuName") ?: ""
        val isTemplateApplied = savedStateHandle.get<Boolean>("isTemplateApplied") ?: false
        val templatePrice = savedStateHandle.get<Int>("templatePrice") ?: 0

        _uiState.update {
            it.copy(
                menuName = menuName,
                isTemplateApplied = isTemplateApplied,
                price = if (isTemplateApplied && templatePrice > 0) templatePrice.toString() else "",
            ).updateNextEnabled()
        }
    }

    fun onMenuNameChanged(name: String) {
        _uiState.update {
            it.copy(menuName = name).updateNextEnabled()
        }
    }

    fun onPriceChanged(price: String) {
        val filteredPrice = price.filter { it.isDigit() }
        _uiState.update {
            it.copy(price = filteredPrice).updateNextEnabled()
        }
    }

    fun onCategorySelected(category: MenuCategory) {
        _uiState.update {
            it.copy(selectedCategory = category)
        }
    }

    fun onPreparationTimeChanged(minutes: Int, seconds: Int) {
        _uiState.update {
            it.copy(
                preparationMinutes = minutes,
                preparationSeconds = seconds,
            )
        }
    }

    fun createMenuDetailData(): MenuDetailData {
        val currentState = _uiState.value
        return MenuDetailData(
            menuName = currentState.menuName,
            price = currentState.price.toIntOrNull() ?: 0,
            category = currentState.selectedCategory,
            preparationMinutes = currentState.preparationMinutes,
            preparationSeconds = currentState.preparationSeconds,
            isTemplateApplied = currentState.isTemplateApplied,
        )
    }

    private fun MenuDetailUiState.updateNextEnabled(): MenuDetailUiState =
        copy(isNextEnabled = price.isNotBlank())

    fun onShowTimePicker() {
        _uiState.update { it.copy(showTimePicker = true) }
    }

    fun onDismissTimePicker() {
        _uiState.update { it.copy(showTimePicker = false) }
    }

    fun onShowCategoryPicker() {
        _uiState.update { it.copy(showCategoryPicker = true) }
    }

    fun onDismissCategoryPicker() {
        _uiState.update { it.copy(showCategoryPicker = false) }
    }
}
