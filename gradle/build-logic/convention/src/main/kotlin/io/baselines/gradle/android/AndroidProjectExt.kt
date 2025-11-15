package io.baselines.gradle.android

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.HasUnitTestBuilder
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import io.baselines.gradle.Versions
import io.baselines.gradle.libs
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

fun Project.configureAndroidCommon() {
    pluginManager.apply("org.jetbrains.kotlin.android")

    android {
        compileSdkVersion(Versions.COMPILE_SDK)

        defaultConfig {
            minSdk = Versions.MIN_SDK
            targetSdk = Versions.TARGET_SDK
        }

        compileOptions {
            isCoreLibraryDesugaringEnabled = true
        }

        testOptions {
            if (this@android is LibraryExtension) {
                targetSdk = Versions.TARGET_SDK
            }

            unitTests {
                all {
                    it.useJUnitPlatform()
                    it.failOnNoDiscoveredTests.set(false)
                }
                isIncludeAndroidResources = true
                isReturnDefaultValues = true
            }
        }

        kotlin {
            compilerOptions {
                jvmTarget.set(JvmTarget.fromTarget(Versions.JAVA_VERSION.toString()))
            }
        }

        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(Versions.JAVA_VERSION))
            }
        }
    }

    androidComponents {
        beforeVariants(selector().withBuildType("release")) { variantBuilder ->
            (variantBuilder as? HasUnitTestBuilder)?.apply {
                enableUnitTest = false
            }
        }
    }

    dependencies {
        "coreLibraryDesugaring"(libs.findLibrary("android.desugarjdklibs").get())
    }
}

private fun Project.android(block: BaseExtension.() -> Unit) {
    extensions.configure<BaseExtension>(block)
}

private fun Project.kotlin(block: KotlinAndroidProjectExtension.() -> Unit) {
    extensions.configure<KotlinAndroidProjectExtension>(block)
}

private fun Project.java(block: JavaPluginExtension.() -> Unit) {
    extensions.configure<JavaPluginExtension>(block)
}

private fun Project.androidComponents(block: AndroidComponentsExtension<*, *, *>.() -> Unit) {
    extensions.configure(AndroidComponentsExtension::class.java, block)
}
