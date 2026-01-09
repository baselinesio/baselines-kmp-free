package io.baselines.sample.ui.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

internal val AppThemeLight = lightColorScheme(
    primary = Palette.purple80,
)
internal val AppThemeDark = darkColorScheme(
    primary = Palette.purple40
)

internal object Palette {
    val purple40 = Color(red = 208, green = 188, blue = 255)
    val purple80 = Color(red = 103, green = 80, blue = 164)
    val black20 = Color(0x332E2A27)
}
