import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.baselines.compose)
}

androidLibrary("io.baselines.ui.viewmodel")

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.compose.viewModel)
            api(libs.androidx.compose.lifecycle)
            implementation(libs.androidx.compose.runtime)
        }
    }
}
