package io.baselines.gradle.ext

import org.gradle.api.plugins.PluginManager
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency

fun PluginManager.alias(dependency: Provider<PluginDependency>) {
    apply(dependency.get().pluginId)
}
