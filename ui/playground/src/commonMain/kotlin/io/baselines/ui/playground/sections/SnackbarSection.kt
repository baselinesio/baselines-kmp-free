package io.baselines.ui.playground.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import io.baselines.sample.ui.designsystem.components.button.AppButton
import io.baselines.sample.ui.designsystem.components.snackbar.AppSnackbarManager
import io.baselines.sample.ui.designsystem.components.snackbar.showFailure
import io.baselines.sample.ui.designsystem.components.snackbar.showSuccess
import io.baselines.sample.ui.designsystem.theme.AppTheme
import io.baselines.toolkit.di.UiScope

@Inject
@ContributesIntoSet(UiScope::class)
class SnackbarSection(
    private val snackbarManager: AppSnackbarManager,
) : PlaygroundSection {

    override val name: String = "Snackbar"

    override val uiFactory: PlaygroundSection.UiFactory = PlaygroundSection.UiFactory {
        Snackbar(
            onSuccessClicked = {
                snackbarManager.showSuccess { "Snackbar success example" }
            },
            onFailureClicked = {
                snackbarManager.showFailure { "Snackbar failure example" }
            },
        )
    }
}

@Composable
private fun Snackbar(
    onSuccessClicked: () -> Unit,
    onFailureClicked: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacings.elementMedium)
    ) {
        AppButton(
            modifier = Modifier.weight(1F),
            label = "Success",
            onClick = onSuccessClicked,
        )
        AppButton(
            modifier = Modifier.weight(1F),
            label = "Failure",
            colors = ButtonDefaults.buttonColors(
                containerColor = AppTheme.colorScheme.error,
                contentColor = AppTheme.colorScheme.onError,
            ),
            onClick = onFailureClicked,
        )
    }
}
