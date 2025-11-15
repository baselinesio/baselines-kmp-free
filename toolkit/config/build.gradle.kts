import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.baselines.di)
}

kotlin {
    androidLibrary("io.baselines.toolkit.config")
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.coroutines.core)
        }
    }
}
