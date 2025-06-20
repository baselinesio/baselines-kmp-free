package io.baselines.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class DiConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.google.devtools.ksp")

        if (pluginManager.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
            addKspDependencies(multiplatform = true)
            addDiDependencies("commonMainImplementation")
        } else {
            addKspDependencies(multiplatform = false)
            addDiDependencies("implementation")
        }
    }

    private fun Project.addDiDependencies(configuration: String) {
        dependencies {
            add(
                configuration,
                libs.findLibrary("kotlin.inject.anvil.runtime").get()
            )
            add(
                configuration,
                libs.findLibrary("kotlin.inject.anvil.runtimeOptional").get()
            )
            add(
                configuration,
                project(":toolkit:di")
            )
        }
    }

    private fun Project.addKspDependencies(multiplatform: Boolean) {
        val anvilKsp = libs.findLibrary("kotlin.inject.anvil.compiler").get()
        if (multiplatform) {
            addKspDependencyForAllTargets(anvilKsp)
        } else {
            dependencies {
                add("ksp", anvilKsp)
            }
        }
    }
}
