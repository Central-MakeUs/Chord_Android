package com.team.chord.navigation

import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PrimaryBlue100
import com.team.chord.feature.aicoach.navigation.AI_COACH_ROUTE
import com.team.chord.feature.home.navigation.HOME_ROUTE
import com.team.chord.feature.ingredient.navigation.INGREDIENT_LIST_ROUTE
import com.team.chord.feature.menu.navigation.MENU_LIST_ROUTE
import com.team.chord.feature.setting.navigation.SETTING_ROUTE
import com.team.chord.ui.shouldUseDarkIcons
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StatusBarStyleMapperTest {
    @Test
    fun topLevelRoutes_mapToExpectedColors() {
        assertEquals(PrimaryBlue100, statusBarStyleForRoute(HOME_ROUTE).color)
        assertEquals(Grayscale200, statusBarStyleForRoute(MENU_LIST_ROUTE).color)
        assertEquals(Grayscale200, statusBarStyleForRoute(INGREDIENT_LIST_ROUTE).color)
        assertEquals(Grayscale200, statusBarStyleForRoute(AI_COACH_ROUTE).color)
        assertEquals(Grayscale200, statusBarStyleForRoute(SETTING_ROUTE).color)
    }

    @Test
    fun parameterizedRoutes_mapToExpectedColors() {
        assertEquals(Grayscale100, statusBarStyleForRoute("menu_detail/42").color)
        assertEquals(Grayscale100, statusBarStyleForRoute("ingredient_detail/7").color)
        assertEquals(
            Grayscale100,
            statusBarStyleForRoute(
                "menu_add_detail/new_menu/false?templatePrice=12000",
            ).color,
        )
        assertEquals(
            Grayscale100,
            statusBarStyleForRoute(
                "setting_webview/terms/https%3A%2F%2Fexample.com",
            ).color,
        )
    }

    @Test
    fun unknownRoute_usesFallbackColor() {
        assertEquals(Grayscale100, statusBarStyleForRoute("unknown_route").color)
        assertEquals(Grayscale100, statusBarStyleForRoute(null).color)
    }

    @Test
    fun iconStyle_isComputedByColorLuminance() {
        assertTrue(Grayscale100.shouldUseDarkIcons())
        assertFalse(Grayscale900.shouldUseDarkIcons())
        assertTrue(statusBarStyleForRoute(HOME_ROUTE).darkIcons)
    }
}
