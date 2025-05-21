package io.baselines.sample.ui.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

internal val DefaultTypography
    @Composable get() = typographyFromDefaults(
        headlineLarge = TextStyle(
            fontFamily = Fonts.DEFAULT
        )
    )

internal object Fonts {

    val DEFAULT: FontFamily
        @Composable get() = FontFamily.Default
}

internal fun typographyFromDefaults(
    /* Headline */
    headlineLarge: TextStyle? = null,
    headlineMedium: TextStyle? = null,
    /* Title */
    titleLarge: TextStyle? = null,
    titleMedium: TextStyle? = null,
    titleSmall: TextStyle? = null,
    /* Body */
    bodyLarge: TextStyle? = null,
    bodyMedium: TextStyle? = null,
    bodySmall: TextStyle? = null,
    /* Label */
    labelLarge: TextStyle? = null,
    labelMedium: TextStyle? = null,
): Typography {
    val defaults = Typography()
    return Typography(
        headlineLarge = defaults.headlineLarge.merge(headlineLarge),
        headlineMedium = defaults.headlineMedium.merge(headlineMedium),
        titleLarge = defaults.titleLarge.merge(titleLarge),
        titleMedium = defaults.titleMedium.merge(titleMedium),
        titleSmall = defaults.titleSmall.merge(titleSmall),
        bodyLarge = defaults.bodyLarge.merge(bodyLarge),
        bodyMedium = defaults.bodyMedium.merge(bodyMedium),
        bodySmall = defaults.bodySmall.merge(bodySmall),
        labelLarge = defaults.labelLarge.merge(labelLarge),
        labelMedium = defaults.labelMedium.merge(labelMedium),
    )
}
