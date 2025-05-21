package io.baselines.gradle

object Versions {

    const val COMPILE_SDK = 35
    const val TARGET_SDK = 35
    const val MIN_SDK = 27
    const val JAVA_VERSION = 17

    private const val VERSION_MAJOR = 0
    private const val VERSION_MINOR = 0
    private const val VERSION_PATCH = 1

    // e.g. "200 010 900" - major = 2; minor = 10; patch = 900;
    // max supported number for each version part is 999.
    const val VERSION_CODE = VERSION_MAJOR * 100000000 + VERSION_MINOR * 1000 + VERSION_PATCH
    const val VERSION_NAME = "$VERSION_MAJOR.$VERSION_MINOR.$VERSION_PATCH"
}
