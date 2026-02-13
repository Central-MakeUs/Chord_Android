package com.team.chord.feature.setting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.team.chord.feature.setting.SettingScreen
import com.team.chord.feature.setting.faq.FaqScreen
import com.team.chord.feature.setting.storeedit.StoreEditScreen
import com.team.chord.feature.setting.webview.SettingWebViewScreen
import com.team.chord.feature.setting.withdraw.WithdrawScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

const val SETTING_ROUTE = "setting"
const val STORE_EDIT_ROUTE = "setting/store_edit"
const val FAQ_ROUTE = "setting/faq"
const val WITHDRAW_ROUTE = "setting/withdraw"
const val SETTING_WEBVIEW_ROUTE = "setting_webview/{title}/{encodedUrl}"

fun NavController.navigateToSetting(navOptions: NavOptions? = null) {
    navigate(SETTING_ROUTE, navOptions)
}

fun NavController.navigateToStoreEdit(navOptions: NavOptions? = null) {
    navigate(STORE_EDIT_ROUTE, navOptions)
}

fun NavController.navigateToFaq(navOptions: NavOptions? = null) {
    navigate(FAQ_ROUTE, navOptions)
}

fun NavController.navigateToWithdraw() {
    navigate(WITHDRAW_ROUTE)
}

fun NavController.navigateToSettingWebView(title: String, url: String) {
    val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
    navigate("setting_webview/$title/$encodedUrl")
}

fun NavGraphBuilder.settingScreen(
    onNavigateBack: () -> Unit,
    onNavigateToStoreEdit: () -> Unit,
    onNavigateToFaq: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onLogout: () -> Unit,
) {
    composable(route = SETTING_ROUTE) {
        SettingScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToStoreEdit = onNavigateToStoreEdit,
            onNavigateToFaq = onNavigateToFaq,
            onNavigateToTerms = onNavigateToTerms,
            onNavigateToWithdraw = onNavigateToWithdraw,
            onLogout = onLogout,
        )
    }
}

fun NavGraphBuilder.storeEditScreen(
    onNavigateBack: () -> Unit,
    onStoreEditComplete: () -> Unit,
) {
    composable(route = STORE_EDIT_ROUTE) {
        StoreEditScreen(
            onNavigateBack = onNavigateBack,
            onStoreEditComplete = onStoreEditComplete,
        )
    }
}

fun NavGraphBuilder.faqScreen(
    onNavigateBack: () -> Unit,
) {
    composable(route = FAQ_ROUTE) {
        FaqScreen(onNavigateBack = onNavigateBack)
    }
}

fun NavGraphBuilder.withdrawScreen(
    onNavigateBack: () -> Unit,
    onWithdrawSuccess: () -> Unit,
) {
    composable(route = WITHDRAW_ROUTE) {
        WithdrawScreen(
            onNavigateBack = onNavigateBack,
            onWithdrawSuccess = onWithdrawSuccess,
        )
    }
}

fun NavGraphBuilder.settingWebViewScreen(
    onNavigateBack: () -> Unit,
) {
    composable(
        route = SETTING_WEBVIEW_ROUTE,
        arguments = listOf(
            navArgument("title") { type = NavType.StringType },
            navArgument("encodedUrl") { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val title = backStackEntry.arguments?.getString("title") ?: ""
        val encodedUrl = backStackEntry.arguments?.getString("encodedUrl") ?: ""
        val url = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
        SettingWebViewScreen(
            title = title,
            url = url,
            onNavigateBack = onNavigateBack,
        )
    }
}
