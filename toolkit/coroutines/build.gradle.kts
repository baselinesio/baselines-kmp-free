import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
}

kotlin {
    androidLibrary("io.baselines.toolkit.coroutines")
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlin.coroutines.core)
        }
    }
}
