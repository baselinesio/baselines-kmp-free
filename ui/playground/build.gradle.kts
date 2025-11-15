import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.baselines.compose)
    alias(libs.plugins.baselines.di)
}

kotlin {
    androidLibrary("io.baselines.ui.playground")
    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain)

            implementation(projects.toolkit.logger)
            implementation(projects.toolkit.coroutines)
            implementation(projects.toolkit.config)

            implementation(projects.ui.designSystem)
            implementation(projects.ui.viewModel)
            implementation(projects.ui.navigation)

            implementation(libs.kotlin.immutableCollections)
        }
    }
}
