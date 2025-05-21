package io.baselines.sample.ui.navigation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Utility function to define a [androidx.navigation.NavType] mapping for enum parameters in navigation routes,
 * with support for Kotlin Multiplatform (KMP) projects.
 *
 * In KMP, there is no built-in support for enum-safe navigation arguments on iOS, so this function helps
 * define a custom [androidx.navigation.NavType] for enums manually. It enables type-safe enum handling
 * when passing or restoring route parameters between destinations.
 *
 * @param TEnum The enum class type to be used as a navigation parameter.
 * @param nullable Whether the enum parameter is nullable. If `true`, the resulting type allows nulls.
 * @return A key-value pair where the key is a [kotlin.reflect.KType] of the enum and
 * the value is a [androidx.navigation.NavType] that supports storing and parsing the enum as a string.
 *
 * ### Usage
 * ```kotlin
 * composable<AuthRoute>(
 *     typeMap = mapOf(enumTypeMapEntry<EnumClass>())
 * )
 * ```
 *
 * This allows you to pass an enum as a navigation parameter, and have it correctly serialized to/from a string
 * under the hood.
 *
 * ### iOS Consideration
 * Since enum navigation argument support is missing on iOS, this helper ensures enums are
 * correctly handled across platforms by serializing them as their string names.
 */
inline fun <reified TEnum : Enum<TEnum>> enumTypeMapEntry(
    nullable: Boolean = false
): Pair<KType, NavType<TEnum?>> {
    val type = if (nullable) typeOf<TEnum?>() else typeOf<TEnum>()
    return type to object : NavType<TEnum?>(isNullableAllowed = nullable) {
        override fun get(bundle: SavedState, key: String): TEnum? {
            val value = bundle.read { getStringOrNull(key) }
            return value?.let(this::parseValue)
        }

        override fun parseValue(value: String): TEnum = enumValueOf(value)

        override fun put(bundle: SavedState, key: String, value: TEnum?) {
            bundle.write {
                value?.let { putString(key, it.name) } ?: putNull(key)
            }
        }
    }
}
