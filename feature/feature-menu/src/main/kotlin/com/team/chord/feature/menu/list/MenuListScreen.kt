package com.team.chord.feature.menu.list

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.R
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.menu.component.CategoryChipRow
import com.team.chord.feature.menu.component.MenuListItem

@Composable
fun MenuListScreen(
    onNavigateToDetail: (Long) -> Unit,
    onAddMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MenuListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MenuListScreenContent(
        uiState = uiState,
        onCategorySelected = viewModel::onCategorySelected,
        onNavigateToDetail = onNavigateToDetail,
        onAddMenuClick = onAddMenuClick,
        modifier = modifier,
    )
}

@Composable
internal fun MenuListScreenContent(
    uiState: MenuListUiState,
    onCategorySelected: (String?) -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onAddMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale200),
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

            CategoryChipRow(
                categories = uiState.categories,
                selectedCategoryCode = uiState.selectedCategoryCode,
                onCategorySelected = onCategorySelected,
            )

            Spacer(modifier = Modifier.height(16.dp))

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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable(onClick = onAddClick),
        ) {
            Text(
                text = "추가",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Grayscale600,
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "메뉴 추가",
                tint = Grayscale600,
                modifier = Modifier.size(12.dp),
            )
        }
    }
}

