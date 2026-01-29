import io.baselines.gradle.multiplatform.androidLibrary
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.baselines.compose)
    alias(libs.plugins.baselines.di)
}

androidLibrary("io.baselines.sample.compose")

kotlin {
    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = "BaselinesSampleCompose"
            isStatic = true

            binaries.configureEach {
                // Add linker flag for SQLite. See:
                // https://github.com/touchlab/SQLiter/issues/77
                linkerOpts("-lsqlite3")
            }

            export(projects.toolkit.config)
            export(projects.toolkit.initializer)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain)

            api(projects.toolkit.initializer)
            api(projects.toolkit.config)
            api(projects.toolkit.logger)
            api(projects.toolkit.coroutines)

            api(projects.ui.viewModel)
            api(projects.ui.designSystem)
            api(projects.ui.navigation)
            api(projects.ui.playground)
            api(projects.ui.home)

            implementation(libs.kotlin.immutableCollections)
        }
    }
}
