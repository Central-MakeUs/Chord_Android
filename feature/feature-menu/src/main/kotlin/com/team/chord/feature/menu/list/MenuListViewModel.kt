package com.team.chord.feature.menu.list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MenuListViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(MenuListUiState())
        val uiState: StateFlow<MenuListUiState> = _uiState.asStateFlow()

        init {
            loadMenuItems()
        }

        fun onCategorySelected(category: MenuCategory) {
            _uiState.update { currentState ->
                val filteredItems =
                    getMockMenuItems().filter { item ->
                        when (category) {
                            MenuCategory.ALL -> true
                            MenuCategory.BEVERAGE -> item.id in listOf(1L, 2L, 3L)
                            MenuCategory.DESSERT -> item.id in listOf(4L, 5L)
                        }
                    }
                currentState.copy(
                    selectedCategory = category,
                    menuItems = filteredItems,
                )
            }
        }

        private fun loadMenuItems() {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    menuItems = getMockMenuItems(),
                )
            }
        }

        private fun getMockMenuItems(): List<MenuItemUi> =
            listOf(
                MenuItemUi(
                    id = 1L,
                    name = "아메리카노",
                    sellingPrice = 4500,
                    costRatio = 0.15f,
                    marginRatio = 0.85f,
                    status = MenuStatus.SAFE,
                ),
                MenuItemUi(
                    id = 2L,
                    name = "카페라떼",
                    sellingPrice = 5000,
                    costRatio = 0.22f,
                    marginRatio = 0.78f,
                    status = MenuStatus.SAFE,
                ),
                MenuItemUi(
                    id = 3L,
                    name = "바닐라라떼",
                    sellingPrice = 5500,
                    costRatio = 0.35f,
                    marginRatio = 0.65f,
                    status = MenuStatus.WARNING,
                ),
                MenuItemUi(
                    id = 4L,
                    name = "치즈케이크",
                    sellingPrice = 6000,
                    costRatio = 0.45f,
                    marginRatio = 0.55f,
                    status = MenuStatus.DANGER,
                ),
                MenuItemUi(
                    id = 5L,
                    name = "티라미수",
                    sellingPrice = 7000,
                    costRatio = 0.32f,
                    marginRatio = 0.68f,
                    status = MenuStatus.WARNING,
                ),
            )
    }
