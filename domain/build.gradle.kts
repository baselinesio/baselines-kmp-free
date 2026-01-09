import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.baselines.di)
}

androidLibrary("io.baselines.sample.domain")

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.domain.api)
            implementation(projects.toolkit.coroutines)
            implementation(projects.toolkit.logger)
            implementation(projects.toolkit.config)
            implementation(projects.toolkit.time)
        }
    }
}
