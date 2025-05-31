package io.baselines.ui.playground.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.TextStyle
import io.baselines.sample.ui.designsystem.theme.AppTheme

@Composable
fun CollapsingSection(
    title: String,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = AppTheme.typography.titleLarge,
    collapsedContent: @Composable (PaddingValues) -> Unit = { },
    expandedContent: @Composable (PaddingValues) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(horizontal = AppTheme.spacings.layoutMedium)
    ) {
        Row(
            modifier = Modifier
                .clip(AppTheme.shapes.roundMedium)
                .clickable(onClick = { expanded = !expanded })
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
            val rotation: Float by animateFloatAsState(
                targetValue = if (expanded) 180F else 0F,
                label = "during_sleep_toggle"
            )
            Text(
                modifier = Modifier
                    .rotate(rotation),
                text = "V",
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
