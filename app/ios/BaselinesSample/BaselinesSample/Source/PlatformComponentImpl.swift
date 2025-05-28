import BaselinesSampleCompose

/// iOS-specific implementation of `PlatformComponent`.
///
/// This class exposes platform-native dependencies to the shared Kotlin codebase.
/// It is typically instantiated in `AppDelegate` and passed into the shared module initializer
/// to allow shared code to access native services like encryption, logging, or storage.
///
/// @see AppDelegate for platform bootstrap and dependency injection context.
class PlatformComponentImpl: PlatformComponent {
}
