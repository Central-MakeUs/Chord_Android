package com.team.chord.feature.ingredient.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.domain.model.ingredient.IngredientFilter
import com.team.chord.core.ui.R
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.ingredient.component.IngredientFilterChip
import com.team.chord.feature.ingredient.component.IngredientListItem

@Composable
fun IngredientListScreen(
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IngredientListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.refresh()
        onPauseOrDispose { }
    }

    IngredientListScreenContent(
        uiState = uiState,
        onFilterToggle = viewModel::onFilterToggle,
        onFilterRemove = viewModel::onFilterRemove,
        onNavigateToDetail = onNavigateToDetail,
        onSearchClick = onNavigateToSearch,
        onMoreClick = { /* TODO: Implement more options */ },
        onRefresh = viewModel::refresh,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun IngredientListScreenContent(
    uiState: IngredientListUiState,
    onFilterToggle: (IngredientFilter) -> Unit,
    onFilterRemove: (IngredientFilter) -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onSearchClick: () -> Unit,
    onMoreClick: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale200),
    ) {
        IngredientListHeader(
            ingredientCount = uiState.ingredients.size,
            onSearchClick = onSearchClick,
            onMoreClick = onMoreClick,
        )

        Spacer(modifier = Modifier.height(12.dp))

        IngredientFilterChipRow(
            activeFilters = uiState.activeFilters,
            onFilterToggle = onFilterToggle,
            onFilterRemove = onFilterRemove,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Grayscale100,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                )
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp),
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = PrimaryBlue500)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    itemsIndexed(
                        items = uiState.ingredients,
                        key = { _, item -> item.id },
                    ) { index, ingredient ->
                        IngredientListItem(
                            name = ingredient.name,
                            price = ingredient.price,
                            usage = ingredient.usage,
                            onClick = { onNavigateToDetail(ingredient.id) },
                        )
                        if (index < uiState.ingredients.lastIndex) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Grayscale300,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IngredientListHeader(
    ingredientCount: Int,
    onSearchClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Grayscale200)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "재료 ",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Grayscale900,
            )
            Text(
                text = "$ingredientCount",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = PrimaryBlue500,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "검색",
                tint = Grayscale600,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onSearchClick),
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_ellipsis),
                contentDescription = "더보기",
                tint = Grayscale900,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onMoreClick),
            )
        }
    }
}

@Composable
private fun IngredientFilterChipRow(
    activeFilters: Set<IngredientFilter>,
    onFilterToggle: (IngredientFilter) -> Unit,
    onFilterRemove: (IngredientFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(IngredientFilter.entries.toList()) { filter ->
            val isSelected = activeFilters.contains(filter)
            IngredientFilterChip(
                text = filter.displayName,
                isSelected = isSelected,
                onClick = { onFilterToggle(filter) },
                onRemove = if (isSelected) {
                    { onFilterRemove(filter) }
                } else {
                    null
                },
            )
        }
    }
}
