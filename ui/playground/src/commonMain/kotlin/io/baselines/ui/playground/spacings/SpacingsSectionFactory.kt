package io.baselines.ui.playground.spacings

import androidx.compose.runtime.Composable
import io.baselines.toolkit.di.UiScope
import io.baselines.ui.playground.SectionFactory
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(UiScope::class, multibinding = true)
class SpacingsSectionFactory : SectionFactory {

    override val name: String = "Spacings"

    @Composable
    override fun Create() {
        SpacingsSection(
            title = name,
        )
    }
}
