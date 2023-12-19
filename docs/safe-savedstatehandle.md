# Type-safe access to SavedStateHandle

- Module `kmp-viewmodel-savedstate` provides the type-safe API
  that allows you to access `SavedStateHandle` in a type-safe way.

- All API are available on all platforms and are located in the `com.hoc081098.kmp.viewmodel.safe`
  package.

- There are 2 types of keys:

  - `NonNullSavedStateHandleKey<T>`: key for values stored in `SavedStateHandle`.
    The type of the value associated with this key is `T` (non-null).
  - `NullableSavedStateHandleKey<T>`: key for values stored in `SavedStateHandle`.
    The type of the value associated with this key is `T?` (nullable, `null` is valid).

- Using `SavedStateHandle.safe` extension function to access `SavedStateHandle` in a type-safe way.
  It accepts a lambda with a single parameter of type `SafeSavedStateHandle`.
  `SafeSavedStateHandle` provides methods:
  - `public inline operator fun <T : Any> get(key: NonNullSavedStateHandleKey<T>): T`.
  - `public inline operator fun <T : Any> get(key: NullableSavedStateHandleKey<T>): T?`.
  - `public inline operator fun <T : Any> set(key: NonNullSavedStateHandleKey<T>, value: T): Unit`.
  - `public inline operator fun <T : Any> set(key: NullableSavedStateHandleKey<T>, value: T?): Unit`.
  - `public inline fun <T : Any> getStateFlow(key: NonNullSavedStateHandleKey<T>): StateFlow<T>`.
  - `public inline fun <T : Any> getStateFlow(key: NullableSavedStateHandleKey<T>): StateFlow<T?>`.
  - `public inline fun <T : Any> remove(key: NullableSavedStateHandleKey<T>)`.

## 1. Define your `SavedStateHandle` keys

```kotlin
import com.hoc081098.kmp.viewmodel.safe.NonNullSavedStateHandleKey
import com.hoc081098.kmp.viewmodel.safe.string

private val searchTermKey: NonNullSavedStateHandleKey<String> = NonNullSavedStateHandleKey.string(
  key = "search_term",
  defaultValue = ""
)
private val userIdKey: NullableSavedStateHandleKey<String> = NullableSavedStateHandleKey.string("user_id")
```

## 2. Use `SavedStateHandle.safe` extension function

```kotlin
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.safe.NonNullSavedStateHandleKey
import com.hoc081098.kmp.viewmodel.safe.NullableSavedStateHandleKey
import com.hoc081098.kmp.viewmodel.safe.safe
import com.hoc081098.kmp.viewmodel.safe.string
import com.hoc081098.kmp.viewmodel.wrapper.NonNullStateFlowWrapper
import com.hoc081098.kmp.viewmodel.wrapper.NullableStateFlowWrapper
import com.hoc081098.kmp.viewmodel.wrapper.wrap

class SearchViewModel(
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
  internal val searchTermStateFlow: NonNullStateFlowWrapper<String> = savedStateHandle
    .safe { it.getStateFlow(searchTermKey) }
    .wrap()

  internal val userIdStateFlow: NullableStateFlowWrapper<String?> = savedStateHandle
    .safe { it.getStateFlow(userIdKey) }
    .wrap()

  internal fun changeSearchTerm(searchTerm: String) {
    savedStateHandle.safe { it[searchTermKey] = searchTerm }
  }

  private fun setUserId(userId: String?) = savedStateHandle.safe { it[userIdKey] = userId }

  companion object {
    private val searchTermKey: NonNullSavedStateHandleKey<String> = NonNullSavedStateHandleKey.string(
      key = "search_term",
      defaultValue = "",
    )
    private val userIdKey: NullableSavedStateHandleKey<String> = NullableSavedStateHandleKey.string("user_id")
  }
}
```
