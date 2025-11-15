package io.baselines.gradle

object Versions {

    const val COMPILE_SDK = 36
    const val TARGET_SDK = 36
    const val MIN_SDK = 27
    const val JAVA_VERSION = 17

    private const val FORM_FACTOR = 1
    private const val VERSION_MAJOR = 0
    private const val VERSION_MINOR = 0
    private const val VERSION_PATCH = 1

    // e.g. "1 02 010 090" - form factor (mobile) = 1; major = 2; minor = 10; patch = 90;
    // Max supported for segments:
    // - minor and patch = 999
    // - major = 99
    const val VERSION_CODE = (((FORM_FACTOR * 1_00) + VERSION_MAJOR) * 1_000 + VERSION_MINOR) * 1_000 + VERSION_PATCH
    const val VERSION_NAME = "$FORM_FACTOR-$VERSION_MAJOR.$VERSION_MINOR.$VERSION_PATCH"
}
