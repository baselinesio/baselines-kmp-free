plugins {
    alias(libs.plugins.baselines.android.library)
    alias(libs.plugins.baselines.multiplatform.compose)
    alias(libs.plugins.baselines.multiplatform.kotlin)
}

android {
    namespace = "io.baselines.sample.ui.designsystem"
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            api(compose.uiTooling)
        }
        commonMain.dependencies {
            implementation(libs.androidx.compose.viewModel)
            api(compose.runtime)
            api(compose.material3)
            api(compose.components.resources)
            api(compose.components.uiToolingPreview)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.baselines.sample.ui.designsystem"
}
