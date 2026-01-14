package com.virakumaro.testbookcabin.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryOrange,
    secondary = DarkBlue,
    tertiary = White,

)


@Composable
fun TestBookcabinTheme(
    isLightStatusBar: Boolean = false,
    content: @Composable () -> Unit
) {
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            WindowCompat.setDecorFitsSystemWindows(window, false)

            window.statusBarColor = Color.Transparent.toArgb()

            // Control icon color (Light vs Dark)
            // Set to true if your background is light (icons become dark)
            // Set to false if your background is dark (icons become white)
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = isLightStatusBar
        }
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}