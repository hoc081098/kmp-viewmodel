# Change Log

## [0.8.0] - Jun 9, 2024

### Update dependencies

- [Kotlin `2.0.0`](https://github.com/JetBrains/Kotlin/releases/tag/v2.0.0) 🎉.
- [AndroidX Lifecycle `2.8.1`](https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.1).
- [JetBrains Compose Multiplatform `1.6.11`](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.6.11).
- [KotlinX Coroutines `1.8.1`](https://github.com/Kotlin/kotlinx.coroutines/releases/tag/1.8.1).
- [Touchlab Stately `2.0.7`](https://github.com/touchlab/Stately/releases/tag/2.0.7).
- [Koin Core `3.5.6`, Koin Compose `1.1.5`](https://github.com/InsertKoinIO/koin/releases/tag/3.5.6).

### `kmp-viewmodel-savedstate`

- Added `JvmSerializable` - multiplatform reference to Java `java.io.Serializable` interface,
  along with `NonNullSavedStateHandleKey.Companion.serializable` and `NullableSavedStateHandleKey.Companion.serializable`

  ```kotlin
  // Use `JvmSerializable` with enum classes.
  enum class Gender : JvmSerializable {
    MALE,
    FEMALE,
  }

  // Create a `NonNullSavedStateHandleKey` for a serializable type.
  private val genderKey: NonNullSavedStateHandleKey<Gender> = NonNullSavedStateHandleKey.serializable(
    key = "gender",
    defaultValue = Gender.MALE,
  )

  // Use `SavedStateHandle.safe` extension function to access `SavedStateHandle` in a type-safe way.
  val genderStateFlow: NonNullStateFlowWrapper<Gender> = savedStateHandle
    .safe { it.getStateFlow(genderKey) }
    .wrap()
  ```

- Since Kotlin 2.0.0, you must add `"plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=com.hoc081098.kmp.viewmodel.parcelable.Parcelize"`
  as a free compiler argument to able to use `@Parcelize` annotation in the common/shared module (Kotlin Multiplatform module).

  ```kotlin
  // build.gradle.kts
  plugins {
    id("kotlin-parcelize") // Apply the plugin for Android
  }

  // Since Kotlin 2.0.0, you must add the below code to your build.gradle.kts of the common/shared module (Kotlin Multiplatform module).
  kotlin {
    [...] // Other configurations

    targets.configureEach {
      val isAndroidTarget = platformType == org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.androidJvm
      compilations.configureEach {
        compileTaskProvider.configure {
          compilerOptions {
            if (isAndroidTarget) {
              freeCompilerArgs.addAll(
                "-P",
                "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=com.hoc081098.kmp.viewmodel.parcelable.Parcelize",
              )
            }
          }
        }
      }
    }
  }
  ```

## [0.7.1] - March 2, 2024

### `kmp-viewmodel-compose`

- [JetBrains Compose Multiplatform `1.6.0`](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.6.0).
- **New**: Add support for Kotlin/Wasm (`wasmJs` target) 🎉.

### Added `kmp-viewmodel-koject` and `kmp-viewmodel-koject-compose` artifacts

- For more information check out the [docs/0.x/viewmodel-koject-compose](https://hoc081098.github.io/kmp-viewmodel/docs/0.x/viewmodel-koject-compose/)

- The [Koject](https://mori-atsushi.github.io/koject/) dependency are used in `kmp-viewmodel-koject-compose`: `com.moriatsushi.koject:koject-core:1.3.0`.

- **New** The `kmp-viewmodel-koject` artifact provides the integration of `kmp-viewmodel`, `kmp-viewmodel-compose` and `Koject`,
  helps us to retrieve `ViewModel` from the Koject DI container without manually dependency injection.

  ```kotlin
  @Provides
  @Singleton
  class MyRepository

  @Provides
  @ViewModelComponent // <-- To inject SavedStateHandle
  class MyViewModel(
    val myRepository: MyRepository,
    val savedStateHandle: SavedStateHandle,
  ) : ViewModel() {
    // ...
  }

  @Composable
  fun MyScreen(
    viewModel: MyViewModel = kojectKmpViewModel(),
  ) {
    // ...
  }
  ```

### Example, docs and tests

- Add [Compose Multiplatform Koject sample](https://github.com/hoc081098/kmp-viewmodel/tree/master/standalone-sample/kmpviewmodel_compose_koject_sample)
  which shares `ViewModel`s and integrates with `Navigation` in Compose Multiplatform. It uses `Koject` for DI.

## [0.7.0] - Feb 17, 2024

### Update dependencies

- [AndroidX Lifecycle `2.7.0`](https://developer.android.com/jetpack/androidx/releases/lifecycle#2.7.0).
- Android target: update `Compile SDK` and `Target SDK` to `34`.
- [KotlinX Coroutines `1.8.0`](https://github.com/Kotlin/kotlinx.coroutines/releases/tag/1.8.0).

### `kmp-viewmodel` and `kmp-viewmodel-savedstate`

- **New**: Add support for Kotlin/Wasm (`wasmJs` target) 🎉.
- The behavior of `ViewModel.addCloseable(Closeable)` on _non-Android targets_ has been changed to be consistent with _Android target_.
  `ViewModel`'s `addCloseable()` now **immediately closes** the `Closeable` if the `ViewModel` has been cleared.
  **This behavior is the same across all targets ✅**.

### `kmp-viewmodel-koin`

- **Fixed**: `koinViewModelFactory`: `CreationExtras` passed to `ViewModelFactory.create` will now be
  passed to the constructor of the ViewModel if it's requested.

  ```kotlin
  class MyViewModel(val extras: CreationExtras) : ViewModel()
  val myModule: Module = module {
    factoryOf(::MyViewModel)
  }

  val factory = koinViewModelFactory<MyViewModel>(
    scope = KoinPlatformTools.defaultContext().get().scopeRegistry.rootScope,
  )
  val extras = buildCreationExtras { /* ... */ }

  val viewModel: MyViewModel = factory.create(extras)
  viewModel.extras === extras // true <--- `viewModel.extras` is the same as `extras` passed to `factory.create(extras)`
  ```

### Example, docs and tests

- Add more tests to `kmp-viewmodel-compose` (android & jvm), `kmp-viewmodel-koin` (common),
  and `kmp-viewmodel-koin-compose` (common & jvm).

## [0.6.2] - Feb 5, 2024

### Update dependencies

- Updated to [Kotlin `1.9.22`](https://github.com/JetBrains/kotlin/releases/tag/v1.9.22).
- [JetBrains Compose Multiplatform `1.5.12`](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.5.12).
- [Touchlab Stately `2.0.6`](https://github.com/touchlab/Stately/releases/tag/2.0.6).

### Added `kmp-viewmodel-koin` and `kmp-viewmodel-koin-compose` artifacts

- For more information check out the [docs/0.x/viewmodel-koin-compose](https://hoc081098.github.io/kmp-viewmodel/docs/0.x/viewmodel-koin-compose/)

- The Koin dependencies are used in `kmp-viewmodel-koin-compose`:
  - `io.insert-koin:koin-core:3.5.3`.
  - `io.insert-koin:koin-compose:1.1.2`.

- **New** The `kmp-viewmodel-koin` artifact provides the integration of `kmp-viewmodel`, `kmp-viewmodel-compose` and `Koin`,
  helps us to retrieve `ViewModel` from the Koin DI container without manually dependency injection.

  ```kotlin
  class MyRepository

  class MyViewModel(
    val myRepository: MyRepository,
    val savedStateHandle: SavedStateHandle,
    val id: Int,
  ) : ViewModel() {
    // ...
  }

  val myModule: Module = module {
    factoryOf(::MyRepository)
    factoryOf(::MyViewModel)
  }

  @Composable
  fun MyScreen(
    id: Int,
    viewModel: MyViewModel = koinKmpViewModel(
      key = "MyViewModel-$id",
      parameters = { parametersOf(id) }
    )
  ) {
    // ...
  }
  ```

### Added type-safe API for `SavedStateHandle`

- For more information check out the [docs/0.x/viewmodel-savedstate-safe](https://hoc081098.github.io/kmp-viewmodel/docs/0.x/viewmodel-savedstate-safe/)

- **New** The `kmp-viewmodel-savedstate` artifact provides the type-safe API
  that allows you to access `SavedStateHandle` in a type-safe way.

  ```kotlin
  private val searchTermKey: NonNullSavedStateHandleKey<String> = NonNullSavedStateHandleKey.string(
    key = "search_term",
    defaultValue = ""
  )

  // Use `SavedStateHandle.safe` extension function to access `SavedStateHandle` in a type-safe way.
  savedStateHandle.safe { it[searchTermKey] = searchTerm }
  savedStateHandle.safe { it.getStateFlow(searchTermKey) }

  // Or use `SavedStateHandle.safe` extension property to access `SavedStateHandle` in a type-safe way.
  savedStateHandle.safe[searchTermKey] = searchTerm
  savedStateHandle.safe.getStateFlow(searchTermKey)
  ```

### `kmp-viewmodel-compose` artifact

-  **New** Add `rememberViewModelFactory`s to remember the `ViewModelFactory`s in `@Composable` functions.
  They accept `builder: @DisallowComposableCalls CreationExtras.() -> VM`s.

  ```kotlin
  class MyViewModel(savedStateHandle: SavedStateHandle): ViewModel()

  @Composable
  fun MyScreen() {
    val factory: ViewModelFactory<MyViewModel> = rememberViewModelFactory {
      MyViewModel(savedStateHandle = createSavedStateHandle())
    }
    val viewModel: MyViewModel = kmpViewModel(factory = factory)
    // ...
  }
  ```

- **New** Add a new `kmpViewModel` overload that accepts `factory: @DisallowComposableCalls CreationExtras.() -> VM`
  (Previously, it only accepts `factory: ViewModelFactory<VM>`).

  ```kotlin
  class MyViewModel(savedStateHandle: SavedStateHandle): ViewModel()

  @Composable
  fun MyScreen(
    viewModel: MyViewModel = kmpViewModel {
      MyViewModel(savedStateHandle = createSavedStateHandle())
    }
  ) {
    // ...
  }
  ```

> The above `kmpViewModel` uses `rememberViewModelFactory` internally.
> Use `rememberViewModelFactory { ... }` and `kmpViewModel(factory = factory)`
> is the same as using `kmpViewModel { ... }`.

## [0.6.1] - Dec 10, 2023

### viewmodel

- On _non-Android targets_: `ViewModel.viewModelScope` does not use `Dispatchers.Default` as a fallback.
  That means the `CoroutineDispatcher` of `ViewModel.viewModelScope` is `Dispatchers.Main.immediate` or `Dispatchers.Main`.

### Example, docs

- Refactor example code.
- Add **NOTE** about the `kotlinx-coroutines` dependency when targeting `Desktop` (aka. `jvm`).

## [0.6.0] - Dec 8, 2023

### Update dependencies

- Supports for [Kotlin `1.9.21`](https://github.com/JetBrains/kotlin/releases/tag/v1.9.21).
- [AndroidX Lifecycle `2.6.2`](https://developer.android.com/jetpack/androidx/releases/lifecycle#2.6.2).
- [JetBrains Compose Multiplatform `1.5.11`](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.5.11).

### Removed

- Remove **now-unsupported targets**: `iosArm32`, `watchosX86`.

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

  > More details: with Kotlin 1.9.20, an expect with default arguments are no longer
  permitted when an actual is a typealias
  > (see [KT-57614](https://youtrack.jetbrains.com/issue/KT-57614)),
  > we cannot
  use `actual typealias MutableCreationExtras = androidx.lifecycle.viewmodel.MutableCreationExtras`.
  > So we have to use wrapper class instead.

- Update the docs of `ViewModel.viewModelScope` to clarify that the scope is **thread-safe**
  on **both _Android_ and _non-Android targets_**.

- On _non-Android targets_
  - `ViewModel.clear()` method has been refactored to improve the performance.
  - Any `Exception` thrown from `Closeable.close()` will be re-thrown as `RuntimeException`.

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

- Dependencies: [Compose Multiplatform 1.5.0](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.5.0).

- Docs: [0.x Viewmodel-Compose docs](https://hoc081098.github.io/kmp-viewmodel/docs/0.x/viewmodel-compose/).

### Example, docs and tests

- Refactor example code.

- Add [Compose Multiplatform Koin sample](https://github.com/hoc081098/kmp-viewmodel/tree/master/standalone-sample/kmpviewmodel_compose_koin_sample)
  which shares `ViewModel`s and integrates with `Navigation` in Compose Multiplatform.

- Add [Compose Multiplatform KmpViewModel KMM Unsplash Sample](https://github.com/hoc081098/Compose-Multiplatform-KmpViewModel-KMM-Unsplash-Sample),
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

[Unreleased]: https://github.com/hoc081098/kmp-viewmodel/compare/0.8.0...HEAD

[0.8.0]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.8.0

[0.7.1]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.7.1

[0.7.0]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.7.0

[0.6.2]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.6.2

[0.6.1]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.6.1

[0.6.0]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.6.0

[0.5.0]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.5.0

[0.4.0]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.4.0

[0.3.0]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.3.0

[0.2.0]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.2.0

[0.1.0]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.1.0

[0.0.1]: https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.0.1
