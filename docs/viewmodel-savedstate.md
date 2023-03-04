# Get started

This library brings:

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
        api("io.github.hoc081098:kmp-viewmodel-savedstate:0.1.0")
      }
    }
  }
}
```

- Expose `kmp-viewmodel-savedstate` to `Darwin` native side.

```kotlin
// Cocoapods
kotlin {
  cocoapods {
    [...]
    framework {
      baseName = "shared"
      export("io.github.hoc081098:kmp-viewmodel-savedstate:0.1.0") // required to expose the classes to iOS.
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
        export("io.github.hoc081098:kmp-viewmodel-savedstate:0.1.0") // required to expose the classes to iOS.
      }
    }
  }
}
```

- _Optional_: apply `kotlin-parcelize` if you want to use `@Parcelize` annotation to
  generate `Parcelable` implementation for Android.

```kotlin
// build.gradle.kts
plugins {
  id("kotlin-parcelize") // Apply the plugin for Android
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
  api("io.github.hoc081098:kmp-viewmodel-savedstate:0.1.1-SNAPSHOT")
}
```

</p>
</details>

## 2. Overview

```kotlin
public expect class SavedStateHandle {
  public constructor(initialState: Map<String, Any?>)
  public constructor()

  public operator fun contains(key: String): Boolean
  public operator fun <T> get(key: String): T?
  public fun <T> getStateFlow(key: String, initialValue: T): StateFlow<T>
  public fun keys(): Set<String>

  public fun <T> remove(key: String): T?
  public operator fun <T> set(key: String, value: T?): Unit
}
```

The `SavedStateHandle` class provides some methods to get and set data.
On Android, it is a type alias of `androidx.lifecycle.SavedStateHandle`.
On other platforms, it is simply a wrapper of the normal `Map<String, Any?>`.

Because the limitation of Android platform, the data stored in `SavedStateHandle` must be one of the
following types:

| Type/Class support      | Array support  |
|-------------------------|----------------|
| double                  | double[]       |
| int                     | int[]          |
| long                    | long[]         |
| String                  | String[]       |
| byte                    | byte[]         |
| char                    | char[]         |
| CharSequence            | CharSequence[] |
| float                   | float[]        |
| Parcelable              | Parcelable[]   |
| Serializable            | Serializable[] |
| short                   | short[]        |
| SparseArray             |                |
| Binder                  |                |
| Bundle                  |                |
| ArrayList               |                |
| Size (only in API 21+)  |                |
| SizeF (only in API 21+) |                |

If the class does not extend one of those in the above list, consider making the class parcelable
by adding the `@Parcelize` annotation.
See [SavedStateHandle supported types docs](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-savedstate#types)
for more details.

## 3. Usage example

```kotlin
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.parcelable.Parcelable
import com.hoc081098.kmp.viewmodel.parcelable.Parcelize

@Parcelize
data class User(val id: Long, val name: String) : Parcelable

class UserViewModel(
  private val savedStateHandle: SavedStateHandle,
  private val getUserUseCase: suspend () -> User,
) : ViewModel() {
  val user: StateFlow<User?> = savedStateHandle.getStateFlow<User?>(USER_KEY, null)

  fun getUser() {
    viewModelScope.launch {
      try {
        savedStateHandle[USER_KEY] = getUserUseCase()
      } catch (e: CancellationException) {
        throw e
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  private companion object {
    private const val USER_KEY = "user_key"
  }
}
```

> For more details, please
> check [kmp viewmodel sample](https://github.com/hoc081098/kmp-viewmodel/tree/master/sample).
