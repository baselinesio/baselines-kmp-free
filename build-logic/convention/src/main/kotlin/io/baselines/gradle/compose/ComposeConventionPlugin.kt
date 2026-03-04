package io.baselines.gradle.compose

import io.baselines.gradle.ext.alias
import io.baselines.gradle.ext.commonMainImplementation
import io.baselines.gradle.ext.implementation
import io.baselines.gradle.ext.libs
import io.baselines.gradle.ext.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class ComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            plugins {
                alias(libs.plugins.kotlin.compose)
                alias(libs.plugins.kotlin.composeCompiler)
            }
            configureCompose()
            configureDependencies()
        }
    }

    private fun Project.configureCompose() {
        composeCompiler {
            // Needed for Layout Inspector to be able to see all the nodes in the component tree:
            //https://issuetracker.google.com/issues/338842143
            includeSourceInformation.set(true)
            stabilityConfigurationFiles.set(
                listOf(
                    rootProject.layout.projectDirectory.file("compose-stability.conf")
                )
            )
        }
    }

    private fun Project.configureDependencies() {
        dependencies {
            if (pluginManager.hasPlugin(libs.plugins.kotlin.multiplatform.get().pluginId)) {
                commonMainImplementation(libs.androidx.compose.runtime)
            } else {
                implementation(libs.androidx.compose.runtime)
            }
        }
    }

    private fun Project.composeCompiler(block: ComposeCompilerGradlePluginExtension.() -> Unit) {
        extensions.configure<ComposeCompilerGradlePluginExtension>(block)
    }
}
