package io.baselines.gradle.multiplatform

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import io.baselines.gradle.Versions
import io.baselines.gradle.ext.alias
import io.baselines.gradle.ext.coreLibraryDesugaring
import io.baselines.gradle.ext.libs
import io.baselines.gradle.ext.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MultiplatformAndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            plugins {
                alias(libs.plugins.android.multiplatform.library)
                alias(libs.plugins.kotlin.multiplatform)
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
        androidResources.enable = true

        compilerOptions {
            val javaVersion = libs.versions.java.get()
            jvmTarget.set(JvmTarget.fromTarget(javaVersion))
        }

        block()
    }

    dependencies {
        coreLibraryDesugaring(libs.android.desugarjdklibs)
    }
}

private fun Project.androidLibrary(block: KotlinMultiplatformAndroidLibraryTarget.() -> Unit) {
    extensions.configure<KotlinMultiplatformExtension> {
        targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java)
            .configureEach { block() }
    }
}
