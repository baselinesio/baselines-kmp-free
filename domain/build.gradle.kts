import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
}

androidLibrary("io.baselines.sample.domain")

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.toolkit.coroutines)
            implementation(projects.toolkit.logger)
            implementation(projects.toolkit.di)
            implementation(projects.toolkit.config)
            implementation(projects.toolkit.time)
        }
    }
}
