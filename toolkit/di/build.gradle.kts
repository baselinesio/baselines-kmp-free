import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
}

kotlin {
    androidLibrary("io.baselines.toolkit.di")
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlin.inject.runtime)
            api(libs.kotlin.inject.anvil.runtime)
            api(libs.kotlin.inject.anvil.runtimeOptional)
        }
    }
}
