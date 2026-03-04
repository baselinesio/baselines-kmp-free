package io.baselines.gradle

import dev.zacsweers.metro.gradle.MetroPluginExtension
import io.baselines.gradle.ext.alias
import io.baselines.gradle.ext.libs
import io.baselines.gradle.ext.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class DiConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        plugins { alias(libs.plugins.metro) }
        metro {
            enableKotlinVersionCompatibilityChecks.set(false)
        }
        dependencies {
            if (pluginManager.hasPlugin(libs.plugins.kotlin.multiplatform.get().pluginId)) {
                add(
                    "commonMainImplementation",
                    project(":toolkit:di")
                )
            } else {
                add(
                    "implementation",
                    project(":toolkit:di")
                )
            }
        }
    }

    private fun Project.metro(action: MetroPluginExtension.() -> Unit) {
        extensions.configure<MetroPluginExtension> { action() }
    }
}
