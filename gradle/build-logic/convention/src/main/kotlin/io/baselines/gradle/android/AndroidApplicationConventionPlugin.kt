package io.baselines.gradle.android

import com.android.build.api.dsl.ApplicationExtension
import io.baselines.gradle.Versions
import io.baselines.gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }
            configureAndroidApplication()
        }
    }

    private fun Project.configureAndroidApplication() {
        android {
            compileSdk { version = release(Versions.COMPILE_SDK) }
            compileOptions { isCoreLibraryDesugaringEnabled = true }
            java { toolchain { languageVersion.set(JavaLanguageVersion.of(Versions.JAVA_VERSION)) } }
            kotlin { compilerOptions { jvmTarget.set(JvmTarget.fromTarget(Versions.JAVA_VERSION.toString())) } }

            defaultConfig {
                minSdk = Versions.MIN_SDK
                targetSdk = Versions.TARGET_SDK
                versionCode = Versions.VERSION_CODE
                versionName = Versions.VERSION_NAME
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

        dependencies { "coreLibraryDesugaring"(libs.findLibrary("android.desugarjdklibs").get()) }
    }

    private fun Project.android(block: ApplicationExtension.() -> Unit) {
        extensions.configure<ApplicationExtension>(block)
    }

    private fun Project.kotlin(block: KotlinAndroidProjectExtension.() -> Unit) {
        extensions.configure<KotlinAndroidProjectExtension>(block)
    }
}
