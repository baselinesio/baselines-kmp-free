# Architecture: Domain

The **domain layer** represents the **core business logic** of your application — the part that defines how your
system behaves according to the rules and requirements **specific to your product or business**.
Unlike other layers (like data or UI), the domain layer is platform-agnostic and independent of frameworks or
infrastructure.

Think of it as the **"what"** your app does, not **"how"** it’s displayed or where the data comes from.

---

## What Belongs in the Domain Layer?

You should place code here if it:

- Encapsulates **business rules or decisions**.
- Coordinates **multiple operations** across repositories or data sources.
- Should be **reused** across different layers (e.g., ViewModel, background tasks).
- It is **independent of Android/iOS/UI or DB frameworks**.

Examples:

- Authentication rules.
- User eligibility checks.
- Subscription billing logic.
- Health metric calculations.
- Decision-making policies (e.g., "Should this reminder be triggered now?").

## Best Practices

- **Use pure Kotlin**: No Android, no UI toolkit, no framework dependencies.
- **Favor immutability**: Inputs and outputs should be value types where possible.
- **Be deterministic**: Given the same input, output should be predictable.
- **Keep it testable**: Domain logic should be easy to unit test without mocks or setup code.
- **Define clear interfaces** for external dependencies (repositories, services).
- **Group logic in use-cases** instead of spreading across models or utils.

## Core Building Blocks: `Worker` and `Observer`

### `Observer<P, R>`

Use `Observer` when you need to **observe ongoing changes** in business state or data. It wraps a `Flow<R>` stream and
serves as the reactive counterpart to `Worker`.

```kotlin
abstract class Observer<in P, R> {
    suspend operator fun invoke(params: P): Flow<R> = create(params)
    protected abstract suspend fun create(params: P): Flow<R>
}
```

**Example:**

```kotlin
@Inject
class ObserveSleepState(
    private val repository: SleepRepository
) : Observer<Unit, SleepData>() {
    override suspend fun create(params: Unit): Flow<SleepData> {
        return repository.observeSleep()
    }
}

// Usage in ViewModel
private val sleepFlow = observableState(null) { observeSleepState(Unit) }

@Composable
override fun state(): UiState {
    val sleepState by sleepFlow.collectAsStateWithLifecycle()
    return UiState(sleepState) {
        /* UI events handling */
    }
}
```

### `Worker<P, R>`

Use `Worker` when you need to **execute a single business operation** that returns a result or fails. The result is
wrapped in a safe `Result<R>` block, so exceptions inside `doWork` won’t crash the app — they can be handled explicitly
in the UI.

```kotlin
abstract class Worker<in P, R> {
    suspend operator fun invoke(params: P): Result<R> = runCatching {
        doWork(params)
    }.onFailure {
        Logger.e(it) { "Error while executing a domain worker." }
    }

    protected abstract suspend fun doWork(params: P): R
}
```

**Example:**

```kotlin
@Inject
class FetchUserData(
    private val repository: UserRepository
) : Worker<UserId, User>() {
    override suspend fun doWork(params: UserId): User {
        return repository.getUserById(params)
    }
}

// Usage in ViewModel
val result = fetchUserData(userId)
result.onSuccess { user -> show(user) }
    .onFailure { error -> showError(error) }
```

<aside>
💡

`Worker` is safe by design. Even if your domain logic crashes inside `doWork`, it’s caught and returned
as a `Result.Failure`, keeping UI layer responsible for rendering error state.

</aside>

## Summary

| Task                         | Use                                     |
|------------------------------|-----------------------------------------|
| One-shot business operation  | `Worker`                                |
| Continuous state observation | `Observer`                              |
| External dependencies        | Inject via interfaces (e.g. Repository) |
| Domain logic execution       | ViewModel or coordination layer         |

---

> 💡 Rule of thumb: if it would break your business if the logic was wrong — it probably belongs in the domain layer.
>
