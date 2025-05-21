package io.baselines.ui.viewmodel

import androidx.compose.runtime.Stable

/**
 * Represents the UI state that should be consumed and rendered by a view (e.g., a `@Composable` function).
 *
 * This interface is typically implemented by a feature-specific state class and is designed to work alongside
 * a corresponding [UiEvent] to support unidirectional data flow between the view and [androidx.lifecycle.ViewModel].
 *
 * ### Usage
 * Extend this interface in your feature module and define the necessary fields to represent the state of your UI.
 *
 * #### Example
 * A "Profile" screen that displays a profile image and username might use the following state:
 *
 * ```kotlin
 * @Immutable
 * data class ProfileUiState(
 *     val profileImageUrl: String,
 *     val username: String,
 *     override val eventSink: (ProfileUiEvent) -> Unit,
 * ) : UiState<ProfileUiEvent>
 * ```
 * @param E A subtype of [UiEvent] representing user interactions or one-time UI actions.
 */
@Stable
interface UiState<E : UiEvent> {

    /**
     * A callback function that allows the view to send [UiEvent]s back to the [androidx.lifecycle.ViewModel]
     * for handling.
     *
     * This is the primary way for the view layer to communicate user interactions, such as button clicks,
     * gestures, or other events.
     *
     * ### Example
     * On a "Profile" screen, when the user taps the "Logout" button, the event should be sent like this:
     *
     * ```kotlin
     * ProfileScreen(
     *     onLogoutClick = { eventSink(ProfileUiEvent.DoLogout) }
     * )
     * ```
     *
     * The [androidx.lifecycle.ViewModel] will then observe and handle this event accordingly.
     */
    val eventSink: (E) -> Unit
}
