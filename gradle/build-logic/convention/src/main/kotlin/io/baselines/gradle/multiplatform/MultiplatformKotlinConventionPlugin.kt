package io.baselines.gradle.multiplatform

import io.baselines.gradle.Versions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

class MultiplatformKotlinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) { apply("org.jetbrains.kotlin.multiplatform") }
        configureMultiplatform()
    }

    private fun Project.configureMultiplatform() {
        kotlin { jvmToolchain(Versions.JAVA_VERSION) }
        multiplatform {
            applyDefaultHierarchyTemplate()

            // Supported platforms
            iosArm64()
            iosSimulatorArm64()
            // In case io.baselines.multiplatform.android.library plugin is not applied
            // and module has an android plugin, we should apply the android target manually
            if (pluginManager.hasPlugin("com.android.library")) {
                androidTarget()
            }

            sourceSets.all {
                languageSettings.optIn("kotlin.RequiresOptIn")
                languageSettings.optIn("kotlin.experimental.ExperimentalNativeApi")
                languageSettings.optIn("kotlin.time.ExperimentalTime")
                compilerOptions {
                    freeCompilerArgs.addAll(
                        "-Xexpect-actual-classes",
                    )
                }
            }
        }
    }

    private fun Project.multiplatform(block: KotlinMultiplatformExtension.() -> Unit) {
        extensions.configure<KotlinMultiplatformExtension>(block)
    }

    private fun Project.kotlin(block: KotlinProjectExtension.() -> Unit) {
        extensions.configure<KotlinProjectExtension>(block)
    }
}
