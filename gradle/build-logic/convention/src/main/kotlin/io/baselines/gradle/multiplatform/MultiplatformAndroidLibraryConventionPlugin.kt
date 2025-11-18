package io.baselines.gradle.multiplatform

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import io.baselines.gradle.Versions
import io.baselines.gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MultiplatformAndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.multiplatform")
            }
        }
    }
}

fun Project.androidLibrary(
    namespace: String,
    block: KotlinMultiplatformAndroidLibraryTarget.() -> Unit = {},
) {
    androidLibrary {
        this.namespace = namespace

        minSdk = Versions.MIN_SDK
        compileSdk = Versions.COMPILE_SDK
        enableCoreLibraryDesugaring = true

        compilerOptions { jvmTarget.set(JvmTarget.fromTarget(Versions.JAVA_VERSION.toString())) }

        block()
    }

    dependencies {
        add(
            "coreLibraryDesugaring",
            libs.findLibrary("android.desugarjdklibs").get()
        )
    }
}

private fun Project.androidLibrary(block: KotlinMultiplatformAndroidLibraryTarget.() -> Unit) {
    extensions.configure<KotlinMultiplatformExtension> {
        targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
            .configureEach { block() }
    }
}
