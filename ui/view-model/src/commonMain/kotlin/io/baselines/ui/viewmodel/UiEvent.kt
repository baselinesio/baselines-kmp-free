package io.baselines.ui.viewmodel

/**
 * Interface for UI events that are intended to be handled by a [androidx.lifecycle.ViewModel].
 *
 * This interface represents one-time actions initiated by the user or the UI, such as button clicks,
 * gestures, or other interactive events. It pairs well with a corresponding [UiState] to facilitate
 * unidirectional data flow in your architecture.
 *
 * You can start with an empty interface and expand it as your feature requires event handling.
 *
 * ### Example
 * Suppose your screen includes a "Logout" button. When the user taps it, an event should be triggered
 * and handled by the ViewModel. Here's how you might define such an event:
 *
 * ```kotlin
 * sealed interface ProfileUiEvent : UiEvent {
 *     data object DoLogout : ProfileUiEvent
 * }
 * ```
 *
 * This event can then be observed and handled inside the ViewModel to perform the logout logic.
 */
interface UiEvent
