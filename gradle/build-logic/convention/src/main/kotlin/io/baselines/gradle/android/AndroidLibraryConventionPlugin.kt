package io.baselines.gradle.android

import com.android.build.gradle.LibraryExtension
import io.baselines.gradle.Versions
import io.baselines.gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) { apply("com.android.library") }
            configureAndroidLibrary()
        }
    }

    private fun Project.configureAndroidLibrary() {
        android {
            compileSdkVersion(Versions.COMPILE_SDK)
            compileOptions { isCoreLibraryDesugaringEnabled = true }
            java { toolchain { languageVersion.set(JavaLanguageVersion.of(Versions.JAVA_VERSION)) } }

            defaultConfig { minSdk = Versions.MIN_SDK }
            defaultConfig { consumerProguardFiles("consumer-rules.pro") }

            testOptions {
                targetSdk = Versions.TARGET_SDK
                unitTests {
                    all {
                        it.useJUnitPlatform()
                        it.failOnNoDiscoveredTests.set(false)
                    }
                    isIncludeAndroidResources = true
                    isReturnDefaultValues = true
                }
            }
        }

        dependencies { "coreLibraryDesugaring"(libs.findLibrary("android.desugarjdklibs").get()) }
    }

    private fun Project.android(block: LibraryExtension.() -> Unit) {
        extensions.configure<LibraryExtension>(block)
    }
}

fun Project.androidLibrary(namespace: String, block: LibraryExtension.() -> Unit = {}) {
    extensions.configure<LibraryExtension> {
        this.namespace = namespace
        block()
    }
}
