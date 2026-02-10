package com.team.chord.feature.menu.add.menusearch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.menu.MenuTemplate
import com.team.chord.core.domain.repository.MenuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuSearchViewModel @Inject constructor(
    private val menuRepository: MenuRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuSearchUiState())
    val uiState: StateFlow<MenuSearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onSearchQueryChanged(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                searchResults = emptyList(),
                isLoading = false,
            )
        }
        searchJob?.cancel()
    }

    fun onSearchSubmit() {
        val query = _uiState.value.searchQuery
        if (query.isBlank()) return

        Log.d(TAG, "onSearchSubmit: query='$query'")
        performSearch(query)
    }

    fun onClearSearch() {
        searchJob?.cancel()
        _uiState.update {
            it.copy(
                searchQuery = "",
                searchResults = emptyList(),
                isLoading = false,
            )
        }
    }

    private fun performSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            Log.d(TAG, "performSearch: Starting search for query='$query'")
            _uiState.update { it.copy(isLoading = true, searchResults = emptyList()) }

            try {
                menuRepository.searchMenuTemplates(query).collectLatest { results ->
                    Log.d(TAG, "performSearch: API success for query='$query', ${results.size} results")
                    _uiState.update {
                        it.copy(
                            searchResults = results,
                            isLoading = false,
                        )
                    }
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                Log.d(TAG, "performSearch: Cancelled for query='$query' (superseded)")
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "performSearch: API error for query='$query' - ${e.javaClass.simpleName}: ${e.message}")
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onTemplateSelected(template: MenuTemplate) {
        _uiState.update {
            it.copy(
                selectedTemplate = template,
                showTemplateDialog = true,
            )
        }
    }

    fun onDismissTemplateDialog() {
        _uiState.update {
            it.copy(
                showTemplateDialog = false,
                selectedTemplate = null,
            )
        }
    }

    fun onConfirmTemplateApply() {
        val selected = _uiState.value.selectedTemplate ?: return

        _uiState.update {
            it.copy(showTemplateDialog = false, isApplyingTemplate = true)
        }

        viewModelScope.launch {
            try {
                val fullTemplate = menuRepository.getTemplateBasic(selected.templateId)
                Log.d(TAG, "onConfirmTemplateApply: Got template details - ${fullTemplate?.menuName}, price=${fullTemplate?.defaultSellingPrice}")
                _uiState.update {
                    it.copy(
                        isApplyingTemplate = false,
                        navigateWithTemplate = fullTemplate ?: selected,
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "onConfirmTemplateApply: Failed to fetch template - ${e.message}")
                _uiState.update {
                    it.copy(
                        isApplyingTemplate = false,
                        navigateWithTemplate = selected,
                    )
                }
            }
        }
    }

    fun onNavigationHandled() {
        _uiState.update {
            it.copy(navigateWithTemplate = null, selectedTemplate = null)
        }
    }

    companion object {
        private const val TAG = "MenuSearch"
    }
}
