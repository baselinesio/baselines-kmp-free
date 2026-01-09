# Baselines: Technical Platform Overview

## What is Baselines

**Baselines** is a **mobile platform foundation** for teams that build mobile applications **repeatably**.

It provides a **reproducible, big-tech–grade project setup and tooling layer** that eliminates starting from scratch,
standardizes architecture and tooling across apps, and dramatically reduces time-to-market and long-term maintenance
cost.

Baselines powers **both Android and iOS** from a single Kotlin Multiplatform codebase, while keeping platform-specific
entry points fully native.

The core idea is simple:  
**build the foundation once, reuse it everywhere, improve it continuously.**

---

## Target Audience

Baselines foundation is designed for:

- **Startups** requiring to start/test multiple mobile MVPs or repeatedly launching mobile products
- **Product & outsourcing studios** creating POCs for clients or delivering many apps per year
- **Internal mobile teams** that want speed without architectural chaos

It is especially valuable for teams that:

- repeatedly recreate the same mobile foundations
- maintain multiple apps with inconsistent architectures
- spend weeks onboarding developers into new projects
- want predictable delivery and cheaper long-term maintenance

---

## Why Baselines Exists

Most mobile teams repeatedly solve the same non-product problems:

- recreating project structure and build logic
- reinventing architecture decisions per app
- reconfiguring analytics, feature flags, encryption, and configs
- onboarding developers into yet another custom setup

This leads to:

- slower launches
- higher maintenance cost
- architectural drift across apps

Baselines turns these recurring problems into a **single standardized platform**, giving teams a **competitive advantage
**:

- launch apps **lightning fast**
- go straight into feature development
- scale teams and products without losing control

---

## Platform Capabilities (High-Level)

Baselines is not a starter kit or a template.  
It is a **platform layer** that sits underneath your product features.

At a high level, it provides:

- A clean, enforced **architecture** shared across all apps
- **Gradle convention plugins** that remove build script boilerplate
- Pre-configured **dependency injection**
- A suite of **advanced internal tooling** typical for big-tech teams
- A reproducible project generator to kick off new apps in minutes

The result: repeatable project creation with predictable outcomes.

---

## Supported Platforms

- **Android** — native entry point, fully supported
- **iOS** — native entry point powered by shared Kotlin Multiplatform code

Shared business logic, UI, and tooling live in Kotlin Multiplatform modules, while each platform keeps its idiomatic
setup.

---

## Architecture Overview

Baselines follows a **clean, layered architecture** optimized for repeatability and long-term scalability.

Core layers:

- **UI** — Compose Multiplatform presentation layer
- **Domain** — pure business logic and interfaces
- **Data** — persistence, storage, and integrations
- **Toolkit** — shared platform tooling and infrastructure
- **App launchers** — Android and iOS entry points

Module boundaries enforce separation of concerns and prevent architectural drift over time.

---

## Build System & Gradle Conventions

Baselines includes **Gradle convention plugins** that encode best practices directly into the build system.

Key benefits:

- Most modules doesn't need anything rather than **dependencies**
- No repeated boilerplate in `build.gradle.kts`
- Centralized dependency and version management
- Consistent configuration across all apps and modules

This makes project setup fast, predictable, and hard to misuse.

---

## Dependency Injection

- Uses **kotlin-inject** as the default DI solution
- Compile-time safe, lightweight, no reflection
- Designed to scale across many modules and features

Planned evolution:

- Migration to **Metro** once Kotlin **2.3.20** becomes publicly available and stable

Dependency injection is part of the platform foundation, not a per-project decision.

---

## Advanced Tooling Included

Baselines ships with **production-grade internal tooling** that teams typically have to build and maintain themselves:

- **Feature tweaks / toggles**  
  Adjust or disable features without redeploying the app. Can be baked by remote configs.

- **Analytics foundation**  
  Centralized, consistent analytics infrastructure across all apps.

- **Encryption & security APIs**  
  Platform-agnostic encryption abstractions for sensitive data.

- **App & feature initializers**  
  Deterministic initialization of app subsystems and features.

- **App configuration system**  
  Environment-aware configs, debug flags, and platform settings.

- **Playground**  
  A safe space to experiment with UI, features, and platform capabilities.

All tooling lives in the `toolkit` layer and evolves independently of product features.

---

## Required Tooling

To work with Baselines, the following tools are required:

| Tool               | Version | Purpose                                                               |
|--------------------|---------|-----------------------------------------------------------------------|
| **Android Studio** | Latest  | Android development and working with shared Kotlin Multiplatform code |
| **Xcode**          | Latest  | Building, running, and debugging the iOS application                  |
| **Python (CLI)**   | 3.x     | Running the Baselines project generator                               |

_No proprietary infrastructure or custom build systems are required._

---

## Quick Start

Creating a new Baselines-powered project takes only a few minutes:

```bash
# 1. Move into a workspace directory
cd ~/projects

# 2. Run the generator
python3 path/to/baselines-kmp/create_baselines.py

# 3. Open in IDEs & Run
open -a "Android Studio" ./MyApp
open ./MyApp/app/ios/MyApp/MyApp.xcodeproj
```
