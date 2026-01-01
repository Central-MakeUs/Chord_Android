package com.team.chord.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.R
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500

enum class TopLevelDestination(
    val route: String,
    val label: String,
    val selectedIcon: @Composable () -> ImageVector,
    val unselectedIcon: @Composable () -> ImageVector,
) {
    HOME(
        route = "home",
        label = "홈",
        selectedIcon = { Icons.Filled.Home },
        unselectedIcon = { Icons.Outlined.Home },
    ),
    MENU(
        route = "menu_list",
        label = "메뉴",
        selectedIcon = { ImageVector.vectorResource(R.drawable.ic_menu_filled) },
        unselectedIcon = { ImageVector.vectorResource(R.drawable.ic_menu_outlined) },
    ),
    INGREDIENT(
        route = "ingredient",
        label = "재료",
        selectedIcon = { Icons.Filled.ShoppingCart },
        unselectedIcon = { Icons.Outlined.ShoppingCart },
    ),
    AI_COACH(
        route = "ai_coach",
        label = "AI코치",
        selectedIcon = { Icons.Filled.Star },
        unselectedIcon = { Icons.Outlined.Star },
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
                        imageVector = if (selected) destination.selectedIcon() else destination.unselectedIcon(),
                        contentDescription = destination.label,
                        modifier = Modifier.size(24.dp),
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
