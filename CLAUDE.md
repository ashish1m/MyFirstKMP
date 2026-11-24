# CLAUDE.md

> **Source of Truth**: Refer to [`AGENTS.md`](./AGENTS.md) for full project architecture, build commands, guidelines, and standards.

## Project Guidelines & Context Summary

All AI interactions and development work in this codebase must strictly adhere to the instructions and specifications in [`AGENTS.md`](./AGENTS.md).

### Primary References in AGENTS.md
- **Architecture**: Clean Architecture with MVVM + Repository pattern located in `:shared/src/commonMain/kotlin/com/example/myfirstkmp/` (`ui/`, `domain/`, `data/`).
- **Principles**: SOLID design principles (Dependency Inversion, Single Responsibility, Interface Segregation, etc.).
- **Build & Run**: Standard Gradle commands for Android, Desktop, Web (Wasm/JS), and iOS targets.
- **Testing**: `./gradlew :shared:allTests` and platform-specific test tasks.
- **Dependencies**: All versions managed centrally in `gradle/libs.versions.toml`.
