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
 * @param isApplyingTemplate Whether template details are being fetched
 * @param navigateWithTemplate One-time event: full template to navigate with after API fetch
 */
data class MenuSearchUiState(
    val searchQuery: String = "",
    val searchResults: List<MenuTemplate> = emptyList(),
    val selectedTemplate: MenuTemplate? = null,
    val showTemplateDialog: Boolean = false,
    val isLoading: Boolean = false,
    val isApplyingTemplate: Boolean = false,
    val navigateWithTemplate: MenuTemplate? = null,
)
