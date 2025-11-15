package io.baselines.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class RootConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            createTask("exportIosAppVersion") { exportIosAppVersion() }
        }
    }

    private fun Task.exportIosAppVersion() {
        val versionCode = Versions.VERSION_CODE
        val versionName = Versions.VERSION_NAME
        val configFile = project.rootProject.file("app/ios/BaselinesSample/Configuration/Config.xcconfig")
        doLast {
            val updatedLines = configFile.readLines().map { line ->
                when {
                    line.trim().startsWith("MARKETING_VERSION=") -> "MARKETING_VERSION=$versionName"
                    line.trim().startsWith("CURRENT_PROJECT_VERSION=") -> "CURRENT_PROJECT_VERSION=$versionCode"
                    else -> line
                }
            }
            configFile.writeText(updatedLines.joinToString("\n"))
        }
    }

    private fun Project.createTask(name: String, action: Task.() -> Unit) {
        tasks.register(name) { action() }
    }
}
