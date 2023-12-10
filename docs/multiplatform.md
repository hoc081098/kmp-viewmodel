# Multiplatform

## Supported targets

- `android`.
- `jvm` (must add `kotlinx-coroutines-swing`/`kotlinx-coroutines-javafx` to your dependencies to
  make sure `Dispatchers.Main` available).

> [!NOTE]  
> If you are targeting `Desktop` and not using `JetBrains Compose Multiplatform`, you should provide the dependency `org.jetbrains.kotlinx:kotlinx-coroutines-swing` **or** `org.jetbrains.kotlinx:kotlinx-coroutines-javafx`,
> the `ViewModel.viewModelScope` depends on `Dispatchers.Main` provided by this library on Desktop.
> 
> If you are using `JetBrains Compose Multiplatform` and targeting `Desktop`, you should provide `org.jetbrains.kotlinx:kotlinx-coroutines-swing`.
  
- `js` (`IR`).
- `Darwin` targets:
  - `iosArm64`, `iosX64`, `iosSimulatorArm64`.
  - `watchosArm32`, `watchosArm64`, `watchosX64`, `watchosSimulatorArm64`.
  - `tvosX64`, `tvosSimulatorArm64`, `tvosArm64`.
  - `macosX64`, `macosArm64`.
