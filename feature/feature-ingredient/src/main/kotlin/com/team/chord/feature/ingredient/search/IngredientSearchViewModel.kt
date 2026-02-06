package com.team.chord.feature.ingredient.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.usecase.ingredient.ManageRecentSearchUseCase
import com.team.chord.core.domain.usecase.ingredient.SearchMyIngredientsUseCase
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
    private val searchMyIngredientsUseCase: SearchMyIngredientsUseCase,
    private val manageRecentSearchUseCase: ManageRecentSearchUseCase,
) : ViewModel() {

    private val _query = MutableStateFlow("")

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
            searchMyIngredientsUseCase(query).map { ingredients ->
                ingredients.map { ingredient ->
                    SearchResultItemUi(
                        id = ingredient.id,
                        name = ingredient.name,
                    )
                }
            }
        }
    }

    val uiState: StateFlow<IngredientSearchUiState> = combine(
        _query,
        recentSearches,
        searchResults,
    ) { query, recentSearches, searchResults ->
        IngredientSearchUiState(
            query = query,
            isSearching = false,
            recentSearches = recentSearches,
            searchResults = searchResults,
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

    fun onClearQuery() {
        _query.value = ""
    }
}
