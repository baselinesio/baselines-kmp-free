import io.baselines.gradle.android.androidLibrary

plugins {
    alias(libs.plugins.baselines.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.baselines.compose)
    alias(libs.plugins.baselines.di)
}

androidLibrary("io.baselines.ui.playground")

kotlin {
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
