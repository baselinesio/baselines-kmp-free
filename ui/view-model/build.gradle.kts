plugins {
    alias(libs.plugins.baselines.android.library)
    alias(libs.plugins.baselines.multiplatform.compose)
    alias(libs.plugins.baselines.multiplatform.kotlin)
}

android {
    namespace = "io.baselines.ui.viewmodel"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.compose.viewModel)
            api(libs.androidx.compose.lifecycle)

            implementation(compose.runtime)
        }
    }
}
