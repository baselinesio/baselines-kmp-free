package io.baselines.ui.playground.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.baselines.sample.ui.designsystem.theme.AppTheme
import io.baselines.toolkit.di.UiScope
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(UiScope::class, multibinding = true)
class SpacingsSection : PlaygroundSection {

    override val name: String = "Spacings"

    override val uiFactory: PlaygroundSection.UiFactory = PlaygroundSection.UiFactory { Spacings() }
}

@Composable
private fun Spacings() {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacings.elementMedium)) {
        // Element
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.5F),
                text = "Element small",
            )
            Box(
                modifier = Modifier
                    .height(12.dp)
                    .width(AppTheme.spacings.elementSmall)
                    .background(Color.Red)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.5F),
                text = "Element medium",
            )
            Box(
                modifier = Modifier
                    .height(12.dp)
                    .width(AppTheme.spacings.elementMedium)
                    .background(Color.Red)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.5F),
                text = "Element large",
            )
            Box(
                modifier = Modifier
                    .height(12.dp)
                    .width(AppTheme.spacings.elementLarge)
                    .background(Color.Red)
            )
        }

        // Layout
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.5F),
                text = "Layout small",
            )
            Box(
                modifier = Modifier
                    .height(12.dp)
                    .width(AppTheme.spacings.layoutSmall)
                    .background(Color.Red)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.5F),
                text = "Layout medium",
            )
            Box(
                modifier = Modifier
                    .height(12.dp)
                    .width(AppTheme.spacings.layoutMedium)
                    .background(Color.Red)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.5F),
                text = "Layout large",
            )
            Box(
                modifier = Modifier
                    .height(12.dp)
                    .width(AppTheme.spacings.layoutLarge)
                    .background(Color.Red)
            )
        }
    }
}
