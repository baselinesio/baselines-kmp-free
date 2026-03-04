package io.baselines.gradle.android

import com.android.build.api.dsl.LibraryExtension
import io.baselines.gradle.Versions
import io.baselines.gradle.ext.alias
import io.baselines.gradle.ext.coreLibraryDesugaring
import io.baselines.gradle.ext.java
import io.baselines.gradle.ext.libs
import io.baselines.gradle.ext.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            plugins {
                alias(libs.plugins.android.library)
            }
            configureAndroidLibrary()
        }
    }

    private fun Project.configureAndroidLibrary() {
        android {
            compileSdk {
                version = release(Versions.COMPILE_SDK)
            }
            compileOptions { isCoreLibraryDesugaringEnabled = true }
            java { toolchain { languageVersion.set(JavaLanguageVersion.of(Versions.JAVA_VERSION)) } }

            defaultConfig {
                minSdk = Versions.MIN_SDK
                consumerProguardFiles("consumer-rules.pro")
            }

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
        dependencies {
            coreLibraryDesugaring(libs.android.desugarjdklibs)
        }
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
