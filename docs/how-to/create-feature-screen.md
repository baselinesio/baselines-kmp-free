# How to: Create Feature Screen

Before you dive in, it may be helpful to get familiar with the companion document
[**Architecture: Feature Screen**](../architecture/feature-screen.md), which explains how the individual classes
fit together. With that context in mind, this guide walks you through creating a new feature screen in the project.
You can choose one of three paths:

1. **Using file templates** — the recommended, fast and stable way
2. **Ask an AI agent** — automated, the fastest way
3. **Manual setup** — when you need full control or when templates aren't available

---

### **Option 1: Using File Templates (recommended)**

1. Ensure the **UI Feature** template is installed
    - File → Manage IDE Settings → Import Settings...
    - Import `file_teamplates.zip` from the **baselines-kmp** root dir
2. Right-click the destination package → **New → UI Feature**
3. Enter the feature name (e.g., Profile, Settings)
4. Review generated files and fix missing imports
5. Done 🎉 — your feature is wired into the app

### Option 2: Setup via AI Agent

1. Open chat with your AI assistant and make sure it's aware of `./AGENTS.md` file.
2. Ask to create new feature screen
3. Provide all the necessary info requested by the AI agent
4. Done 🎉 — your feature is wired into the app

### Option 3: Manual Setup

1. Create `*UiEvent`

```kotlin
sealed interface ProfileUiEvent : UiEvent
```

1. Create `*UiState`

```kotlin
@Immutable
data class ProfileUiState(
    override val eventSink: (ProfileUiEvent) -> Unit,
) : UiState<ProfileUiEvent>
```

1. Create `*ViewModel`

```kotlin
@Inject
class ProfileViewModel : BaselineViewModel<ProfileUiEvent, ProfileUiState>() {

    @Composable
    override fun state() = ProfileUiState { /* handle events */ }
}
```

1. Create `*Screen`

```kotlin
@Composable
fun ProfileScreen(onLogoutClicked: () -> Unit) { /* UI */
}
```

1. Create `*Route`

```kotlin
@Composable
fun ProfileRoute(viewModel: ProfileViewModel) {
    val state = viewModel.state()
    val eventSink = state.eventSink
    ProfileScreen(
        onLogoutClicked = { eventSink(ProfileUiEvent.PerformLogout) }
    )
}
```

1. Add DI `*Module`

```kotlin
@ContributesTo(UiScope::class)
interface ProfileUiModule {

    @Provides
    @IntoSet
    fun provideProfileNavGraphEntry(
        vmFactory: () -> ProfileViewModel,
    ): NavGraphEntry = NavGraphEntry {
        composable<AppNavRoutes.Profile> {
            ProfileRoute(viewModel(vmFactory))
        }
    }
}
```

1. Ensure the feature module is a dependency of **app/compose** module
2. Done 🎉 — your feature is wired into the app
