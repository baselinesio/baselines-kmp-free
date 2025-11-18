import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
}

androidLibrary("io.baselines.toolkit.di")

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlin.inject.runtime)
            api(libs.kotlin.inject.anvil.runtime)
            api(libs.kotlin.inject.anvil.runtimeOptional)
        }
    }
}
