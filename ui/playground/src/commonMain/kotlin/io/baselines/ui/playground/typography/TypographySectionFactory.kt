package io.baselines.ui.playground.typography

import androidx.compose.runtime.Composable
import io.baselines.toolkit.di.UiScope
import io.baselines.ui.playground.SectionFactory
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(UiScope::class, multibinding = true)
class TypographySectionFactory : SectionFactory {

    override val name: String = "Typography"

    @Composable
    override fun Create() {
        TypographySection(
            title = name,
        )
    }
}
