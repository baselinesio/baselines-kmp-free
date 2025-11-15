package io.baselines.gradle.compose

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class ComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            configureCompose()
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

    private fun Project.composeCompiler(block: ComposeCompilerGradlePluginExtension.() -> Unit) {
        extensions.configure<ComposeCompilerGradlePluginExtension>(block)
    }
}
