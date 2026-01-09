@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("gradle/build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":app:android")
include(":app:compose")

include(":domain")
include(":domain:api")

include(":toolkit:di")
include(":toolkit:initializer")
include(":toolkit:logger")
include(":toolkit:coroutines")
include(":toolkit:config")
include(":toolkit:time")

include(":ui:playground")
include(":ui:design-system")
include(":ui:view-model")
include(":ui:navigation")
include(":ui:home")
