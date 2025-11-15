package io.baselines.gradle.android

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
            configureCompose()
        }
    }

    private fun Project.configureCompose() {
        extensions.configure<BaseExtension> {
            buildFeatures.compose = true
        }
    }
}
