package com.team.chord.feature.setup.menusearch

import com.team.chord.core.domain.model.menu.MenuTemplate

/**
 * UI State for MenuSearchScreen.
 *
 * @param searchQuery Current search query string
 * @param searchResults List of menu templates matching the search query
 * @param selectedTemplate Currently selected template for confirmation
 * @param showTemplateDialog Whether to show the template apply confirmation dialog
 * @param isLoading Whether a search is currently in progress
 */
data class MenuSearchUiState(
    val searchQuery: String = "",
    val searchResults: List<MenuTemplate> = emptyList(),
    val selectedTemplate: MenuTemplate? = null,
    val showTemplateDialog: Boolean = false,
    val isLoading: Boolean = false,
)
