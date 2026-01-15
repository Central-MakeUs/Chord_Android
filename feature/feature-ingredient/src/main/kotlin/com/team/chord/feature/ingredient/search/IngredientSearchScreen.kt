package com.team.chord.feature.ingredient.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.component.ChordSearchBar
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.feature.ingredient.component.RecentSearchItem
import com.team.chord.feature.ingredient.component.SearchResultItem

@Composable
fun IngredientSearchScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IngredientSearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    IngredientSearchScreenContent(
        uiState = uiState,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::onSearch,
        onCancelClick = onNavigateBack,
        onRecentSearchClick = viewModel::onRecentSearchClick,
        onRecentSearchDelete = viewModel::onRecentSearchDelete,
        onAddIngredient = viewModel::onAddIngredient,
        modifier = modifier,
    )
}

@Composable
internal fun IngredientSearchScreenContent(
    uiState: IngredientSearchUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onCancelClick: () -> Unit,
    onRecentSearchClick: (String) -> Unit,
    onRecentSearchDelete: (Long) -> Unit,
    onAddIngredient: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale200),
    ) {
        // Search bar
        ChordSearchBar(
            query = uiState.query,
            onQueryChange = onQueryChange,
            placeholder = "재료명, 메뉴명으로 검색",
            backgroundColor = Grayscale200,
            onSearch = onSearch,
            onCancelClick = onCancelClick,
        )

        if (uiState.showRecentSearches) {
            // Recent searches section
            RecentSearchesSection(
                recentSearches = uiState.recentSearches,
                onItemClick = onRecentSearchClick,
                onDeleteClick = onRecentSearchDelete,
            )
        } else {
            // Search results section
            SearchResultsSection(
                searchResults = uiState.searchResults,
                onAddClick = onAddIngredient,
            )
        }
    }
}

@Composable
private fun RecentSearchesSection(
    recentSearches: List<RecentSearchUi>,
    onItemClick: (String) -> Unit,
    onDeleteClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = Grayscale200
            )
            .padding(top = 20.dp),
    ) {
        Text(
            text = "최근 검색",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Grayscale700,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(
                items = recentSearches,
                key = { it.id },
            ) { recentSearch ->
                RecentSearchItem(
                    query = recentSearch.query,
                    onClick = { onItemClick(recentSearch.query) },
                    onDeleteClick = { onDeleteClick(recentSearch.id) },
                )
            }
        }
    }
}

@Composable
private fun SearchResultsSection(
    searchResults: List<SearchResultItemUi>,
    onAddClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = Grayscale200,
            )
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = searchResults,
            key = { it.id },
        ) { result ->
            SearchResultItem(
                name = result.name,
                isAdded = result.isAdded,
                onAddClick = { onAddClick(result.id) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IngredientSearchScreenRecentSearchesPreview() {
    IngredientSearchScreenContent(
        uiState = IngredientSearchUiState(
            query = "",
            showRecentSearches = true,
            recentSearches = listOf(
                RecentSearchUi(id = 1, query = "레몬티"),
                RecentSearchUi(id = 2, query = "말차 가루"),
            ),
        ),
        onQueryChange = {},
        onSearch = {},
        onCancelClick = {},
        onRecentSearchClick = {},
        onRecentSearchDelete = {},
        onAddIngredient = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun IngredientSearchScreenSearchResultsPreview() {
    IngredientSearchScreenContent(
        uiState = IngredientSearchUiState(
            query = "돌체 라떼",
            showRecentSearches = false,
            searchResults = listOf(
                SearchResultItemUi(id = 1, name = "원두", isAdded = false),
                SearchResultItemUi(id = 2, name = "돌체 시나몬 시럽", isAdded = false),
                SearchResultItemUi(id = 3, name = "우유", isAdded = false),
                SearchResultItemUi(id = 4, name = "종이컵", isAdded = false),
                SearchResultItemUi(id = 5, name = "컵 홀더", isAdded = false),
            ),
        ),
        onQueryChange = {},
        onSearch = {},
        onCancelClick = {},
        onRecentSearchClick = {},
        onRecentSearchDelete = {},
        onAddIngredient = {},
    )
}
