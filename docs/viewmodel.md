# ViewModel

[![maven-central](https://img.shields.io/maven-central/v/io.github.hoc081098/kmp-viewmodel)](https://search.maven.org/search?q=g:io.github.hoc081098%20kmp-viewmodel)

## 1. Add dependency

- Add `mavenCentral()` to `repositories` list in `build.gradle.kts`/`settings.gradle.kts`.

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
  [...]
  repositories {
    mavenCentral()
    [...]
  }
}
```

- Add dependency to `build.gradle.kts` of your shared module (must use `api` configuration).

```kotlin
// build.gradle.kts
kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api("io.github.hoc081098:kmp-viewmodel:0.8.0")
      }
    }
  }
}
```

- Expose `kmp-viewmodel` to `Darwin` native side.

```kotlin
// Cocoapods
kotlin {
  cocoapods {
    [...]
    framework {
      baseName = "shared"
      export("io.github.hoc081098:kmp-viewmodel:0.8.0") // required to expose the classes to iOS.
    }
  }
}

// -- OR --

// Kotlin/Native as an Apple framework
kotlin {
  ios {
    binaries {
      framework {
        baseName = "shared"
        export("io.github.hoc081098:kmp-viewmodel:0.8.0") // required to expose the classes to iOS.
      }
    }
  }
}
```

<details>
<summary>Snapshots of the development version are available in Sonatype's snapshots repository.</summary>
<p>

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
  repositories {
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
    [...]
  }
}

// build.gradle.kts
dependencies {
  api("io.github.hoc081098:kmp-viewmodel:0.7.2-SNAPSHOT")
}
```

</p>
</details>

## 2. Overview

```kotlin
public expect abstract class ViewModel {
  public constructor()
  public constructor(vararg closeables: Closeable)

  public val viewModelScope: CoroutineScope
  public fun addCloseable(closeable: Closeable)

  protected open fun onCleared()
}
```

- The ViewModel has a `viewModelScope` which is a `CoroutineScope` that is cancelled when the
  ViewModel
  is cleared. Once the ViewModel is cleared, all coroutines launched in this scope will be
  cancelled.

- `addCloseable` method is used to add `Closeable` that will be closed directly before `onCleared`
  is called.

- `onCleared` is called when the ViewModel is cleared, you can override this method to do some clean
  up work.
  But it is recommended to use the `addCloseable` method instead of overriding `onCleared` method.


## 3. Create your `ViewModel` in `commonMain` source set.

```kotlin
import com.hoc081098.kmp.viewmodel.Closeable
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.wrapper.NonNullFlowWrapper
import com.hoc081098.kmp.viewmodel.wrapper.NonNullStateFlowWrapper
import com.hoc081098.kmp.viewmodel.wrapper.wrap

class ProductsViewModel(
  private val getProducts: GetProducts,
) : ViewModel() {
  private val _eventChannel = Channel<ProductSingleEvent>(Int.MAX_VALUE)
  private val _actionFlow = MutableSharedFlow<ProductsAction>(Int.MAX_VALUE)

  val stateFlow: NonNullStateFlowWrapper<ProductsState>
  val eventFlow: NonNullFlowWrapper<ProductSingleEvent> = _eventChannel.receiveAsFlow().wrap()

  init {
    // Close _eventChannel when ViewModel is cleared.
    addCloseable(_eventChannel::close)

    stateFlow = _actionFlow
      .transformToStateFlow()
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ProductsState.INITIAL,
      )
      .wrap()
  }

  // Do business logic here, to convert `ProductsAction`s to `ProductsState`s.
  private fun SharedFlow<ProductsAction>.transformToStateFlow(): Flow<ProductsState> = TODO()

  fun dispatch(action: ProductsAction) {
    _actionFlow.tryEmit(action)
  }
}
```

## 4. Use common `ViewModel` in each platform.

### 4.1. Android

Use the `ViewModel` as a normal `AndroidX Lifecycle ViewModel`.

```kotlin
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProductsScreen(
  viewModel: ProductsViewModel = viewModel(),
) {
  val state by viewModel.stateFlow.collectAsStateWithLifecycle()
  // Render UI based on state.
}
```

### 4.2. non-Android

- Make sure that you call `clear()` on your ViewModel when it's no longer needed,
  to properly cancel the `CoroutineScope` and close resources.
  For example, you should call `clear()` in `deinit` block when using `ViewModel` in `Darwin`
  targets (`ios`, `macos`, `tvos`, `watchos`).

- In addition, you should create a wrapper of the common `ViewModel` in each platform and use
  flow wrappers provided by this library (`NonNullFlowWrapper`, `NullableFlowWrapper`
  , `NonNullStateFlowWrapper`, `NullableStateFlowWrapper`), to consume
  the common `ViewModel` easily and safely.

> For more details, please
> check [kmp viewmodel sample](https://github.com/hoc081098/kmp-viewmodel/tree/master/sample).

The below example is using `NonNullStateFlowWrapper.subscribe(scope:onValue:)` method
to consume the `Flow`s in `Darwin` targets (Swift language).

```Swift
@MainActor
class IosProductsViewModel: ObservableObject {
  private let commonVm: ProductsViewModel

  @Published private(set) var state: ProductsState

  init(commonVm: ProductsViewModel) {
    self.commonVm = commonVm

    self.state = self.commonVm.stateFlow.value
    self.commonVm.stateFlow.subscribe(
      scope: self.commonVm.viewModelScope,
      onValue: { [weak self] in self?.state = $0 } // use weak self to avoid retain cycle.
    )
  }

  func dispatch(action: ProductsAction) { self.commonVm.dispatch(action: action) }

  deinit { self.commonVm.clear() }
}
```
