package io.baselines.gradle

import dev.zacsweers.metro.gradle.MetroPluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class DiConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("dev.zacsweers.metro")
        extensions.configure<MetroPluginExtension> {
            generateContributionHintsInFir.set(true)
            enableTopLevelFunctionInjection.set(true)
        }
        dependencies {
            if (pluginManager.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
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
}
