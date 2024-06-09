# ViewModel Compose Multiplatform and Koject

[![maven-central](https://img.shields.io/maven-central/v/io.github.hoc081098/kmp-viewmodel-koject-compose)](https://search.maven.org/search?q=g:io.github.hoc081098%20kmp-viewmodel-koject-compose)

Koject integration with **Kotlin Multiplatform ViewModel** and **Jetpack Compose Multiplatform**.

**Koject** is a DI Container Library for **Kolin Multiplatform** using KSP.
For more information check out the [Koject documentation](https://mori-atsushi.github.io/koject/).

The `kmp-viewmodel-koject-compose` artifact provides the integration of `kmp-viewmodel-compose`
and `Koject`,
helps us to retrieve `ViewModel` in `@Composable` functions from the Koject DI container
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
        implementation("io.github.hoc081098:kmp-viewmodel-koject-compose:0.8.0")

        // NOTE: You can add `koject-core` dependency to your project to specify the version of Koject.
        // For more information check out the [Koject Setup documentation](https://mori-atsushi.github.io/koject/docs/setup#multiplatform).
        implementation("com.moriatsushi.koject:koject-core:${kojectVersion}")
      }
    }
  }
}

dependencies {
  val processor = "com.moriatsushi.koject:koject-processor-app:${kojectVersion}"

  fun String.capitalizeUS() = replaceFirstChar {
    if (it.isLowerCase()) {
      it.titlecase(Locale.US)
    } else {
      it.toString()
    }
  }

  kotlin
    .targets
    .names
    .map { it.capitalizeUS() }
    .forEach { target ->
      val targetConfigSuffix = if (target == "Metadata") "CommonMainMetadata" else target
      add("ksp$targetConfigSuffix", processor)
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
  implementation("io.github.hoc081098:kmp-viewmodel-koject-compose:0.7.2-SNAPSHOT")
}
```

</p>
</details>

> [!NOTE]
> Make sure to setup KSP in your project.
> For more information check out
> the [Koject Setup documentation](https://mori-atsushi.github.io/koject/docs/setup#multiplatform).

## 2. Add `@Provides` and `@ViewModelComponent` to `ViewModel`

```kotlin
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.koject.ViewModelComponent
import com.moriatsushi.koject.Provides
import com.moriatsushi.koject.Singleton

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
```

> [!NOTE]
> Make sure to start Koject in your project, for example:
>
> ```kotlin
> Koject.start()
> ```
> For more information check out
> the [Start Koject documentation](https://mori-atsushi.github.io/koject/docs/core/basic).

## 3. Retrieve `ViewModel` in `@Composable`s function via `kojectKmpViewModel` function

```kotlin
import com.hoc081098.kmp.viewmodel.koject.compose.kojectKmpViewModel

@Composable
fun MyScreen(
  viewModel: MyViewModel = kojectKmpViewModel(),
) {
  // ...
}
```

