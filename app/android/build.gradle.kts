plugins {
    alias(libs.plugins.baselines.android.application)
    alias(libs.plugins.baselines.android.kotlin)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.composeCompiler)
}

android {
    namespace = "io.baselines.sample"
}

dependencies {
    implementation(libs.androidx.compose.activity)
    implementation(projects.app.compose)
}
