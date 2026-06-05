package io.baselines.sample.compose.navigation

import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.baselines.sample.ui.navigation.NavEvent
import io.baselines.sample.ui.navigation.Navigator
import io.baselines.toolkit.di.UiScope

@Inject
@SingleIn(UiScope::class)
class ComposeNavigator(
    private val navigator: Navigator,
) {

    suspend fun bind(navController: NavHostController) {
        navigator.events.collect { event ->
            when (event) {
                is NavEvent.Navigate -> navController.navigate(
                    route = event.route,
                    navOptions = event.optionsBuilder?.let { optionsBuilder ->
                        navOptions {
                            with(optionsBuilder) { configure(navController.graph) }
                        }
                    }
                )

                is NavEvent.Up -> navController.navigateUp()

                is NavEvent.Back -> event.route
                    ?.let { navController.popBackStack(it, event.inclusive) }
                    ?: navController.popBackStack()

                is NavEvent.Undefined -> {
                    /* no-op */
                }
            }
        }
    }
}
