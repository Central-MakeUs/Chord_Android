package com.team.chord.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.R as CoreUiR
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500

enum class TopLevelDestination(
    val route: String,
    val label: String,
    val selectedIconResId: Int,
    val unselectedIconResId: Int,
) {
    HOME(
        route = "home",
        label = "홈",
        selectedIconResId = CoreUiR.drawable.ic_home_select,
        unselectedIconResId = CoreUiR.drawable.ic_home_un_select,
    ),
    MENU(
        route = "menu_list",
        label = "메뉴",
        selectedIconResId = CoreUiR.drawable.ic_menu_select,
        unselectedIconResId = CoreUiR.drawable.ic_menu_un_select,
    ),
    INGREDIENT(
        route = "ingredient",
        label = "재료",
        selectedIconResId = CoreUiR.drawable.ic_ingredients_select,
        unselectedIconResId = CoreUiR.drawable.ic_ingredients_un_select,
    ),
    AI_COACH(
        route = "ai_coach",
        label = "AI코치",
        selectedIconResId = CoreUiR.drawable.ic_coach_select,
        unselectedIconResId = CoreUiR.drawable.ic_coach_un_select,
    ),
}

@Composable
fun ChordBottomNavBar(
    currentDestination: String?,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Grayscale100,
        tonalElevation = 0.dp,
    ) {
        TopLevelDestination.entries.forEach { destination ->
            val selected = currentDestination == destination.route

            ChordNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        painter =
                            painterResource(
                                id = if (selected) destination.selectedIconResId else destination.unselectedIconResId,
                            ),
                        contentDescription = destination.label,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified,
                    )
                },
                label = destination.label,
            )
        }
    }
}

@Composable
private fun RowScope.ChordNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: String,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = icon,
        label = {
            Text(
                text = label,
                fontFamily = PretendardFontFamily,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 12.sp,
            )
        },
        colors =
            NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue500,
                selectedTextColor = PrimaryBlue500,
                unselectedIconColor = Grayscale500,
                unselectedTextColor = Grayscale500,
                indicatorColor = Grayscale100,
            ),
    )
}
