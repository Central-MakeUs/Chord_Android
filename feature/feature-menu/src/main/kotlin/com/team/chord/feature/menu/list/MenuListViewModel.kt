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

    private val selectedCategoryId = MutableStateFlow<Long?>(null)

    init {
        loadData()
    }

    fun onCategorySelected(categoryId: Long?) {
        selectedCategoryId.value = categoryId
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            combine(
                getCategoriesUseCase(),
                getMenuListUseCase(),
                selectedCategoryId,
            ) { categories, menus, selectedId ->
                val filteredMenus = if (selectedId == null) {
                    menus
                } else {
                    menus.filter { it.category.id == selectedId }
                }

                MenuListUiState(
                    isLoading = false,
                    categories = categories,
                    selectedCategoryId = selectedId,
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
