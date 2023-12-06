# Change Log

## [0.6.0] - TBD

### Update dependencies

- Supports for [Kotlin `1.9.21`.](https://github.com/JetBrains/kotlin/releases/tag/v1.9.21).
- [AndroidX Lifecycle `2.6.2`.](https://developer.android.com/jetpack/androidx/releases/lifecycle#2.6.2).
- [Jetpack Compose `1.5.11`](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.5.11)

### viewmodel

- `MutableCreationExtras` has been renamed to `MutableCreationExtrasBuilder`,
  and it does not inherit from `CreationExtras` anymore.
  Because of this, a new method
  `MutableCreationExtrasBuilder.asCreationExtras()` has been introduced can be used to convert
  a builder back to `CreationExtras` as needed.

  > NOTE: `buildCreationExtras` and `CreationExtras.edit` methods are still the same as before.

  ```kotlin
  // Old version (0.5.0)
  val creationExtras: CreationExtras = MutableCreationExtras().apply {
    // ...
  }

  // New version (0.6.0): `MutableCreationExtras` does not inherit from `CreationExtras` anymore.
  val creationExtras: CreationExtras = MutableCreationExtrasBuilder().apply {
    // ...
  }.asCreationExtras() // <--- asCreationExtras: convert a builder back to `CreationExtras` as needed.
  ```

  > FOR MORE INFORMATION: With Kotlin 1.9.20, an expect with default arguments are no longer
  permitted when an actual is a typealias
  > (see [KT-57614](https://youtrack.jetbrains.com/issue/KT-57614)),
  > we cannot
  use `actual typealias MutableCreationExtras = androidx.lifecycle.viewmodel.MutableCreationExtras`.
  > So we have to use wrapper class instead.

## [0.5.0] - Sep 27, 2023

### Update dependencies

- Kotlin `1.9.0`.
- AndroidX Lifecycle `2.6.1`.
- KotlinX Coroutines `1.7.3`.
- Android Gradle Plugin `8.1.0`.

### viewmodel

- Add `ViewModelStore` and `ViewModelStoreOwner`.
- Add `ViewModelFactory` and `VIEW_MODEL_KEY`.
- Add `CreationExtras` and `CreationExtrasKey`.
- Add `buildCreationExtras` and `CreationExtras.edit`.
- Add `ViewModel.isCleared()` method to check if the `ViewModel` is cleared, _only available on
  non-Android targets_.
- Add `MainThread` (moved from `viewmodel-savedstate` module).

### viewmodel-savedstate

- Remove `MainThread` (moved to `viewmodel` module).
- Add `SavedStateHandleFactory` interface.
- Add `SAVED_STATE_HANDLE_FACTORY_KEY` and `CreationExtras.createSavedStateHandle()`.

### viewmodel-compose

- A new module allows to access `ViewModel`s in **Jetpack Compose Multiplatform**.
  - `kmpViewModel` to retrieve `ViewModel`s in @Composable functions.
  - `LocalSavedStateHandleFactory` and `SavedStateHandleFactoryProvider` to
    get/provide `SavedStateHandleFactory` in @Composable functions.
    It allows integration with any navigation library.
  - `LocalViewModelStoreOwner` and `ViewModelStoreOwnerProvider` to
    get/provide `ViewModelStoreOwner` in @Composable functions.
    It allows integration with any navigation library.
  - `defaultPlatformCreationExtras` and `defaultPlatformViewModelStoreOwner`
    to get the default `CreationExtras` and `ViewModelStoreOwner`,
    which depends on the platform.

-

Dependencies: [Compose Multiplatform 1.5.0](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.5.0).

-

Docs: [0.x Viewmodel-Compose docs](https://hoc081098.github.io/kmp-viewmodel/docs/0.x/viewmodel-compose/).

### Example, docs and tests

- Refactor example code.

-

Add [Compose Multiplatform sample](https://github.com/hoc081098/kmp-viewmodel/tree/master/standalone-sample/kmpviewmodel_compose_sample)
which shares `ViewModel`s and integrates with `Navigation` in Compose Multiplatform.

-

Add [Compose Multiplatform KmpViewModel KMM Unsplash Sample](https://github.com/hoc081098/Compose-Multiplatform-KmpViewModel-KMM-Unsplash-Sample),
a KMP template of the Unsplash App using Compose multiplatform for Android, Desktop, iOS.
Share everything including data, domain, presentation, and UI.

- Add more docs: [0.x docs](https://hoc081098.github.io/kmp-viewmodel/docs/0.x).
- Add more tests.

## [0.4.0] - Apr 7, 2023

### Changed

#### Update dependencies

- Kotlin `1.8.10`.
- Target `Java 11`.
- Touchlab Stately `1.2.5`.
- AndroidX Lifecycle `2.6.0`.
- Android Gradle Plugin `7.4.2`.

#### Flow wrappers

- Add `NonNullStateFlowWrapper` and `NullableFlowWrapper` to common source set.

- Move all `Flow` wrappers to common source set.
  Previously, they were only available for `Darwin targets` (`iOS`, `macOS`, `tvOS`, `watchOS`).

- Add `Flow.wrap()` extension methods to wrap `Flow`s sources:
  - `Flow<T: Any>.wrap(): NonNullFlowWrapper<T>`.
  - `Flow<T>.wrap(): NullableFlowWrapper<T>`.
  - `StateFlow<T: Any>.wrap(): NonNullStateFlowWrapper<T>`.
  - `StateFlow<T>.wrap(): NullableStateFlowWrapper<T>`.

  In common code, you can use these methods to wrap `Flow` sources and use them in Swift code
  easily.
  ```kotlin
  // Kotlin code
  data class State(...)

  class SharedViewModel : ViewModel() {
    private val _state = MutableStateFlow(State(...))
    val stateFlow: NonNullStateFlowWrapper<State> = _state.wrap()
  }
  ```
  ```swift
  // Swift code
  @MainActor class IosViewModel: ObservableObject {
    private let vm: SharedViewModel

    @Published private(set) var state: State

    init(viewModel: SharedViewModel) {
      vm = viewModel

      state = vm.stateFlow.value       //  <--- Use `value` property with type safety (do not need to cast).
      vm.stateFlow.subscribe(          //  <--- Use `subscribe(scope:onValue:)` method directly.
        scope: vm.viewModelScope,
        onValue: { [weak self] in self?.state = $0 }
      )
    }

    deinit { vm.clear() }
  }
  ```

#### Example, docs and tests

- Refactor example code.
- Add more docs: [0.x docs](https://hoc081098.github.io/kmp-viewmodel/docs/0.x).
- Add more tests.

## [0.3.0] - Mar 18, 2023

### Added

- Add `NonNullFlowWrapper` and `NullableFlowWrapper`, that are wrappers for `Flow`s
  that provides a more convenient API for subscribing to the `Flow`s on `Darwin targets` (`iOS`
  , `macOS`, `tvOS`, `watchOS`)
  ```kotlin
  // Kotlin code
  val flow: StateFlow<Int>
  ```
  ```swift
  // Swift code
  NonNullFlowWrapper<KotlinInt>(flow: flow).subscribe(
    scope: scope,
    onValue: { print("Received ", $0) }
  )
  ```

### Changed

- Add more example, refactor example code.
- Add more docs: [0.x docs](https://hoc081098.github.io/kmp-viewmodel/docs/0.x).
- Add more tests.
- Gradle `8.0.2`.
- Dokka `1.8.10`.

## [0.2.0] - Mar 5, 2023

### Added

- Add `kmp-viewmodel-savedstate` artifact. This artifact brings:
  - [Android Parcelable](https://developer.android.com/reference/android/os/Parcelable) interface.
  - The `@Parcelize` annotation
    from [kotlin-parcelize](https://developer.android.com/kotlin/parcelize) compiler plugin.
  - [SavedStateHandle](https://developer.android.com/reference/androidx/lifecycle/SavedStateHandle)
    class.

  to Kotlin Multiplatform, so they can be used in common code.
  This is typically used for state/data preservation
  over [Android configuration changes](https://developer.android.com/guide/topics/resources/runtime-changes)
  and [system-initiated process death](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-savedstate)
  , when writing common code targeting Android.

### Changed

- Add more example, refactor example code.
- Add more docs: [0.x docs](https://hoc081098.github.io/kmp-viewmodel/docs/0.x).

## [0.1.0] - Feb 18, 2023

### Changed

- Make `ViewModel.viewModelScope` public.

### Added

- Add an `ViewModel.addCloseable` API and a new constructor
  overload `constructor(vararg closeables: Closeable)`,
  that allow you to add one or more `Closeable` objects to the `ViewModel`
  that will be closed when the `ViewModel` is cleared without requiring any manual work
  in `onCleared()`.

## [0.0.1] - Feb 11, 2023

- Initial release.

[Unreleased]: https://github.com/hoc081098/kmp-viewmodel/compare/0.5.0...HEAD

[0.5.0]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.5.0

[0.4.0]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.4.0

[0.3.0]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.3.0

[0.2.0]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.2.0

[0.1.0]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.1.0

[0.0.1]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.0.1
