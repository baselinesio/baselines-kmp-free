package io.baselines.sample.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import io.baselines.sample.ui.navigation.NavEvent
import io.baselines.sample.ui.navigation.Navigator
import io.baselines.toolkit.di.UiScope
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(UiScope::class)
class ComposeNavigator(
    private val navigator: Navigator,
) {

    @Composable
    fun bind(navController: NavHostController): NavEvent {
        val event by navigator.events.collectAsStateWithLifecycle(NavEvent.Undefined())
        LaunchNavigationHandler(navController, event)
        return event
    }

    @Composable
    private fun LaunchNavigationHandler(controller: NavHostController, event: NavEvent) {
        LaunchedEffect(event) {
            when (event) {
                is NavEvent.Navigate -> controller.navigate(
                    route = event.route,
                    navOptions = event.optionsBuilder?.let { optionsBuilder ->
                        navOptions {
                            with(optionsBuilder) { configure(controller.graph) }
                        }
                    }
                )

                is NavEvent.Up -> controller.navigateUp()

                is NavEvent.Back -> event.route
                    ?.let { controller.popBackStack(it, event.inclusive) }
                    ?: controller.popBackStack()

                is NavEvent.Undefined -> {
                    /* no-op */
                }
            }
        }
    }
}
