package io.baselines.ui.playground.spacings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import io.baselines.toolkit.di.UiScope
import io.baselines.ui.playground.Res
import io.baselines.ui.playground.SectionFactory
import io.baselines.ui.playground.playground_sectionTitle_spacings
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(UiScope::class)
@ContributesBinding(UiScope::class, multibinding = true)
class SpacingsSectionFactory : SectionFactory {

    override val titleRes: StringResource = Res.string.playground_sectionTitle_spacings

    override val expandedState: MutableState<Boolean> = mutableStateOf(false)

    @Composable
    override fun Create(expandedState: MutableState<Boolean>) {
        SpacingsSection(
            title = stringResource(titleRes),
            expandedState = expandedState,
        )
    }
}
