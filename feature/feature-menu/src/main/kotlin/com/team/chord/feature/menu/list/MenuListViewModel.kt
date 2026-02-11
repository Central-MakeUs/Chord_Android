package com.team.chord.feature.menu.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.usecase.menu.GetCategoriesUseCase
import com.team.chord.core.domain.usecase.menu.GetMenuListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
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

    private var selectedCategoryCode: String? = null
    private var menusByCategory: Map<String, List<MenuItemUi>> = emptyMap()
    private var loadJob: Job? = null

    init {
        loadData()
    }

    fun onCategorySelected(categoryCode: String?) {
        selectedCategoryCode = categoryCode
        _uiState.update {
            it.copy(
                selectedCategoryCode = categoryCode,
                menuItems = getMenusForCategory(categoryCode),
            )
        }
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
                val categories = getCategoriesUseCase().first()

                val menuResults = coroutineScope {
                    categories.map { category ->
                        async {
                            category.code to getMenuListUseCase(category.code).first()
                        }
                    }.awaitAll().toMap()
                }

                menusByCategory = menuResults.mapValues { (_, menus) ->
                    menus.map { menu ->
                        MenuItemUi(
                            id = menu.id,
                            name = menu.name,
                            sellingPrice = menu.price,
                            costRatio = menu.costRatio,
                            marginRatio = menu.marginRatio,
                            marginGrade = menu.marginGrade,
                        )
                    }
                }

                _uiState.value = MenuListUiState(
                    categories = categories,
                    selectedCategoryCode = selectedCategoryCode,
                    menuItems = getMenusForCategory(selectedCategoryCode),
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, isRefreshing = false, errorMessage = e.message)
                }
            }
        }
    }

    private fun getMenusForCategory(categoryCode: String?): List<MenuItemUi> =
        if (categoryCode == null) menusByCategory.values.flatten()
        else menusByCategory[categoryCode].orEmpty()
}
