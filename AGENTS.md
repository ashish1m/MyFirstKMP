# AGENTS.md — Developer & AI Agent Guide for MyFirstKMP

This file provides comprehensive context, architectural guidelines, and build commands for AI agents and developers working on the **MyFirstKMP** repository.

---

## 1. Project Overview & Tech Stack

**MyFirstKMP** is a Kotlin Multiplatform (KMP) project powered by **Compose Multiplatform** for shared UI across multiple targets:
- **Android** (Phone / Tablet / Emulator)
- **Desktop** (JVM / Swing / Desktop target)
- **Web** (Kotlin/Wasm & Kotlin/JS)
- **iOS** (SwiftUI entry point consuming KMP shared framework)

### Core Technologies
- **Language**: Kotlin 2.4.21 (JVM Target 11)
- **UI Framework**: Jetpack / Compose Multiplatform 1.12.2 with Material 3
- **Android SDK**: Compile SDK 37, Target SDK 37, Min SDK 30, AGP 9.1.1
- **Dependency Management**: Gradle Version Catalog (`gradle/libs.versions.toml`)
- **Resource Management**: Compose Resources (`org.jetbrains.compose.components:components-resources`)

---

## 2. Repository Structure

```
MyFirstKMP/
├── androidApp/          # Android application module (thin wrapper over :shared)
├── desktopApp/          # Desktop JVM application entry point (main.kt)
├── webApp/              # Web application entry point (WasmJs and JS targets)
├── iosApp/              # iOS Xcode project and SwiftUI entry point
├── shared/              # Core KMP module containing shared UI & business logic
│   └── src/
│       ├── commonMain/  # Cross-platform code & shared Compose components
│       │   └── kotlin/com/example/myfirstkmp/
│       │       ├── ui/         # Presentation Layer (Composables, ViewModels, UI State)
│       │       ├── domain/     # Domain Layer (Domain Models, Use Cases, Repository Interfaces)
│       │       └── data/       # Data Layer (Repository Implementations, Data Sources, Mappers)
│       ├── commonTest/  # Unit tests common across all platforms
│       ├── androidMain/ # Android-specific implementations & resources
│       ├── jvmMain/     # Desktop-specific implementations
│       ├── jsMain/      # Kotlin/JS-specific implementations
│       ├── wasmJsMain/  # Kotlin/Wasm-specific implementations
│       └── iosMain/     # iOS-specific implementations & framework export
├── gradle/
│   └── libs.versions.toml # Centralized dependency version catalog
├── build.gradle.kts     # Root build script
├── settings.gradle.kts  # Module inclusions & repository configuration
└── README.md            # Standard project readme
```

---

## 3. Architecture & Guidelines for Agents

### MVVM + Repository Pattern with Clean Architecture
All new features and refactoring **MUST** adhere to Clean Architecture principles using the **MVVM + Repository** pattern in `:shared/src/commonMain/kotlin/com/example/myfirstkmp/`:

```
┌────────────────────────────────────────────────────────┐
│                   Presentation Layer                   │
│   Composable UI <───(StateFlow)─── ViewModel           │
└───────────────────────────┬────────────────────────────┘
                            │ (Calls UseCases / Repos)
┌───────────────────────────▼────────────────────────────┐
│                      Domain Layer                      │
│   Domain Models  │  Use Cases  │  Repository Interface │
└───────────────────────────▲────────────────────────────┘
                            │ (Implements Interface)
┌───────────────────────────┴────────────────────────────┘
│                       Data Layer                       │
│   Repository Impl  │  Data Sources  │  Data Mappers    │
└────────────────────────────────────────────────────────┘
```

1. **Presentation Layer (`ui/`)**:
   - **Composables**: Pure, stateless UI functions where possible. Observe state from ViewModels.
   - **ViewModels**: Extend `androidx.lifecycle.ViewModel`. Manage UI logic and state. Expose UI state as `StateFlow<UiState>`.
   - **UI State**: Immutable data classes or sealed interfaces/classes representing explicit state (e.g., `Loading`, `Success`, `Error`).

2. **Domain Layer (`domain/`)**:
   - **Domain Models**: Pure Kotlin data objects representing core business entities (independent of database or network schemas).
   - **Repository Interfaces**: Abstract contracts defining operations needed by the domain/presentation layer.
   - **Use Cases / Interactors**: Optional encapsulation of specific business logic functions (e.g., `GetGreetingUseCase`).

3. **Data Layer (`data/`)**:
   - **Repository Implementations**: Implement domain repository interfaces. Coordinate data retrieval from local or remote sources.
   - **Data Sources**: Handle raw data operations (e.g., Ktor HTTP client, Room DB, Settings/DataStore, or platform-specific APIs).
   - **Mappers**: Convert data layer DTOs / models into pure domain models.

---

## 4. SOLID Principles Enforcement

Every contribution must respect SOLID design principles:

- **S — Single Responsibility Principle**:
  - ViewModels handle UI state & presentation logic only.
  - Repositories handle data orchestration & caching.
  - Data Sources handle API or database interactions.
- **O — Open/Closed Principle**:
  - Design components for extension via interfaces without modifying existing tested code.
- **L — Liskov Substitution Principle**:
  - Implementations of repository interfaces (e.g., `RealGreetingRepository`, `FakeGreetingRepository` for tests) must be completely interchangeable without breaking caller logic.
- **I — Interface Segregation Principle**:
  - Prefer small, focused interfaces over monolithic ones.
- **D — Dependency Inversion Principle**:
  - Presentation and Domain layers depend on repository **interfaces**, never concrete repository or data source classes.
  - Inject dependencies (via constructor injection or factory patterns) into ViewModels and Repositories.

---

## 5. KMP Platform Abstractions & Best Practices

1. **Shared Code First**: Place non-platform-specific UI composables, ViewModels, Domain models, and Repositories in `:shared/src/commonMain/kotlin`.
2. **Platform Abstractions (`expect` / `actual`)**:
   - Use `expect` declarations in `commonMain` for platform-dependent features.
   - Provide corresponding `actual` implementations in `androidMain`, `jvmMain`, `iosMain`, `jsMain`, and `wasmJsMain`.
3. **Dependency Management**:
   - Never hardcode dependency versions in `build.gradle.kts` files. Always define them in `gradle/libs.versions.toml`.
4. **App Modules as Thin Entry Points**:
   - `:androidApp`, `:desktopApp`, `:webApp`, and `iosApp` should remain lean wrappers whose primary job is invoking shared composables (e.g., `App()`) and initializing platform-specific contexts.
5. **State & Lifecycle Management**:
   - Use `androidx.lifecycle.viewmodelCompose` and `androidx.lifecycle.runtimeCompose` for state management in `commonMain`.

---

## 6. Build, Run, and Test Commands

### Build Commands
- **Assemble All**: `./gradlew assemble`
- **Android App**: `./gradlew :androidApp:assembleDebug`
- **Desktop App**: `./gradlew :desktopApp:assemble`
- **Web App**: `./gradlew :webApp:build`

### Run Commands
- **Android App**: Deploy `:androidApp` via IDE runner or `./gradlew :androidApp:installDebug`
- **Desktop App**:
  - Run standard: `./gradlew :desktopApp:run`
  - Hot reload mode: `./gradlew :desktopApp:hotRun --auto`
- **Web App**:
  - Wasm target (modern browsers): `./gradlew :webApp:wasmJsBrowserDevelopmentRun`
  - JS target (legacy browsers): `./gradlew :webApp:jsBrowserDevelopmentRun`
- **iOS App**: Open `iosApp/iosApp.xcodeproj` in Xcode and execute target.

### Testing Commands
- **Run All Shared Unit Tests**: `./gradlew :shared:allTests`
- **Android Host Unit Tests**: `./gradlew :shared:testAndroidHostTest`
- **Desktop (JVM) Tests**: `./gradlew :shared:jvmTest`
- **Web Tests**:
  - Wasm: `./gradlew :shared:wasmJsTest`
  - JS: `./gradlew :shared:jsTest`
- **iOS Tests**: `./gradlew :shared:iosSimulatorArm64Test`

---

## 7. Coding Standards & Conventions

- **Package Name**: `com.example.myfirstkmp`
- **Formatting**: Standard Kotlin style conventions (4 spaces indentation, idiomatic Kotlin).
- **Resources**: Place shared drawables, strings, fonts, and assets in `shared/src/commonMain/composeResources/`. Access them using `Res` (e.g., `Res.drawable.compose_multiplatform`).
- **Compatibility**: Ensure JVM target is set to Java 11 across build scripts (`jvmTarget = JvmTarget.JVM_11`).
