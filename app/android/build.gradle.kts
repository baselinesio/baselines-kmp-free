plugins {
    alias(libs.plugins.baselines.android.application)
    alias(libs.plugins.baselines.compose)
    alias(libs.plugins.baselines.di)
}

android {
    namespace = "io.baselines.sample"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.splash)
    implementation(projects.app.compose)
}
