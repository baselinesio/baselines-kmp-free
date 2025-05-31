plugins {
    alias(libs.plugins.baselines.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.baselines.multiplatform.compose)
    alias(libs.plugins.baselines.di)
}

android {
    namespace = "io.baselines.sample.ui.home"
}

kotlin {
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
