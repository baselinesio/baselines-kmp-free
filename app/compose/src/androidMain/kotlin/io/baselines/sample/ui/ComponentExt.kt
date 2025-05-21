package io.baselines.sample.ui

import android.content.Context
import io.baselines.sample.di.AndroidAppComponent

val Context.appComponent: AndroidAppComponent
    get() = (applicationContext as BaselineApplication).appComponent
