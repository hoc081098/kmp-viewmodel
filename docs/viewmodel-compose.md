# ViewModel Compose Multiplatform

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
        api("io.github.hoc081098:kmp-viewmodel-compose:0.6.1")
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
  api("io.github.hoc081098:kmp-viewmodel-compose:0.6.2-SNAPSHOT")
}
```

</p>
</details>

## 2. Create your `ViewModel` in `commonMain` source set

```kotlin
class ScreenAViewModel(
  private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
  val countStateFlow = savedStateHandle.getStateFlow("count", 0)

  init {
    println("$this init")

    addCloseable {
      println("$this close")
    }
  }

  fun inc() {
    savedStateHandle["count"] = countStateFlow.value + 1
  }
}
```

## 3. Use `ViewModel` in Compose Multiplatform

Using `kmpViewModel` to retrieve `ViewModel` in `@Composable` functions.

```kotlin
@Composable
fun ScreenAContent(
  viewModel: ScreenAViewModel = kmpViewModel(
    factory = viewModelFactory {
      ScreenAViewModel(savedStateHandle = createSavedStateHandle())
    },
  ),
  modifier: Modifier = Modifier,
) {
  val count by viewModel.countStateFlow.collectAsState()

  Column(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(text = "Count: $count")

    Spacer(modifier = Modifier.height(16.dp))

    Button(onClick = viewModel::inc) {
      Text(text = "Increment")
    }
  }
}
```

## 4. Customize `SavedStateHandleFactory` and `ViewModelStoreOwner`

`LocalSavedStateHandleFactory` and `LocalViewModelStoreOwner` are used to easily provide `SavedStateHandleFactory`
and `ViewModelStoreOwner` in `@Composable` functions.
It allows integration with any navigation library.

```kotlin
// https://github.com/hoc081098/kmp-viewmodel/blob/892cbe109fe623c57d3769b830ffda198159aee4/standalone-sample/kmpviewmodel_compose_sample/common/src/commonMain/kotlin/com/hoc081098/common/navigation/NavHost.kt#L123

@Composable
private fun <T : Route> NavEntryContent(
  navEntry: NavEntry<T>,
  navStoreViewModel: NavStoreViewModel,
) {
  ViewModelStoreOwnerProvider(
    navStoreViewModel provideViewModelStoreOwner navEntry.id,
  ) {
    SavedStateHandleFactoryProvider(
      navStoreViewModel provideSavedStateHandleFactory navEntry,
    ) {
      navEntry.content.Content(route = navEntry.route)
    }
  }
}
```

> Full example is available at:
> https://github.com/hoc081098/kmp-viewmodel/blob/892cbe109fe623c57d3769b830ffda198159aee4/standalone-sample/kmpviewmodel_compose_sample/common/src/commonMain/kotlin/com/hoc081098/common/navigation/NavHost.kt#L123

## 5. Navigation for Compose Multiplatform

Check out https://github.com/hoc081098/solivagant
