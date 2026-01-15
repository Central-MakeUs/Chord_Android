package com.team.chord.feature.ingredient.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.usecase.ingredient.AddIngredientToListUseCase
import com.team.chord.core.domain.usecase.ingredient.ManageRecentSearchUseCase
import com.team.chord.core.domain.usecase.ingredient.SearchIngredientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class IngredientSearchViewModel @Inject constructor(
    private val searchIngredientUseCase: SearchIngredientUseCase,
    private val manageRecentSearchUseCase: ManageRecentSearchUseCase,
    private val addIngredientToListUseCase: AddIngredientToListUseCase,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _addedIngredientIds = MutableStateFlow<Set<Long>>(emptySet())

    private val recentSearches = manageRecentSearchUseCase.getRecentSearches()
        .map { searches ->
            searches.map { search ->
                RecentSearchUi(
                    id = search.id,
                    query = search.query,
                )
            }
        }

    private val searchResults = _query.flatMapLatest { query ->
        if (query.isBlank()) {
            flowOf(emptyList())
        } else {
            searchIngredientUseCase(query).map { ingredients ->
                ingredients.map { ingredient ->
                    SearchResultItemUi(
                        id = ingredient.id,
                        name = ingredient.name,
                        isAdded = false,
                    )
                }
            }
        }
    }

    val uiState: StateFlow<IngredientSearchUiState> = combine(
        _query,
        recentSearches,
        searchResults,
        _addedIngredientIds,
    ) { query, recentSearches, searchResults, addedIds ->
        val updatedResults = searchResults.map { item ->
            item.copy(isAdded = addedIds.contains(item.id))
        }

        IngredientSearchUiState(
            query = query,
            isSearching = false,
            recentSearches = recentSearches,
            searchResults = updatedResults,
            showRecentSearches = query.isBlank(),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = IngredientSearchUiState(),
    )

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun onSearch() {
        val currentQuery = _query.value.trim()
        if (currentQuery.isNotBlank()) {
            viewModelScope.launch {
                manageRecentSearchUseCase.addRecentSearch(currentQuery)
            }
        }
    }

    fun onRecentSearchClick(query: String) {
        _query.value = query
        onSearch()
    }

    fun onRecentSearchDelete(id: Long) {
        viewModelScope.launch {
            manageRecentSearchUseCase.deleteRecentSearch(id)
        }
    }

    fun onAddIngredient(ingredientId: Long) {
        viewModelScope.launch {
            when (addIngredientToListUseCase(ingredientId)) {
                is Result.Success -> {
                    _addedIngredientIds.value = _addedIngredientIds.value + ingredientId
                }
                is Result.Error -> {
                    // TODO: Handle error - show snackbar or toast
                }
                is Result.Loading -> {
                    // No-op: Loading state is handled elsewhere
                }
            }
        }
    }

    fun onClearQuery() {
        _query.value = ""
    }
}
