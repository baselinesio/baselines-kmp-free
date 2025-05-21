plugins {
    alias(libs.plugins.baselines.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
}

android {
    namespace = "io.baselines.sample.domain"
}

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
