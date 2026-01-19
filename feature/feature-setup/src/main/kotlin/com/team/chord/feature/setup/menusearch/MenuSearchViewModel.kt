package com.team.chord.feature.setup.menusearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.menu.MenuTemplate
import com.team.chord.core.domain.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MenuSearchViewModel @Inject constructor(
    private val menuRepository: MenuRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuSearchUiState())
    val uiState: StateFlow<MenuSearchUiState> = _uiState.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")

    init {
        observeSearchQuery()
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(SEARCH_DEBOUNCE_MS)
                .distinctUntilChanged()
                .collectLatest { query ->
                    performSearch(query)
                }
        }
    }

    private suspend fun performSearch(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList(), isLoading = false) }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        menuRepository.searchMenuTemplates(query).collectLatest { results ->
            _uiState.update {
                it.copy(
                    searchResults = results,
                    isLoading = false,
                )
            }
        }
    }

    /**
     * Called when user types in the search field.
     */
    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchQueryFlow.value = query
    }

    /**
     * Called when user clears the search field.
     */
    fun onClearSearch() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                searchResults = emptyList(),
            )
        }
        searchQueryFlow.value = ""
    }

    /**
     * Called when user taps on a template from the search results.
     * Shows the template apply confirmation dialog.
     */
    fun onTemplateSelected(template: MenuTemplate) {
        _uiState.update {
            it.copy(
                selectedTemplate = template,
                showTemplateDialog = true,
            )
        }
    }

    /**
     * Called when user dismisses the template dialog.
     */
    fun onDismissTemplateDialog() {
        _uiState.update {
            it.copy(
                showTemplateDialog = false,
                selectedTemplate = null,
            )
        }
    }

    /**
     * Called when user confirms applying the template.
     */
    fun onConfirmTemplateApply() {
        _uiState.update {
            it.copy(showTemplateDialog = false)
        }
        // The selected template will be used by the caller for navigation
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 300L
    }
}
