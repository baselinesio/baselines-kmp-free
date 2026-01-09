package io.baselines.sample.ui.designsystem.components.snackbar

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.baselines.sample.ui.designsystem.Res
import io.baselines.sample.ui.designsystem.components.button.AppButton
import io.baselines.sample.ui.designsystem.icon_check_mono_24
import io.baselines.sample.ui.designsystem.icon_close_small_mono_24
import io.baselines.sample.ui.designsystem.icon_error_mono_24
import io.baselines.sample.ui.designsystem.theme.AppTheme
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ComposeSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onGloballyPositioned: (LayoutCoordinates) -> Unit = {},
) {
    SnackbarHost(
        modifier = modifier,
        hostState = hostState
    ) {
        val snackbar = it.visuals as AppSnackbarManager.AppSnackbar
        val dismissAction = remember(snackbar) {
            { it.dismiss() }.takeIf { snackbar.withDismissAction }
        }
        when (snackbar) {
            is AppSnackbarManager.AppSnackbar.Success -> SnackbarSuccess(
                onDismissClick = dismissAction,
                data = snackbar,
                onGloballyPositioned = onGloballyPositioned,
            )

            is AppSnackbarManager.AppSnackbar.Failure -> SnackbarFailure(
                onDismissClick = dismissAction,
                data = snackbar,
                onGloballyPositioned = onGloballyPositioned,
            )
        }
    }
}

@Composable
private fun SnackbarSuccess(
    data: AppSnackbarManager.AppSnackbar.Success,
    modifier: Modifier = Modifier,
    onGloballyPositioned: (LayoutCoordinates) -> Unit = {},
    onDismissClick: (() -> Unit)? = null,
) {
    AppSnackbar(
        modifier = modifier,
        onGloballyPositioned = onGloballyPositioned,
        data = data,
        icon = vectorResource(Res.drawable.icon_check_mono_24),
        containerColor = AppTheme.colorScheme.primaryContainer,
        contentColor = AppTheme.colorScheme.onPrimaryContainer,
        onDismissClick = onDismissClick,
    )
}

@Composable
private fun SnackbarFailure(
    data: AppSnackbarManager.AppSnackbar.Failure,
    modifier: Modifier = Modifier,
    onGloballyPositioned: (LayoutCoordinates) -> Unit = {},
    onDismissClick: (() -> Unit)? = null,
) {
    AppSnackbar(
        modifier = modifier,
        onGloballyPositioned = onGloballyPositioned,
        data = data,
        icon = vectorResource(Res.drawable.icon_error_mono_24),
        containerColor = AppTheme.colorScheme.errorContainer,
        contentColor = AppTheme.colorScheme.onErrorContainer,
        onDismissClick = onDismissClick,
    )
}

@Composable
fun AppSnackbar(
    data: AppSnackbarManager.AppSnackbar,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    onDismissClick: (() -> Unit)?,
    onGloballyPositioned: (LayoutCoordinates) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .systemBarsPadding()
            .padding(horizontal = AppTheme.spacings.layoutLarge)
            .clip(AppTheme.shapes.roundedSmall)
            .background(containerColor)
    ) {
        if (data.duration != SnackbarDuration.Indefinite) {
            var snackbarProgress by remember { mutableFloatStateOf(1F) }
            val animatedProgress by animateFloatAsState(
                targetValue = snackbarProgress,
                animationSpec = tween(
                    durationMillis = data.duration.toMillis(data).toInt(),
                    easing = LinearEasing
                ),
            )
            // Trigger the animation from 1F to 0F only once
            LaunchedEffect(Unit) {
                snackbarProgress = 0f
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = animatedProgress)
                    .fillMaxHeight()
                    .clip(AppTheme.shapes.roundedSmall)
                    .background(contentColor.copy(alpha = 0.05f)),

                )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .onGloballyPositioned(onGloballyPositioned),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacings.elementMedium)
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = AppTheme.spacings.elementLarge),
                imageVector = icon,
                tint = contentColor,
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .weight(1F)
                    .padding(vertical = AppTheme.spacings.elementLarge),
                text = data.message,
                style = AppTheme.typography.labelMedium,
                color = contentColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            onDismissClick?.let {
                AppButton(
                    modifier = Modifier.fillMaxHeight(),
                    onClick = onDismissClick,
                    shape = AppTheme.shapes.roundedSmall,
                    startIcon = vectorResource(Res.drawable.icon_close_small_mono_24),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = Color.Transparent,
                        contentColor = contentColor,
                    ),
                )
            }
        }
    }
}

// copy of the internal Compose function
@Composable
private fun SnackbarDuration.toMillis(
    snackbar: AppSnackbarManager.AppSnackbar
): Long {
    val accessibilityManager = LocalAccessibilityManager.current
    val original =
        when (this) {
            SnackbarDuration.Indefinite -> Long.MAX_VALUE
            SnackbarDuration.Long -> 10000L
            SnackbarDuration.Short -> 4000L
        }
    if (accessibilityManager == null) {
        return original
    }
    return accessibilityManager.calculateRecommendedTimeoutMillis(
        original,
        containsIcons = true,
        containsText = true,
        containsControls = snackbar.actionLabel != null,
    )
}

@Preview
@Composable
private fun PreviewSnackbarSuccess() {
    AppTheme {
        SnackbarSuccess(
            modifier = Modifier.padding(12.dp),
            data = AppSnackbarManager.AppSnackbar.Success(
                message = "Data sync has been completed"
            ),
        )
    }
}

@Preview
@Composable
private fun PreviewSnackbarFailure() {
    AppTheme {
        SnackbarFailure(
            modifier = Modifier.padding(12.dp),
            data = AppSnackbarManager.AppSnackbar.Failure(
                message = "Couldn't sync the data"
            ),
            onDismissClick = {
                /* no-op */
            }
        )
    }
}
