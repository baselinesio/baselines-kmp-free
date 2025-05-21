plugins {
    alias(libs.plugins.baselines.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
}

android {
    namespace = "io.baselines.toolkit.coroutines"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlin.coroutines.core)
        }
    }
}
