import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
}

kotlin {
    androidLibrary("io.baselines.toolkit.logger")
    sourceSets {
        commonMain.dependencies {
            implementation(libs.logging.kermit)
        }
    }
}
