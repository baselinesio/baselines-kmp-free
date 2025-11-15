package io.baselines.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

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

fun Project.addKspDependencyForAllTargets(dependencyNotation: Any) =
    addKspDependencyForAllTargets("", dependencyNotation)

private fun Project.addKspDependencyForAllTargets(
    configurationNameSuffix: String,
    dependencyNotation: Any,
) {
    val kmpExtension = extensions.getByType<KotlinMultiplatformExtension>()
    dependencies {
        kmpExtension.targets
            .asSequence()
            .filter { target ->
                // Don't add KSP for common target, only final platforms
                target.platformType != KotlinPlatformType.common
            }
            .forEach { target ->
                val capitalizedTarget = target.targetName.replaceFirstChar { it.uppercaseChar() }
                add(
                    "ksp$capitalizedTarget$configurationNameSuffix",
                    dependencyNotation,
                )
            }
    }
}
