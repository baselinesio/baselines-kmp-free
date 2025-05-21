plugins {
    alias(libs.plugins.baselines.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
}

android {
    namespace = "io.baselines.toolkit.di"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlin.inject.runtime)
            api(libs.kotlin.inject.anvil.runtime)
            api(libs.kotlin.inject.anvil.runtimeOptional)
        }
    }
}
