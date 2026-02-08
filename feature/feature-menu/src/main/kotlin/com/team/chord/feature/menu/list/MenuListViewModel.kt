package com.team.chord.feature.menu.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.usecase.menu.GetCategoriesUseCase
import com.team.chord.core.domain.usecase.menu.GetMenuListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CancellationException
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
    private var loadJob: Job? = null

    init {
        loadData()
    }

    fun onCategorySelected(categoryCode: String?) {
        selectedCategoryCode.value = categoryCode
    }

    fun refresh() {
        loadData(isRefresh = true)
    }

    private fun loadData(isRefresh: Boolean = false) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update {
                if (isRefresh) it.copy(isRefreshing = true)
                else it.copy(isLoading = true)
            }

            try {
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
                        isRefreshing = false,
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
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, isRefreshing = false, errorMessage = e.message) }
            }
        }
    }
}
