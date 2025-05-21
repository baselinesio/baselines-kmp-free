package io.baselines.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class DiConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.google.devtools.ksp")
        target.addKspDependencyForAllTargets(libs.findLibrary("kotlin.inject.anvil.compiler").get())
        dependencies {
            add(
                "commonMainImplementation",
                libs.findLibrary("kotlin.inject.anvil.runtime").get()
            )
            add(
                "commonMainImplementation",
                libs.findLibrary("kotlin.inject.anvil.runtimeOptional").get()
            )
            add(
                "commonMainImplementation",
                project(":toolkit:di")
            )
        }
    }
}
