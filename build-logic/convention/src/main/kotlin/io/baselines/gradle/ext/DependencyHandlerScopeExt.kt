package io.baselines.gradle.ext

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.coreLibraryDesugaring(dependency: Any) {
    add("coreLibraryDesugaring", dependency)
}

fun DependencyHandlerScope.commonMainImplementation(dependency: Any) {
    add("commonMainImplementation", dependency)
}

fun DependencyHandlerScope.implementation(dependency: Any) {
    add("implementation", dependency)
}
