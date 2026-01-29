package io.baselines.sample

import android.content.Context
import io.baselines.sample.compose.di.AndroidAppComponent

val Context.appComponent: AndroidAppComponent
    get() = (applicationContext as App).appComponent
