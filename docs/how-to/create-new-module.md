# How to: Create New Module

This guide outlines the step-by-step process for adding a new module to the project. Android-specific and Multiplatform
module build configs are supported via `baselines` convention plugins.

---

## **1️⃣ Decide Where the Module Belongs**

First, determine which layer your module should live in. This helps maintain a clean architectural separation:

| Layer     | Purpose                                                       |
|-----------|---------------------------------------------------------------|
| `ui`      | User interface logic, navigation, screens, theming            |
| `domain`  | Business logic, use-case orchestration, app rules             |
| `data`    | Repository implementations, network, local storage            |
| `toolkit` | Reusable utilities, wrappers, DI, logging, crypto, time, etc. |

## **2️⃣ Create the Module Directory**

Create a new directory under the corresponding layer. For example:
`ui/profile`

## **3️⃣ Add a `build.gradle.kts` File**

Inside your module directory, create a `build.gradle.kts` file. The setup depends on whether you're creating
a **multiplatform** or an **Android-specific** module.

> 💡 Key Difference: The top-level plugins you apply come from different namespaces:
>
> - **Multiplatform module** - use plugins under `libs.plugins.baselines.multiplatform.*`
> - **Android-only module** - use plugins under `libs.plugins.baselines.android.*`

We’ll break these into separate sections below.

### **🔀 Multiplatform Module Setup**

Use this if your module should compile for both or on of the mobile platforms (Android/iOS).

✅ Step-by-Step:

1. Apply plugins

```kotlin
plugins {
    alias(libs.plugins.baselines.multiplatform.kotlin)
    // IMPORTANT: 
    // Android plugin should be applied, if module also targets Android.
    // Current Android Gradle Plugin (below version 9.0) has an issue with UI Preview rendering that affects UI modules
    // only. To workaround the issue without migrating to new AGP version, replace 
    // `alias(libs.plugins.baselines.multiplatform.android.library)` with `alias(libs.plugins.baselines.android.library)`
    alias(libs.plugins.baselines.multiplatform.android.library)
    // Add to include compose (usually, necessary for the UI modules)
    alias(libs.plugins.baselines.compose)
}
```

2. (Optional) Set android target namespace

```kotlin
// IMPORTANT: make sure to use the correct import. Depending on the applied above plugins, the import will change.
// If you applied `alias(libs.plugins.baselines.android.library)` -> add `import io.baselines.gradle.android.androidLibrary`
// If you applied `alias(libs.plugins.baselines.multiplatform.android.library)` -> add `import io.baselines.gradle.multiplatform.androidLibrary`
androidLibrary("com.example.ui.profile")
```

3. Declare dependencies by target

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            // Common dependencies
        }
        androidMain.dependencies {
            // Android-specific dependencies
        }
        iosMain.dependencies {
            // iOS-specific dependencies
        }
    }
}
```

4. Create source folders

After finishing your`build.gradle.kts`setup, create the appropriate source folders:

- For common code:

```
src/commonMain/kotlin
```

- For platform-specific code:

```
src/androidMain/kotlin
src/iosMain/kotlin
```

Within each source folder, create a directory structure that matches the namespace.

For example, if your namespace is`com.example.ui.`profile, the folder should be:

```
src/commonMain/kotlin/com/example/ui/profile
```

✅ Full Multiplatform example:

```kotlin
import io.baselines.gradle.multiplatform.androidLibrary

plugins {
    alias(libs.plugins.baselines.multiplatform.android.library)
    alias(libs.plugins.baselines.multiplatform.kotlin)
    alias(libs.plugins.baselines.compose)
}

androidLibrary("com.example.ui.profile")

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Common dependencies
        }
        androidMain.dependencies {
            // Android-specific dependencies
        }
        iosMain.dependencies {
            // iOS-specific dependencies
        }
    }
}
```

### **🤖 Android-Only Module Setup**

Use this if your module is meant only for Android (e.g., Android-specific screens, splash behavior, Google services).

✅ Step-by-Step:

1. Apply required plugins

```kotlin
plugins {
    alias(libs.plugins.baselines.android.library)
    // Add to include compose (usually, necessary for the UI modules)
    alias(libs.plugins.baselines.compose)
}
```

2. Set the namespace

```kotlin
import io.baselines.gradle.multiplatform.androidLibrary

androidLibrary("com.example.ui.profile")
```

3. Declare dependencies

```kotlin
dependencies {
    // Android-specific dependencies
}
```

4. Create source folder

Create the following source directory:

```
src/main/kotlin
```

Inside, create a directory structure that matches your namespace:

```
src/main/kotlin/com/example/ui/profile
```

✅ Full Android example

```kotlin
import io.baselines.gradle.android.androidLibrary

plugins {
    alias(libs.plugins.baselines.android.library)
    alias(libs.plugins.baselines.compose)
}

androidLibrary("com.example.ui.profile")

dependencies {
    // Android-specific dependencies
}
```
