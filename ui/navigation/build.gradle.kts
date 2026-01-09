import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.baselines.compose)
    alias(libs.plugins.baselines.di)
}

androidLibrary("io.baselines.sample.ui.navigation")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.api)
            implementation(libs.androidx.compose.runtime)
            api(libs.androidx.compose.lifecycle)
            api(libs.androidx.compose.navigation)
        }
    }
}
