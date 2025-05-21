package io.baselines.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
inline fun <reified VM : ViewModel> viewModel(crossinline factory: () -> VM): VM {
    return viewModel { factory() }
}

@Composable
inline fun <reified VM : ViewModel> viewModel(crossinline factory: (SavedStateHandle) -> VM): VM {
    return viewModel {
        factory(createSavedStateHandle())
    }
}
