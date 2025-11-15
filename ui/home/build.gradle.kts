import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.baselines.compose)
    alias(libs.plugins.baselines.di)
}

kotlin {
    androidLibrary("io.baselines.sample.ui.home")
    sourceSets {
        commonMain.dependencies {
            implementation(projects.toolkit.logger)
            implementation(projects.toolkit.coroutines)
            implementation(projects.ui.designSystem)
            implementation(projects.ui.viewModel)
            implementation(projects.ui.navigation)
        }
    }
}
