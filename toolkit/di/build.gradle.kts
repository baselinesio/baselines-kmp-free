import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.inject.metro)
}

androidLibrary("io.baselines.toolkit.di")

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.metro.viewmodel)
        }
    }
}
