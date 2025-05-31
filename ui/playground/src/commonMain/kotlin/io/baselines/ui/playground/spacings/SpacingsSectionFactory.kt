package io.baselines.ui.playground.spacings

import androidx.compose.runtime.Composable
import io.baselines.ui.playground.Res
import io.baselines.ui.playground.SectionFactory
import io.baselines.ui.playground.playground_sectionTitle_spacings
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, multibinding = true)
class SpacingsSectionFactory : SectionFactory {

    override val titleRes: StringResource = Res.string.playground_sectionTitle_spacings

    @Composable
    override fun Create() {
        SpacingsSection(stringResource(titleRes))
    }
}
