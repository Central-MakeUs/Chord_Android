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
    val fixedCategoryCodes = resolveFixedCategoryCodes(categories)

    val tabs = listOf(
        null to "전체",
        fixedCategoryCodes.beverageCode to "음료",
        fixedCategoryCodes.dessertCode to "디저트",
        fixedCategoryCodes.foodCode to "푸드",
    )

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

private data class FixedCategoryCodes(
    val beverageCode: String,
    val dessertCode: String,
    val foodCode: String,
)

private fun resolveFixedCategoryCodes(categories: List<Category>): FixedCategoryCodes {
    val sortedCategories = categories.sortedBy { it.displayOrder }

    val beverageCode = categories.firstOrNull { isBeverageCategory(it) }?.code
        ?: sortedCategories.getOrNull(0)?.code
        ?: "BEVERAGE"
    val dessertCode = categories.firstOrNull { isDessertCategory(it) }?.code
        ?: sortedCategories.getOrNull(1)?.code
        ?: "DESSERT"
    val foodCode = categories.firstOrNull { isFoodCategory(it) }?.code
        ?: sortedCategories.getOrNull(2)?.code
        ?: "FOOD"

    return FixedCategoryCodes(
        beverageCode = beverageCode,
        dessertCode = dessertCode,
        foodCode = foodCode,
    )
}

private fun isBeverageCategory(category: Category): Boolean {
    val code = category.code.trim().uppercase()
    val name = category.name.trim().uppercase()
    return code == "BEVERAGE" ||
        code == "DRINK" ||
        code == "DRINKS" ||
        name == "음료" ||
        name == "BEVERAGE" ||
        name == "DRINK"
}

private fun isDessertCategory(category: Category): Boolean {
    val code = category.code.trim().uppercase()
    val name = category.name.trim().uppercase()
    return code == "DESSERT" ||
        code == "DESSERTS" ||
        name == "디저트" ||
        name == "DESSERT"
}

private fun isFoodCategory(category: Category): Boolean {
    val code = category.code.trim().uppercase()
    val name = category.name.trim().uppercase()
    return code == "FOOD" ||
        code == "FOODS" ||
        name == "푸드" ||
        name == "FOOD"
}
