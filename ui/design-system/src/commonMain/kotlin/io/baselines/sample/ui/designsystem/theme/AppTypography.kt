package io.baselines.sample.ui.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.baselines.sample.ui.designsystem.Res
import io.baselines.sample.ui.designsystem.google_sans_flex_variable
import org.jetbrains.compose.resources.Font

internal val DefaultTypography
    @Composable get() = typographyFromDefaults(
        /* Headline */
        headlineLarge = TextStyle(
            fontFamily = Fonts.GOOGLE_SANS_FLEX,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 40.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = Fonts.GOOGLE_SANS_FLEX,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 36.sp
        ),
        /* Title */
        titleLarge = TextStyle(
            fontFamily = Fonts.GOOGLE_SANS_FLEX,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 32.sp
        ),
        titleMedium = TextStyle(
            fontFamily = Fonts.GOOGLE_SANS_FLEX,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            lineHeight = 26.sp
        ),
        titleSmall = TextStyle(
            fontFamily = Fonts.GOOGLE_SANS_FLEX,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 22.sp
        ),
        /* Body */
        bodyLarge = TextStyle(
            fontFamily = Fonts.GOOGLE_SANS_FLEX,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            lineHeight = 26.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = Fonts.GOOGLE_SANS_FLEX,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        bodySmall = TextStyle(
            fontFamily = Fonts.GOOGLE_SANS_FLEX,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        /* Label */
        labelLarge = TextStyle(
            fontFamily = Fonts.GOOGLE_SANS_FLEX,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        labelMedium = TextStyle(
            fontFamily = Fonts.GOOGLE_SANS_FLEX,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    )

internal object Fonts {

    val GOOGLE_SANS_FLEX: FontFamily
        @Composable get() = FontFamily(
            Font(Res.font.google_sans_flex_variable, FontWeight.Normal),
            Font(Res.font.google_sans_flex_variable, FontWeight.Medium),
            Font(Res.font.google_sans_flex_variable, FontWeight.SemiBold),
            Font(Res.font.google_sans_flex_variable, FontWeight.Bold)
        )
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
