import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.baselines.compose)
    alias(libs.plugins.baselines.di)
}

androidLibrary("io.baselines.ui.viewmodel") {
    withHostTest {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
        enableCoverage = true
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.compose.runtime)
            api(libs.metro.viewmodel)
            api(libs.androidx.compose.viewModel)
            api(libs.androidx.compose.lifecycle)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.coroutines.test)
        }
    }
}
