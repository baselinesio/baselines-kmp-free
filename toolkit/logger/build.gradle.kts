import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
}

androidLibrary("io.baselines.toolkit.logger")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.logging.kermit)
        }
    }
}
