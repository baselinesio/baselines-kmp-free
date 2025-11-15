package io.baselines.sample.ui.designsystem.loading

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet

/**
 * Default implementation of [LoadingController].
 *
 * This class maintains an internal loading counter to support nested loading operations.
 * The `loading` state is only cleared when all operations wrapped by [withLoadingIndication] have completed.
 */
class LoadingControllerImpl : LoadingController {

    private val loadingCounter = MutableStateFlow(0)
    private val _loadingStateFlow = MutableStateFlow<LoadingStateUm?>(null)

    override val loading: StateFlow<LoadingStateUm?> = _loadingStateFlow.asStateFlow()

    override suspend fun <T> withLoadingIndication(
        loadingProgress: LoadingStateUm,
        block: suspend () -> T
    ): T {
        loadingCounter.update { it.inc() }
        _loadingStateFlow.update { loadingProgress }
        return block.invoke().also {
            val counter = loadingCounter.updateAndGet {
                if (it > 0) it.dec() else it
            }
            if (counter == 0) _loadingStateFlow.update { null }
        }
    }
}
