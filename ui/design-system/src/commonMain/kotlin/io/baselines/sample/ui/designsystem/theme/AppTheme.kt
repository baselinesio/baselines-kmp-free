package io.baselines.sample.ui.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable

/**
 * The main theme wrapper for the application, designed for use in Compose-based UI on all platforms.
 *
 * [AppTheme] sets up your custom design system, including:
 * - Color scheme (light/dark)
 * - Typography
 * - Shapes
 * - Custom layout spacings
 *
 * It integrates with [MaterialTheme] while extending it via [CompositionLocalProvider] to offer project-specific
 * theming extensions like [AppSpacings] and [AppShapes].
 *
 * ### Usage
 * Wrap your root screen or app entry point:
 * ```kotlin
 * AppTheme {
 *     AppContent()
 * }
 * ```
 *
 * @param darkTheme Whether to use the dark color scheme. Defaults to system setting.
 * @param content The Composable content to be styled.
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val spacings = DefaultSpacings
    val typography = DefaultTypography
    val shapes = DefaultShapes
    val colorScheme = if (darkTheme) AppThemeDark else AppThemeLight
    val shadows = AppLightSchemeShadows
    CompositionLocalProvider(
        LocalSpacings provides spacings,
        LocalShapes provides shapes,
        LocalShadows provides shadows,
    ) {
        MaterialTheme(
            content = content,
            typography = typography,
            colorScheme = colorScheme,
        )
    }
}

/**
 * Access point for theme values used throughout the app's UI.
 *
 * This object provides convenient access to theming values like colors, typography, spacing, and shapes.
 * It combines values from [MaterialTheme] and app-specific extensions provided via [CompositionLocalProvider].
 *
 * ### Example
 * ```kotlin
 * Text(
 *     text = "Hello",
 *     style = AppTheme.typography.bodyLarge,
 *     modifier = Modifier.padding(AppTheme.spacings.medium)
 * )
 * ```
 */
@Immutable
object AppTheme {

    /**
     * Retrieves the current [AppSpacings] instance from the composition.
     *
     * Defines app-wide spacing values (e.g., small, medium, large) to ensure consistent padding/margin usage.
     */
    val spacings: AppSpacings
        @Composable
        get() = LocalSpacings.current

    /**
     * Retrieves the current [ColorScheme] defined by [MaterialTheme].
     *
     * Automatically switches between [AppThemeLight] and [AppThemeDark] based on [AppTheme]'s config.
     */
    val colorScheme: ColorScheme
        @Composable
        get() = MaterialTheme.colorScheme

    /**
     * Retrieves the current [Typography] instance from the composition.
     *
     * Defines app-wide typography (e.g., bodyLarge, titleMedium) to ensure consistent styling usage.
     */
    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    /**
     * Retrieves the current [AppShapes] instance from the composition.
     *
     * Defines the shape system used across components (e.g., rounded corners, cut corners).
     */
    val shapes: AppShapes
        @Composable
        get() = LocalShapes.current

    /**
     * Retrieves the current [AppShadows] instance from the composition.
     *
     * Defines the shadow system used across components (e.g., color, spread, radius).
     */
    val shadows: AppShadows
        @Composable
        get() = LocalShadows.current
}
