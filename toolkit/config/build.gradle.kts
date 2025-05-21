plugins {
    alias(libs.plugins.baselines.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.baselines.di)
}

android {
    namespace = "io.baselines.toolkit.config"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.coroutines.core)
        }
    }
}
