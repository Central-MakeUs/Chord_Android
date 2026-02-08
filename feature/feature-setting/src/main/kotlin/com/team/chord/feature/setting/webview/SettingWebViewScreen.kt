package com.team.chord.feature.setting.webview

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.team.chord.core.ui.component.ChordTopAppBar

@Composable
fun SettingWebViewScreen(
    title: String,
    url: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        ChordTopAppBar(
            title = title,
            onBackClick = onNavigateBack,
        )

        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    webViewClient = WebViewClient()
                    loadUrl(url)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingWebViewScreenPreview() {
    SettingWebViewScreen(
        title = "이용약관",
        url = "https://example.com",
        onNavigateBack = {},
    )
}
