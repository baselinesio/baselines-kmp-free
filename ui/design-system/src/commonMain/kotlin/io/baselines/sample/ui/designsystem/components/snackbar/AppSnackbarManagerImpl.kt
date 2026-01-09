package io.baselines.sample.ui.designsystem.components.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.baselines.sample.ui.designsystem.components.snackbar.AppSnackbarManager.AppSnackbar
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

@Inject
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class AppSnackbarManagerImpl : AppSnackbarManager {

    private val snackbarEventFlow = MutableSharedFlow<AppSnackbar>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override fun show(snackbar: AppSnackbar): Boolean {
        return snackbarEventFlow.tryEmit(snackbar)
    }

    @Composable
    override fun bind(hostState: SnackbarHostState): AppSnackbar? {
        val snackbarEvent by snackbarEventFlow.collectAsStateWithLifecycle(null)
        LaunchSnackbarHandler(hostState, snackbarEvent)
        return hostState.currentSnackbarData?.visuals as? AppSnackbar
    }

    @Composable
    private fun LaunchSnackbarHandler(hostState: SnackbarHostState, snackbarEvent: AppSnackbar?) {
        LaunchedEffect(snackbarEvent) {
            when (snackbarEvent) {
                is AppSnackbar.Failure -> hostState.showSnackbar(snackbarEvent)

                is AppSnackbar.Success -> hostState.showSnackbar(snackbarEvent)

                null -> {
                    /* no-op */
                }
            }
        }
    }
}
