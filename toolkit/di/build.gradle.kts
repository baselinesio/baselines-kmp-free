import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.metro)
}

androidLibrary("io.baselines.toolkit.di")

metro {
    enableKotlinVersionCompatibilityChecks.set(false)
}
