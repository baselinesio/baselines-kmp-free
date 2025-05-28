package io.baselines.toolkit.config

data class AppConfig(
    val debug: Boolean = false,
    val version: String = "",
    val platform: Platform = Platform.UNKNOWN,
) {

    enum class Platform {
        IOS, ANDROID, UNKNOWN
    }
}
