package com.team.chord.feature.menu.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500

@Composable
fun CategoryTabRow(
    categories: List<Category>,
    selectedCategoryCode: String?,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Build tab list: "전체" + category names
    val tabs = buildList {
        add(null to "전체")
        categories.forEach { add(it.code to it.name) }
    }

    val selectedIndex = tabs.indexOfFirst { it.first == selectedCategoryCode }.coerceAtLeast(0)

    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier.fillMaxWidth(),
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        contentColor = PrimaryBlue500,
        indicator = { tabPositions ->
            if (selectedIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                    height = 2.dp,
                    color = PrimaryBlue500,
                )
            }
        },
        divider = {
            androidx.compose.material3.HorizontalDivider(
                thickness = 1.dp,
                color = Grayscale300,
            )
        },
    ) {
        tabs.forEachIndexed { index, (code, name) ->
            val isSelected = index == selectedIndex
            Tab(
                selected = isSelected,
                onClick = { onCategorySelected(code) },
                text = {
                    Text(
                        text = name,
                        fontFamily = PretendardFontFamily,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 14.sp,
                        color = if (isSelected) PrimaryBlue500 else Grayscale600,
                    )
                },
            )
        }
    }
}
