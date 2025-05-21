plugins {
    alias(libs.plugins.baselines.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.baselines.di)
}

android {
    namespace = "io.baselines.toolkit.initializer"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.toolkit.logger)
            implementation(projects.toolkit.coroutines)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.coroutines.test)
        }
    }
}
