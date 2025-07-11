plugins {
    alias(libs.plugins.baselines.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
}

android {
    namespace = "io.baselines.toolkit.logger"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.logging.kermit)
        }
    }
}
