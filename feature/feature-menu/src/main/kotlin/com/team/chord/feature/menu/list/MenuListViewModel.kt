package com.team.chord.feature.menu.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.usecase.menu.GetCategoriesUseCase
import com.team.chord.core.domain.usecase.menu.GetMenuListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuListViewModel @Inject constructor(
    private val getMenuListUseCase: GetMenuListUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuListUiState())
    val uiState: StateFlow<MenuListUiState> = _uiState.asStateFlow()

    private val selectedCategoryCode = MutableStateFlow<String?>(null)

    init {
        loadData()
    }

    fun onCategorySelected(categoryCode: String?) {
        selectedCategoryCode.value = categoryCode
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            combine(
                getCategoriesUseCase(),
                getMenuListUseCase(),
                selectedCategoryCode,
            ) { categories, menus, selectedCode ->
                val filteredMenus = if (selectedCode == null) {
                    menus
                } else {
                    menus.filter { it.categoryCode == selectedCode }
                }

                MenuListUiState(
                    isLoading = false,
                    categories = categories,
                    selectedCategoryCode = selectedCode,
                    menuItems = filteredMenus.map { menu ->
                        MenuItemUi(
                            id = menu.id,
                            name = menu.name,
                            sellingPrice = menu.price,
                            costRatio = menu.costRatio,
                            marginRatio = menu.marginRatio,
                            marginGrade = menu.marginGrade,
                        )
                    },
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}
