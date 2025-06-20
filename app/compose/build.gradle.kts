import io.baselines.gradle.addKspDependencyForAllTargets
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.baselines.android.library)
    alias(libs.plugins.baselines.multiplatform.compose)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.baselines.di)
}

android {
    namespace = "io.baselines.sample"

    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = "BaselinesSampleCompose"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.splash)
        }
        commonMain.dependencies {
            implementation(projects.domain)

            implementation(projects.toolkit.initializer)
            implementation(projects.toolkit.logger)
            implementation(projects.toolkit.coroutines)
            implementation(projects.toolkit.config)

            implementation(projects.ui.designSystem)
            implementation(projects.ui.viewModel)
            implementation(projects.ui.navigation)
            implementation(projects.ui.playground)
            implementation(projects.ui.home)

            implementation(libs.kotlin.immutableCollections)
        }
    }
}

addKspDependencyForAllTargets(libs.kotlin.inject.compiler)
