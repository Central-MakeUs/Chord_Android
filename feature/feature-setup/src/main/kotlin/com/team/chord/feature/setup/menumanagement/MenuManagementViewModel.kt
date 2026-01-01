package com.team.chord.feature.setup.menumanagement

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.team.chord.feature.setup.menuentry.MenuCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MenuManagementViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(MenuManagementUiState())
        val uiState: StateFlow<MenuManagementUiState> = _uiState.asStateFlow()

        fun addMenu(
            name: String,
            price: Int,
            category: MenuCategory,
        ) {
            val newMenuItem =
                MenuItem(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    price = price,
                    category = category,
                )

            _uiState.update { state ->
                when (category) {
                    MenuCategory.BEVERAGE -> {
                        state
                            .copy(
                                beverageMenus = state.beverageMenus + newMenuItem,
                            ).updateCompleteEnabled()
                    }

                    MenuCategory.FOOD -> {
                        state
                            .copy(
                                foodMenus = state.foodMenus + newMenuItem,
                            ).updateCompleteEnabled()
                    }
                }
            }
        }

        fun onDeleteModeToggle() {
            _uiState.update {
                it.copy(
                    isDeleteMode = !it.isDeleteMode,
                    selectedMenuIds = emptySet(),
                )
            }
        }

        fun onMenuSelected(menuId: String) {
            if (!_uiState.value.isDeleteMode) return

            _uiState.update { state ->
                val newSelectedIds =
                    if (menuId in state.selectedMenuIds) {
                        state.selectedMenuIds - menuId
                    } else {
                        state.selectedMenuIds + menuId
                    }
                state.copy(selectedMenuIds = newSelectedIds)
            }
        }

        fun onDeleteSelectedMenus() {
            _uiState.update { state ->
                state
                    .copy(
                        beverageMenus = state.beverageMenus.filter { it.id !in state.selectedMenuIds },
                        foodMenus = state.foodMenus.filter { it.id !in state.selectedMenuIds },
                        selectedMenuIds = emptySet(),
                        isDeleteMode = false,
                    ).updateCompleteEnabled()
            }
        }

        fun onMenuStatusChanged(
            menuId: String,
            status: MenuStatus,
        ) {
            _uiState.update { state ->
                state.copy(
                    beverageMenus =
                        state.beverageMenus.map { menu ->
                            if (menu.id == menuId) {
                                menu.copy(status = status, isStatusDropdownExpanded = false)
                            } else {
                                menu
                            }
                        },
                    foodMenus =
                        state.foodMenus.map { menu ->
                            if (menu.id == menuId) {
                                menu.copy(status = status, isStatusDropdownExpanded = false)
                            } else {
                                menu
                            }
                        },
                )
            }
        }

        fun onMenuStatusDropdownToggle(menuId: String) {
            _uiState.update { state ->
                state.copy(
                    beverageMenus =
                        state.beverageMenus.map { menu ->
                            if (menu.id == menuId) {
                                menu.copy(isStatusDropdownExpanded = !menu.isStatusDropdownExpanded)
                            } else {
                                menu.copy(isStatusDropdownExpanded = false)
                            }
                        },
                    foodMenus =
                        state.foodMenus.map { menu ->
                            if (menu.id == menuId) {
                                menu.copy(isStatusDropdownExpanded = !menu.isStatusDropdownExpanded)
                            } else {
                                menu.copy(isStatusDropdownExpanded = false)
                            }
                        },
                )
            }
        }

        fun onMenuStatusDropdownDismiss(menuId: String) {
            _uiState.update { state ->
                state.copy(
                    beverageMenus =
                        state.beverageMenus.map { menu ->
                            if (menu.id == menuId) {
                                menu.copy(isStatusDropdownExpanded = false)
                            } else {
                                menu
                            }
                        },
                    foodMenus =
                        state.foodMenus.map { menu ->
                            if (menu.id == menuId) {
                                menu.copy(isStatusDropdownExpanded = false)
                            } else {
                                menu
                            }
                        },
                )
            }
        }

        private fun MenuManagementUiState.updateCompleteEnabled(): MenuManagementUiState =
            copy(
                isCompleteEnabled = beverageMenus.isNotEmpty() || foodMenus.isNotEmpty(),
            )
    }
