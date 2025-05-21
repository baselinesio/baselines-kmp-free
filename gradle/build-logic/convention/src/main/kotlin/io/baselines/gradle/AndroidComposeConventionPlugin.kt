package io.baselines.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
            configureCompose()
        }
    }


    private fun Project.configureCompose() {
        android {
            buildFeatures.compose = true
        }
    }
}
