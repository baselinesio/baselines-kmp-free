plugins {
    alias(libs.plugins.baselines.root)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.kotlin) apply false
    alias(libs.plugins.gradle.cacheFixPlugin) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.composeCompiler) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.google.ksp) apply false
}
