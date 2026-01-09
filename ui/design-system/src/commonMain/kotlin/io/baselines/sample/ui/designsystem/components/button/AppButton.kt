package io.baselines.sample.ui.designsystem.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.baselines.sample.ui.designsystem.theme.AppTheme

@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    startIcon: ImageVector? = null,
    startIconTint: Color? = null,
    endIcon: ImageVector? = null,
    endIconTint: Color? = null,
    textStyle: TextStyle = AppTheme.typography.labelMedium,
    shape: Shape = AppTheme.shapes.roundedMedium,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    enabled: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    contentPadding: Dp = AppTheme.spacings.elementMedium,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    shadow: Shadow = AppTheme.shadows.none,
) {
    Button(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .dropShadow(
                shape = shape,
                shadow = shadow,
            ),
        onClick = onClick,
        contentPadding = PaddingValues(contentPadding),
        shape = shape,
        elevation = elevation,
        colors = colors,
        enabled = enabled,
        border = border
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                AppTheme.spacings.elementMedium,
                horizontalAlignment
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            startIcon?.let { vector ->
                Icon(
                    imageVector = vector,
                    tint = startIconTint ?: LocalContentColor.current,
                    contentDescription = null
                )
            }
            label?.let {
                Text(
                    text = label,
                    style = textStyle,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            endIcon?.let { vector ->
                Icon(
                    imageVector = vector,
                    tint = endIconTint ?: LocalContentColor.current,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAppButton() {
    AppTheme {
        AppButton(
            modifier = Modifier.padding(12.dp),
            onClick = { /* no-op */ },
            label = "Button",
            colors = ButtonDefaults.buttonColors()
        )
    }
}


@Preview
@Composable
private fun PreviewTonalAppButton() {
    AppTheme {
        AppButton(
            modifier = Modifier.padding(12.dp),
            onClick = { /* no-op */ },
            label = "Button",
            colors = ButtonDefaults.filledTonalButtonColors()
        )
    }
}

@Preview
@Composable
private fun PreviewAppButtonDisabled() {
    AppTheme {
        AppButton(
            modifier = Modifier.padding(12.dp),
            onClick = { /* no-op */ },
            label = "Disabled Button",
            enabled = false
        )
    }
}
