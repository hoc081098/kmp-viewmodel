# Multiplatform

## Supported targets

- `android`.
- `jvm` (must add `kotlinx-coroutines-swing`/`kotlinx-coroutines-javafx` to your dependencies to
  make sure `Dispatchers.Main` available).

> [!NOTE]
> If you are targeting `Desktop` and:
>   - not using `JetBrains Compose Multiplatform`, you should provide the dependency `org.jetbrains.kotlinx:kotlinx-coroutines-swing` **or** `org.jetbrains.kotlinx:kotlinx-coroutines-javafx`.
>   - using `JetBrains Compose Multiplatform`, you should provide `org.jetbrains.kotlinx:kotlinx-coroutines-swing`.
>
> Because the `ViewModel.viewModelScope` depends on `Dispatchers.Main` provided by that libraries on Desktop.

- `js` (`IR`).
- `Darwin` targets:
  - `iosArm64`, `iosX64`, `iosSimulatorArm64`.
  - `watchosArm32`, `watchosArm64`, `watchosX64`, `watchosSimulatorArm64`.
  - `tvosX64`, `tvosSimulatorArm64`, `tvosArm64`.
  - `macosX64`, `macosArm64`.
