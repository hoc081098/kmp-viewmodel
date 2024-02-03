# ViewModel Compose Multiplatform and Koin

**Koin** is the pragmatic **Kotlin & Kotlin Multiplatform Dependency Injection framework**.
For more information check out the [Koin documentation](https://insert-koin.io/).

The `kmp-viewmodel-koin-compose` library the integration of `kmp-viewmodel-compose` and `Koin`,
helps us to retrieve `ViewModel` in `@Composable` functions from the Koin DI container
without manually dependency injection.

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

- Add dependency to `build.gradle.kts` of your shared module.

```kotlin
// build.gradle.kts
kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        api("io.github.hoc081098:kmp-viewmodel-koin-compose:0.6.1")
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
  api("io.github.hoc081098:kmp-viewmodel-koin-compose:0.6.2-SNAPSHOT")
}
```

</p>
</details>

## 2. Declare `ViewModel` in a Koin Module using `factory` or `factoryOf`

```kotlin
import com.hoc081098.kmp.viewmodel.ViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

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
```

> [!NOTE]
> Make sure to include your module definition in the Koin DI container, for example:
>
> ```kotlin
> startKoin {
>   // others ...
>   modules(myModule)
> }
> ```
> For more information check out the [Start Koin documentation](https://insert-koin.io/docs/reference/koin-core/start-koin#the-startkoin-function).

## 3. Retrieve `ViewModel` in `@Composable`s function via `koinKmpViewModel` function

```kotlin
import org.koin.core.parameter.parametersOf
import com.hoc081098.kmp.viewmodel.koin.compose.koinKmpViewModel

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

