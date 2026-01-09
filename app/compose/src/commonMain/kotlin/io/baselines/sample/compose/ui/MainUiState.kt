package io.baselines.sample.compose.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Immutable
import androidx.navigation.NavHostController
import io.baselines.sample.domain.api.NavRoute
import io.baselines.sample.ui.navigation.NavGraphEntry
import io.baselines.ui.viewmodel.UiState
import kotlinx.collections.immutable.ImmutableSet

@Immutable
data class MainUiState(
    val navState: NavStateUm?,
    val snackbarHostState: SnackbarHostState,
    override val eventSink: (MainUiEvent) -> Unit
) : UiState<MainUiEvent> {

    @Immutable
    data class NavStateUm(
        val controller: NavHostController,
        val navGraph: ImmutableSet<NavGraphEntry>,
        val startRoute: NavRoute,
    )
}
