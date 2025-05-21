package io.baselines.gradle

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.HasUnitTestBuilder
import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

fun Project.configureAndroid() {
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
            unitTests.apply {
                all { it.useJUnitPlatform() }
            }
            if (this@android is LibraryExtension) {
                // We only want to configure this for library modules
                targetSdk = Versions.TARGET_SDK
            }

            unitTests {
                isIncludeAndroidResources = true
                isReturnDefaultValues = true
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

fun Project.configureAndroidLibrary() {
    android {
        defaultConfig {
            consumerProguardFiles("consumer-rules.pro")
        }
    }
}

fun Project.configureAndroidKotlin() {
    android {
        with(extensions.getByType<KotlinAndroidProjectExtension>()) {
            compilerOptions {
                jvmTarget.set(JvmTarget.fromTarget(Versions.JAVA_VERSION.toString()))
            }
        }
    }
}

fun Project.configureAndroidApplication() {
    android {
        defaultConfig {
            versionCode = Versions.VERSION_CODE
            versionName = Versions.VERSION_NAME
        }

        application {
            applicationVariants.all {
                outputs.all {
                    if (this is ApkVariantOutputImpl) {
                        outputFileName = "app-${buildType.name}-v${versionName}.apk"
                    }
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

fun Project.android(action: BaseExtension.() -> Unit) = extensions.configure<BaseExtension>(action)

private fun BaseExtension.application(action: AppExtension.() -> Unit) = (this as? AppExtension)?.action()

private fun Project.androidComponents(action: AndroidComponentsExtension<*, *, *>.() -> Unit) {
    extensions.configure(AndroidComponentsExtension::class.java, action)
}
