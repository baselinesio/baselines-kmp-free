package io.baselines.ui.playground.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.TextStyle
import io.baselines.sample.ui.designsystem.theme.AppTheme

@Composable
fun CollapsingSection(
    title: String,
    expanded: Boolean,
    onExpandedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = AppTheme.typography.titleMedium,
    collapsedContent: @Composable (PaddingValues) -> Unit = { },
    expandedContent: @Composable (PaddingValues) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = AppTheme.spacings.layoutMedium)
    ) {
        Row(
            modifier = Modifier
                .clip(AppTheme.shapes.roundedMedium)
                .clickable(onClick = { onExpandedChanged(!expanded) })
                .then(modifier)
                .padding(AppTheme.spacings.layoutMedium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                AppTheme.spacings.elementLarge
            )
        ) {
            Text(
                modifier = Modifier.weight(1F),
                text = title,
                style = titleStyle,
                color = AppTheme.colorScheme.onSurface
            )
            val rotation = if (expanded) 180F else 0F
            Text(
                modifier = Modifier
                    .rotate(rotation),
                text = "⬇\uFE0F",
            )
        }
        val contentPaddings = PaddingValues(
            start = AppTheme.spacings.layoutMedium,
            end = AppTheme.spacings.layoutMedium,
            bottom = AppTheme.spacings.layoutMedium,
        )
        AnimatedContent(
            targetState = expanded,
            label = "animated_collapsing_review_section",
        ) { targetState ->
            if (targetState) {
                expandedContent(contentPaddings)
            } else {
                collapsedContent(contentPaddings)
            }
        }
    }
}
