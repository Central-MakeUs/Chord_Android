package com.team.chord.feature.menu.list

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.team.chord.core.ui.R
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.menu.component.CategoryTabRow
import com.team.chord.feature.menu.component.MenuListItem

@Composable
fun MenuListScreen(
    onNavigateToDetail: (Long) -> Unit,
    onAddMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MenuListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.refresh()
        onPauseOrDispose { }
    }

    MenuListScreenContent(
        uiState = uiState,
        onCategorySelected = viewModel::onCategorySelected,
        onNavigateToDetail = onNavigateToDetail,
        onAddMenuClick = onAddMenuClick,
        onRefresh = viewModel::refresh,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MenuListScreenContent(
    uiState: MenuListUiState,
    onCategorySelected: (String?) -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onAddMenuClick: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale200),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            MenuListHeader(
                menuCount = uiState.menuItems.size,
                onAddClick = onAddMenuClick,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Grayscale200,
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    )
                    .padding(horizontal = 20.dp),
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                CategoryTabRow(
                    categories = uiState.categories,
                    selectedCategoryCode = uiState.selectedCategoryCode,
                    onCategorySelected = onCategorySelected,
                )

                Spacer(modifier = Modifier.height(12.dp))

                MenuListLegend()

                Spacer(modifier = Modifier.height(12.dp))

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
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(
                            items = uiState.menuItems,
                            key = { it.id },
                        ) { menuItem ->
                            MenuListItem(
                                name = menuItem.name,
                                price = menuItem.sellingPrice,
                                costRatio = menuItem.costRatio,
                                marginRatio = menuItem.marginRatio,
                                marginGrade = menuItem.marginGrade,
                                onClick = { onNavigateToDetail(menuItem.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuListHeader(
    menuCount: Int,
    onAddClick: () -> Unit,
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
                text = "메뉴 ",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Grayscale900,
            )
            Text(
                text = "$menuCount",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = PrimaryBlue500,
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = "메뉴 추가",
            tint = Grayscale600,
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onAddClick),
        )
    }
}

@Composable
private fun MenuListLegend(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Canvas(modifier = Modifier.size(6.dp)) {
            drawCircle(color = Grayscale900)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "원가율",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Grayscale600,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Canvas(modifier = Modifier.size(6.dp)) {
            drawCircle(color = PrimaryBlue500)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "마진율",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Grayscale600,
        )
    }
}

