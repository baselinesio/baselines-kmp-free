package io.baselines.ui.playground.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.style.TextAlign
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import io.baselines.sample.ui.designsystem.theme.AppTheme
import io.baselines.toolkit.di.UiScope

@Inject
@ContributesIntoSet(UiScope::class)
class ShadowsSection : PlaygroundSection {

    override val name: String = "Shadows"

    override val uiFactory: PlaygroundSection.UiFactory = PlaygroundSection.UiFactory { Shadows() }
}

@Composable
private fun Shadows() {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacings.elementLarge)
    ) {
        ShadowItem(
            label = "No shadow",
            shadow = AppTheme.shadows.none,
        )
        ShadowItem(
            label = "Level 1",
            shadow = AppTheme.shadows.level1,
        )
        ShadowItem(
            label = "Level 2",
            shadow = AppTheme.shadows.level2,
        )
        ShadowItem(
            label = "Level 3",
            shadow = AppTheme.shadows.level3,
        )
        ShadowItem(
            label = "Level 4",
            shadow = AppTheme.shadows.level4,
        )
    }
}

@Composable
private fun ShadowItem(
    label: String,
    shadow: Shadow,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(
                shape = AppTheme.shapes.roundedMedium,
                shadow = shadow,
            )
            .background(
                color = AppTheme.colorScheme.surfaceContainerHighest,
                shape = AppTheme.shapes.roundedMedium,
            )
            .padding(AppTheme.spacings.elementMedium),
        style = AppTheme.typography.bodyMedium,
        text = label,
        textAlign = TextAlign.Center,
    )
}
