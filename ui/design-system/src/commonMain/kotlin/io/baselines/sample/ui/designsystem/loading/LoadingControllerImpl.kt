package io.baselines.sample.ui.designsystem.loading

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

/**
 * Default implementation of [LoadingController].
 *
 * This class maintains an internal loading counter to support nested loading operations.
 * The `loading` state is only cleared when all operations wrapped by [withLoadingIndication] have completed.
 */
class LoadingControllerImpl : LoadingController {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val state = MutableStateFlow(State())

    override val loading: StateFlow<LoadingStateUm?> = state.map { it.loading }
        .stateIn(
            scope = scope,
            SharingStarted.Eagerly,
            null
        )

    override suspend fun <T> withLoadingIndication(
        loading: LoadingStateUm,
        block: suspend () -> T
    ): T {
        state.update { it.copy(counter = it.counter.inc(), loading = loading) }
        return try {
            block()
        } finally {
            state.update {
                val updatedCounter = (it.counter.dec()).coerceAtLeast(0)
                it.copy(
                    counter = updatedCounter,
                    loading = if (updatedCounter == 0) null else it.loading
                )
            }
        }
    }

    private data class State(
        val counter: Int = 0,
        val loading: LoadingStateUm? = null
    )
}
