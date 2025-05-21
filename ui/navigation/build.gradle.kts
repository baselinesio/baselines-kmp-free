plugins {
    alias(libs.plugins.baselines.android.library)
    alias(libs.plugins.baselines.multiplatform.compose)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.baselines.di)
}

android {
    namespace = "io.baselines.sample.ui.navigation"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.serialization)
            api(libs.androidx.compose.lifecycle)
            api(libs.androidx.compose.navigation)

            implementation(compose.runtime)
        }
    }
}
