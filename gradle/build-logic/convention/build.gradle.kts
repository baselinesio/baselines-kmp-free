plugins {
    `kotlin-dsl`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.composeCompiler.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("root") {
            id = "io.baselines.root"
            implementationClass = "io.baselines.gradle.RootPlugin"
        }

        register("di") {
            id = "io.baselines.di"
            implementationClass = "io.baselines.gradle.DiConventionPlugin"
        }

        register("androidApplication") {
            id = "io.baselines.android.application"
            implementationClass = "io.baselines.gradle.AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "io.baselines.android.library"
            implementationClass = "io.baselines.gradle.AndroidLibraryConventionPlugin"
        }

        register("androidKotlin") {
            id = "io.baselines.android.kotlin"
            implementationClass = "io.baselines.gradle.AndroidKotlinConventionPlugin"
        }

        register("androidCompose") {
            id = "io.baselines.android.compose"
            implementationClass = "io.baselines.gradle.AndroidComposeConventionPlugin"
        }

        register("multiplatformCompose") {
            id = "io.baselines.multiplatform.compose"
            implementationClass = "io.baselines.gradle.MultiplatformComposeConventionPlugin"
        }

        register("multiplatformKotlin") {
            id = "io.baselines.multiplatform.kotlin"
            implementationClass = "io.baselines.gradle.MultiplatformKotlinConventionPlugin"
        }
    }
}
