package com.team.chord.feature.menu.add.menusearch

import com.team.chord.core.domain.model.menu.MenuTemplate

data class MenuSearchUiState(
    val searchQuery: String = "",
    val searchResults: List<MenuTemplate> = emptyList(),
    val selectedTemplate: MenuTemplate? = null,
    val showTemplateDialog: Boolean = false,
    val isLoading: Boolean = false,
    val isApplyingTemplate: Boolean = false,
    val navigateWithTemplate: MenuTemplate? = null,
)
