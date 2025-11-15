package io.baselines.gradle.android

import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import io.baselines.gradle.Versions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) { apply("com.android.application") }
            configureAndroidCommon()
            configureAndroidApplication()
        }
    }

    private fun Project.configureAndroidApplication() {
        android {
            defaultConfig {
                versionCode = Versions.VERSION_CODE
                versionName = Versions.VERSION_NAME
            }

            applicationVariants.all {
                outputs.all {
                    if (this is ApkVariantOutputImpl) {
                        outputFileName = "app-${buildType.name}-v${versionName}.apk"
                    }
                }
            }

            buildTypes {
                register("staging") {
                    initWith(getByName("debug"))
                    matchingFallbacks += listOf("debug")
                    isMinifyEnabled = true
                    isShrinkResources = true
                    isDebuggable = false
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                }

                getByName("release") {
                    isMinifyEnabled = true
                    isShrinkResources = true
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                }
            }
        }
    }

    private fun Project.android(block: AppExtension.() -> Unit) {
        extensions.configure<AppExtension>(block)
    }
}
