# Swift interop

## `kmp-viewmodel` and `Swift` interoperability

`kmp-viewmodel` can be exported and used in `Swift` as usual, however there are few limitations.

### Exposing `kmp-viewmodel` sources to Swift

KotlinX Coroutines Flow are Kotlin interfaces with generic types.
Since generics for interfaces
are [not exported](https://kotlinlang.org/docs/reference/native/objc_interop.html#generics)
to `Swift`,
`kmp-viewmodel` provides `Flow` wrapper classes.

You can wrap `Flow`s sources using corresponding `wrap()` extension functions:

- `Flow<T: Any>.wrap(): NonNullFlowWrapper<T>`
- `Flow<T>.wrap(): NullableFlowWrapper<T>`
- `StateFlow<T: Any>.wrap(): NonNullStateFlowWrapper<T>`
- `StateFlow<T>.wrap(): NullableStateFlowWrapper<T>`

Example:

```kotlin
// Kotlin code
class SharedViewModel : ViewModel() {
  private val _state = MutableStateFlow(State())
  val stateFlow: NonNullStateFlowWrapper<State> = _state.wrap()
}
```

### Expose `kmp-viewmodel` and `kotlinx-coroutines-core` to `Darwin` native side

```kotlin
// Cocoapods
kotlin {
  cocoapods {
    [...]
    framework {
      baseName = "shared"
      export("io.github.hoc081098:kmp-viewmodel:0.8.0") // required to expose the classes to iOS.
      export("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
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
        export("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
      }
    }
  }
}
```

### Using wrappers in Swift

Flow wrappers can be used in Swift as usual:

```Swift
// Swift code
func foo() {
  let viewModel = SharedViewModel()

  val closable = viewModel.stateFlow.subscribe(
    scope: viewModel.viewModelScope,
    onValue: { state in
      // do something with state
    }
  )

  // At some point later
  closable.close()
}
```

If Kotlin Code does not wrap `Flow`s to `FlowWrapper`s, we can also wrap them in Swift code.

```Swift
// Swift code
let nonNull = NonNullFlowWrapperKt.wrap(self) as! NonNullFlowWrapper<T>
let nullable = NullableFlowWrapperKt.wrap(self) as! NullableFlowWrapper<T>
...
```

### Combine interop

Please
check [kotlinxCoroutinesFlowExtensions.swift](https://github.com/hoc081098/kmp-viewmodel/blob/master/sample/iosApp/iosApp/Utils/kotlinxCoroutinesFlowExtensions.swift)
.

### RxSwift interop

Please
check [kotlinxCoroutinesFlow+RxSwift.swift](https://github.com/hoc081098/kmp-viewmodel/blob/master/sample/iosApp/iosApp-RxSwift/Utils/kotlinxCoroutinesFlow%2BRxSwift.swift).
