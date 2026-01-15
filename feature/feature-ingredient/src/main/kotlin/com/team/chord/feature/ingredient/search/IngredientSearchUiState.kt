package com.team.chord.feature.ingredient.search

/**
 * UI State for Ingredient Search Screen.
 *
 * @property query Current search query text.
 * @property isSearching Whether search is in progress.
 * @property recentSearches List of recent searches to display.
 * @property searchResults List of search result items.
 * @property showRecentSearches Whether to show recent searches (true when query is empty).
 */
data class IngredientSearchUiState(
    val query: String = "",
    val isSearching: Boolean = false,
    val recentSearches: List<RecentSearchUi> = emptyList(),
    val searchResults: List<SearchResultItemUi> = emptyList(),
    val showRecentSearches: Boolean = true,
)

/**
 * UI model for recent search item.
 *
 * @property id Unique identifier for the recent search.
 * @property query The search query text.
 */
data class RecentSearchUi(
    val id: Long,
    val query: String,
)

/**
 * UI model for search result item.
 *
 * @property id Unique identifier for the ingredient.
 * @property name Name of the ingredient.
 * @property isAdded Whether the ingredient has been added to the list.
 */
data class SearchResultItemUi(
    val id: Long,
    val name: String,
    val isAdded: Boolean = false,
)
