# Change Log

## [Unreleased]

### Changed

- Kotlin `1.8.10`.
- Target `Java 11`.
- Touchlab Stately `1.2.5`.
- AndroidX Lifecycle `2.6.0`.
- Android Gradle Plugin `7.4.2`.

## [0.3.0] - Mar 18, 2023

### Added

- Add `NonNullFlowWrapper` and `NullableFlowWrapper`, that are wrappers for `Flow`s
  that provides a more convenient API for subscribing to the `Flow`s on `Darwin targets` (`iOS`, `macOS`, `tvOS`, `watchOS`)
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
  that will be closed when the `ViewModel` is cleared without requiring any manual work in `onCleared()`.

## [0.0.1] - Feb 11, 2023

- Initial release.

[Unreleased]: https://github.com/hoc081098/kmp-viewmodel/compare/0.3.0...HEAD
[0.3.0]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.3.0
[0.2.0]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.2.0
[0.1.0]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.1.0
[0.0.1]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.0.1
