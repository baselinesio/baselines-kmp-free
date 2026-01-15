import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.baselines.compose)
    alias(libs.plugins.baselines.di)
}

androidLibrary("io.baselines.sample.ui.designsystem")

kotlin {
    sourceSets {
        androidMain.dependencies {
            api(libs.androidx.compose.tooling)
        }
        commonMain.dependencies {
            implementation(libs.androidx.compose.lifecycle)
            api(libs.androidx.compose.runtime)
            api(libs.kotlin.compose.preview)
            api(libs.kotlin.compose.material3)
            api(libs.kotlin.compose.resources)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.baselines.sample.ui.designsystem"
}
