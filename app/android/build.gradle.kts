plugins {
    alias(libs.plugins.baselines.android.application)
    alias(libs.plugins.baselines.compose)
}

android {
    namespace = "io.baselines.sample"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.compose.activity)
    implementation(projects.app.compose)
}
