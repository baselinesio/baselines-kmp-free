package io.baselines.ui.playground.typography

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import io.baselines.sample.ui.designsystem.theme.AppTheme
import io.baselines.ui.playground.main.CollapsingSection

@Composable
fun TypographySection(
    title: String,
    expandedState: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {
    CollapsingSection(
        modifier = modifier,
        title = title,
        expandedState = expandedState,
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacings.elementMedium)
        ) {
            // Display
            Column {
                Text(
                    text = "Display Large",
                    style = AppTheme.typography.displayLarge,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                )
                Text(
                    text = "Display Medium",
                    style = AppTheme.typography.displayMedium,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                )
                Text(
                    text = "Display Small",
                    style = AppTheme.typography.displaySmall,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                )
            }

            // Headline
            Column {
                Text(
                    text = "Headline Large",
                    style = AppTheme.typography.headlineLarge,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                )
                Text(
                    text = "Headline Medium",
                    style = AppTheme.typography.headlineMedium,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                )
                Text(
                    text = "Headline Small",
                    style = AppTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                )
            }

            // Title
            Column {
                Text(
                    text = "Title Large",
                    style = AppTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                )
                Text(
                    text = "Title Medium",
                    style = AppTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                )
                Text(
                    text = "Title Small",
                    style = AppTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                )
            }

            // Label
            Column {
                Text(
                    text = "Label Large",
                    style = AppTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                )
                Text(
                    text = "Label Medium",
                    style = AppTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                )
                Text(
                    text = "Label Small",
                    style = AppTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                )
            }

            // Body
            Column {
                Text(
                    text = "Body Large",
                    style = AppTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                )
                Text(
                    text = "Body Medium",
                    style = AppTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                )
                Text(
                    text = "Body Small",
                    style = AppTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                )
            }
        }
    }
}
