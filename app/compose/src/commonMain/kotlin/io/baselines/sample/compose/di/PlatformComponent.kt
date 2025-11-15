package io.baselines.sample.compose.di

import io.baselines.toolkit.config.AppConfig

/**
 * Marker interface used as a platform bridge to expose platform-specific dependencies
 * to the shared `commonMain` module.
 *
 * This interface is implemented separately on each platform:
 * - On **Android**, the implementation is provided in Kotlin (e.g., `PlatformComponentImpl.kt`)
 * - On **iOS**, the implementation is provided in Swift (e.g., `PlatformComponentImpl.swift`)
 *
 * ### Purpose
 * `PlatformComponent` serves as a glue layer that enables shared code to depend on
 * platform-specific implementations (e.g., system crypto, analytics, logging, etc.)
 * without directly referencing platform-specific APIs.
 *
 * ### Example Use Case
 * Consider a `WebOAuth2Session` interface defined in shared code with platform-specific implementations.
 * To expose this into shared logic:
 *
 * ```kotlin
 * interface PlatformComponent {
 *     val webOAuth2Session: WebOAuth2Session
 * }
 * ```
 *
 * On Android:
 * ```kotlin
 * class PlatformComponentImpl : PlatformComponent {
 *     override val webOAuth2Session: WebOAuth2Session = WebOAuth2SessionImpl()
 * }
 * ```
 *
 * On iOS:
 * ```swift
 * class PlatformComponentImpl: PlatformComponent {
 *     let webOAuth2Session: WebOAuth2Session = WebOAuth2SessionImpl()
 * }
 * ```
 *
 * You can then inject this `PlatformComponent` into shared code, allowing platform bindings
 * to be resolved during composition/bootstrap without violating architecture boundaries.
 */
interface PlatformComponent {

    val appInfo: AppConfig.AppInfo
}
