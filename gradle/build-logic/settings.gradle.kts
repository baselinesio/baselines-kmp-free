dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google {
            content {
                includeGroupByRegex(".*google.*")
                includeGroupByRegex(".*android.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }

    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
}

buildCache {
    val isCi = providers.environmentVariable("CI").isPresent
    local {
        isEnabled = !isCi
    }
}

rootProject.name = "buildLogic"

include(":convention")
