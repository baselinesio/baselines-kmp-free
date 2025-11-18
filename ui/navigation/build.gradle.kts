import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.baselines.compose)
    alias(libs.plugins.baselines.di)
    alias(libs.plugins.kotlin.serialization)
}

androidLibrary("io.baselines.sample.ui.navigation")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.serialization)
            api(libs.androidx.compose.lifecycle)
            api(libs.androidx.compose.navigation)
            implementation(libs.androidx.compose.runtime)
        }
    }
}
