@file:OptIn(ExperimentalUuidApi::class)

package io.baselines.sample.ui.designsystem.components.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface AppSnackbarManager {

    @Composable
    fun bind(hostState: SnackbarHostState): AppSnackbar?

    fun show(snackbar: AppSnackbar): Boolean

    @Immutable
    sealed class AppSnackbar : SnackbarVisuals {

        abstract val id: String

        @Immutable
        data class Success(
            override val id: String = Uuid.random().toString(),
            override val actionLabel: String? = null,
            override val duration: SnackbarDuration = SnackbarDuration.Long,
            override val withDismissAction: Boolean = true,
            override val message: String,
        ) : AppSnackbar()

        @Immutable
        data class Failure(
            override val id: String = Uuid.random().toString(),
            override val actionLabel: String? = null,
            override val duration: SnackbarDuration = SnackbarDuration.Long,
            override val withDismissAction: Boolean = false,
            override val message: String
        ) : AppSnackbar()
    }
}

fun AppSnackbarManager.showSuccess(
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Long,
    withDismissAction: Boolean = true,
    message: () -> String,
): Boolean {
    return show(
        AppSnackbarManager.AppSnackbar.Success(
            actionLabel = actionLabel,
            duration = duration,
            withDismissAction = withDismissAction,
            message = message(),
        )
    )
}

fun AppSnackbarManager.showSuccess(
    indefinite: Boolean,
    message: () -> String,
): Boolean {
    return if (indefinite) {
        showSuccess(message = message, duration = SnackbarDuration.Indefinite)
    } else {
        showSuccess(message = message)
    }
}

fun AppSnackbarManager.showFailure(
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Long,
    withDismissAction: Boolean = true,
    message: () -> String
): Boolean {
    return show(
        AppSnackbarManager.AppSnackbar.Failure(
            actionLabel = actionLabel,
            duration = duration,
            withDismissAction = withDismissAction,
            message = message(),
        )
    )
}

fun AppSnackbarManager.showFailure(
    indefinite: Boolean,
    message: () -> String,
): Boolean {
    return if (indefinite) {
        showFailure(message = message, duration = SnackbarDuration.Indefinite)
    } else {
        showFailure(message = message)
    }
}
