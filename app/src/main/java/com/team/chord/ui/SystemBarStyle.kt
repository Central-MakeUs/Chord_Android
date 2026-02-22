package com.team.chord.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

data class SystemBarStyleSpec(
    val color: Color,
    val darkIcons: Boolean,
)

@Composable
fun ApplySystemBarStyle(style: SystemBarStyleSpec) {
    val view = LocalView.current
    if (view.isInEditMode) return

    val activity = view.context.findActivity() ?: return

    SideEffect {
        val window = activity.window
        window.statusBarColor = style.color.toArgb()
        WindowCompat.getInsetsController(window, view)?.isAppearanceLightStatusBars = style.darkIcons
    }
}

fun Color.shouldUseDarkIcons(): Boolean = luminance() > 0.5f

private tailrec fun Context.findActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
