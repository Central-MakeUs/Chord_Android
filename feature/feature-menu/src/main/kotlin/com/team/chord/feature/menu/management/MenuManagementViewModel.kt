package com.team.chord.feature.menu.management

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.onSuccess
import com.team.chord.core.domain.usecase.menu.DeleteMenuUseCase
import com.team.chord.core.domain.usecase.menu.GetCategoriesUseCase
import com.team.chord.core.domain.usecase.menu.GetMenuDetailUseCase
import com.team.chord.core.domain.usecase.menu.UpdateMenuCategoryUseCase
import com.team.chord.core.domain.usecase.menu.UpdateMenuNameUseCase
import com.team.chord.core.domain.usecase.menu.UpdateMenuPreparationTimeUseCase
import com.team.chord.core.domain.usecase.menu.UpdateMenuPriceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuManagementViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMenuDetailUseCase: GetMenuDetailUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val updateMenuNameUseCase: UpdateMenuNameUseCase,
    private val updateMenuPriceUseCase: UpdateMenuPriceUseCase,
    private val updateMenuPreparationTimeUseCase: UpdateMenuPreparationTimeUseCase,
    private val updateMenuCategoryUseCase: UpdateMenuCategoryUseCase,
    private val deleteMenuUseCase: DeleteMenuUseCase,
) : ViewModel() {

    private val menuId: Long = savedStateHandle.get<Long>("menuId") ?: 0L

    private val _uiState = MutableStateFlow(MenuManagementUiState())
    val uiState: StateFlow<MenuManagementUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun showNameBottomSheet() {
        _uiState.update { it.copy(showNameBottomSheet = true) }
    }

    fun hideNameBottomSheet() {
        _uiState.update { it.copy(showNameBottomSheet = false) }
    }

    fun showPriceBottomSheet() {
        _uiState.update { it.copy(showPriceBottomSheet = true) }
    }

    fun hidePriceBottomSheet() {
        _uiState.update { it.copy(showPriceBottomSheet = false) }
    }

    fun showTimeBottomSheet() {
        _uiState.update { it.copy(showTimeBottomSheet = true) }
    }

    fun hideTimeBottomSheet() {
        _uiState.update { it.copy(showTimeBottomSheet = false) }
    }

    fun showDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }

    fun hideDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun showDeleteSuccessDialog() {
        _uiState.update { it.copy(showDeleteSuccessDialog = true) }
    }

    fun hideDeleteSuccessDialog() {
        _uiState.update { it.copy(showDeleteSuccessDialog = false) }
    }

    fun updateMenuName(name: String) {
        viewModelScope.launch {
            updateMenuNameUseCase(menuId, name).onSuccess {
                _uiState.update { state ->
                    state.copy(
                        menuName = name,
                        showNameBottomSheet = false,
                    )
                }
            }
        }
    }

    fun updatePrice(price: Int) {
        viewModelScope.launch {
            updateMenuPriceUseCase(menuId, price).onSuccess {
                _uiState.update { state ->
                    state.copy(
                        price = price,
                        showPriceBottomSheet = false,
                    )
                }
            }
        }
    }

    fun updatePreparationTime(seconds: Int) {
        viewModelScope.launch {
            updateMenuPreparationTimeUseCase(menuId, seconds).onSuccess {
                _uiState.update { state ->
                    state.copy(
                        preparationTimeSeconds = seconds,
                        showTimeBottomSheet = false,
                    )
                }
            }
        }
    }

    fun updateCategory(categoryId: Long) {
        viewModelScope.launch {
            updateMenuCategoryUseCase(menuId, categoryId).onSuccess {
                _uiState.update { state ->
                    state.copy(selectedCategoryId = categoryId)
                }
            }
        }
    }

    fun deleteMenu() {
        viewModelScope.launch {
            deleteMenuUseCase(menuId).onSuccess {
                _uiState.update { it.copy(showDeleteDialog = false, showDeleteSuccessDialog = true) }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val menu = getMenuDetailUseCase(menuId)
            val categories = getCategoriesUseCase().first()

            if (menu != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        menuId = menu.id,
                        menuName = menu.name,
                        price = menu.price,
                        preparationTimeSeconds = menu.preparationTimeSeconds,
                        categories = categories,
                        selectedCategoryId = menu.category.id,
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
