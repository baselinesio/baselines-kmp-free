package io.baselines.gradle.multiplatform

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon

class MultiplatformKotlinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) { apply("org.jetbrains.kotlin.multiplatform") }
        configureMultiplatform()
    }

    private fun Project.configureMultiplatform() {
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

            metadata {
                compilations.configureEach {
                    if (name == KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME) {
                        compileTaskProvider.configure {
                            // We replace the default library names with something more unique (the project path).
                            // This allows us to avoid the annoying issue of `duplicate library name: foo_commonMain`
                            // https://youtrack.jetbrains.com/issue/KT-57914
                            val projectPath = path.substring(1).replace(":", "_")
                            this as KotlinCompileCommon
                            moduleName.set("${projectPath}_commonMain")
                        }
                    }
                }
            }
        }
    }

    private fun Project.multiplatform(block: KotlinMultiplatformExtension.() -> Unit) {
        extensions.configure<KotlinMultiplatformExtension>(block)
    }
}
