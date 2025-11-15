package io.baselines.sample.compose.ui

import android.content.Context
import io.baselines.sample.compose.di.AndroidAppComponent

val Context.appComponent: AndroidAppComponent
    get() = (applicationContext as BaselineApplication).appComponent
