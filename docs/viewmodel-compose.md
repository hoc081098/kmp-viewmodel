# Get started

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
        api("io.github.hoc081098:kmp-viewmodel-compose:0.4.0")
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
  api("io.github.hoc081098:kmp-viewmodel-compose:0.4.1-SNAPSHOT")
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

