package io.baselines.gradle.android

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

fun Project.java(block: JavaPluginExtension.() -> Unit) {
    extensions.configure<JavaPluginExtension>(block)
}
