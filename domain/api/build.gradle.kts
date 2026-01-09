import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.kotlin.serialization)
}

androidLibrary("io.baselines.sample.domain.api")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.toolkit.coroutines)
            implementation(projects.toolkit.logger)
            implementation(libs.kotlin.serialization)
        }
    }
}
