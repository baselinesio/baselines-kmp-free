package io.baselines.gradle.android

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

fun Project.kotlin(block: KotlinAndroidProjectExtension.() -> Unit) {
    extensions.configure<KotlinAndroidProjectExtension>(block)
}

fun Project.java(block: JavaPluginExtension.() -> Unit) {
    extensions.configure<JavaPluginExtension>(block)
}
