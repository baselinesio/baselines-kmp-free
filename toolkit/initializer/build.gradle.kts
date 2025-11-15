import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.baselines.di)
}

kotlin {
    androidLibrary("io.baselines.toolkit.initializer") {
        withHostTest {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
            enableCoverage = true
        }
    }
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
