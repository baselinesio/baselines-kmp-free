# Architecture: Feature Screen

Explains **why** each piece exists and how to wire them together.

---

## `UiEvent` – Capturing User Actions

**Purpose:** enumerate every user interaction (clicks, swipes, etc.).

<aside>
💡

Start minimal; add events as the UI grows.

</aside>

```kotlin
sealed interface ProfileUiEvent : UiEvent {
    data object OnLogoutClicked : ProfileUiEvent
}
```

## **`UiState` – UI State Single Source of Truth**

**Purpose:** holds everything the composable needs to render.

<aside>
💡

- Annotate with **@Immutable** so Compose skips redundant recompositions.
- If the state becomes large, split it into smaller data classes and nest inside the main state data class.

</aside>

```kotlin
@Immutable
data class ProfileUiState(
    override val eventSink: (ProfileUiEvent) -> Unit,
) : UiState<ProfileUiEvent>
```

## **`ViewModel` – UI logic**

**Purpose:** UI logic & state producer (BaselineViewModel).

<aside>
💡

`state()` is **@Composable** to leverage Compose snapshots and make sure UI is updated automatically, when the state
changes.

</aside>

```kotlin
@Inject
class ProfileViewModel : BaselineViewModel<ProfileUiEvent, ProfileUiState>() {

    private val sectionsFlow = mutableState(persistentListOf()) { createSections() }

    @Composable
    override fun state(): ProfileUiState {
        val sections by sectionsFlow.collectAsStateWithLifecycle()
        return ProfileUiState(sections) { event ->
            when (event) {
                ProfileUiEvent.PerformLogout -> handleLogout()
            }
        }
    }

    private fun handleLogout() { /* … */
    }

    private suspend fun createSections(): ImmutableList<Section> { /* … */
    }
}
```

## **`Screen` – Pure UI Layer**

**Purpose:** stateless composable that renders the layout.

<aside>
💡

Forward interactions via lambdas only.

</aside>

```kotlin
@Composable
fun ProfileScreen(
    sections: ImmutableList<Section>,
    onLogoutClicked: () -> Unit,
) {
    /* UI layout */
}
```

## **`Route` – Glue Layer**

**Purpose:** binds ViewModel state to the UI.

<aside>
💡

State provided by the ViewModel may change frequently. By extracting `eventSink` outside callbacks/lambdas you make sure
Compose treats them as stable objects and avoid redundant recompositions.

</aside>

```kotlin
@Composable
fun ProfileRoute(viewModel: ProfileViewModel) {
    val state = viewModel.state()
    val eventSink = state.eventSink
    ProfileScreen(
        sections = state.sections,
        onLogoutClicked = { eventSink(ProfileUiEvent.PerformLogout) },
    )
}
```

## **`UiModule` – Dependency Injection**

**Purpose:** registers the feature with the app’s navigation graph.

<aside>
💡

- Provide a **unique** `provide…` function names to avoid clashes inside the app’s root `UiComponent.kt`
- `vmFactory` is auto-generated when the `ViewModel` is marked with `@Inject`
- You can add multiple `composable<Route>` blocks inside one `NavGraphEntry`.

</aside>

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

---

Need inspiration? See **PlaygroundViewModel** for an advanced example that collects StateFlows to reflect loading state
dynamically.
