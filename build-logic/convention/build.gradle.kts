import org.gradle.initialization.DependenciesAccessors
import org.gradle.kotlin.dsl.support.serviceOf

plugins {
    `kotlin-dsl`
}

java {
    toolchain {
        val javaVersion = libs.versions.java.get()
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.metro.gradlePlugin)
    compileOnly(libs.composeCompiler.gradlePlugin)
}

gradle.serviceOf<DependenciesAccessors>().classes.asFiles.forEach {
    dependencies {
        compileOnly(files(it.absolutePath))
    }
}

gradlePlugin {
    plugins {
        register("root") {
            id = "io.baselines.root"
            implementationClass = "io.baselines.gradle.RootConventionPlugin"
        }

        register("di") {
            id = "io.baselines.di"
            implementationClass = "io.baselines.gradle.DiConventionPlugin"
        }

        register("androidApplication") {
            id = "io.baselines.android.application"
            implementationClass = "io.baselines.gradle.android.AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "io.baselines.android.library"
            implementationClass = "io.baselines.gradle.android.AndroidLibraryConventionPlugin"
        }

        register("compose") {
            id = "io.baselines.compose"
            implementationClass = "io.baselines.gradle.compose.ComposeConventionPlugin"
        }

        register("multiplatformAndroidLibrary") {
            id = "io.baselines.multiplatform.android.library"
            implementationClass = "io.baselines.gradle.multiplatform.MultiplatformAndroidLibraryConventionPlugin"
        }

        register("multiplatformKotlin") {
            id = "io.baselines.multiplatform.kotlin"
            implementationClass = "io.baselines.gradle.multiplatform.MultiplatformKotlinConventionPlugin"
        }
    }
}
