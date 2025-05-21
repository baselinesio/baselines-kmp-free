package io.baselines.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidKotlinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.android")
            configureAndroidKotlin()
        }
    }
}
