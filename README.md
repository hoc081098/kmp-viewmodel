# kmp-viewmodel 🔆

## Kotlin Multiplatform ViewModel - Kotlin Multiplatform MVVM - Compose Multiplatform ViewModel

Common/ Shared ViewModel in **Kotlin Multiplatform** - A Kotlin Multiplatform library that provides shared MVVM
for UI applications.
Components are **lifecycle-aware on Android**.
Supports **Android Parcelable**, **Kotlin Parcelize**, **AndroidX SavedStateHandle** for restoring state after process death.
Easy interoperability with **Swift/Objective-C** and **SwiftUI**.
Supports **Compose Multiplatform** Framework (Android, Desktop, Web, iOS, macOS, tvOS, watchOS).

> The ViewModel class is a business logic or screen level state holder.
It exposes state to the UI and encapsulates related business logic.
Its principal advantage is that it caches state and persists it through configuration changes (on Android).

## kmp-viewmodel and Navigation for JetBrains Compose Multiplatform 👉 https://github.com/hoc081098/solivagant

[![maven-central](https://img.shields.io/maven-central/v/io.github.hoc081098/kmp-viewmodel)](https://search.maven.org/search?q=g:io.github.hoc081098%20kmp-viewmodel)
[![codecov](https://codecov.io/gh/hoc081098/kmp-viewmodel/branch/master/graph/badge.svg?token=jBFg12osvP)](https://codecov.io/gh/hoc081098/kmp-viewmodel)
[![Build and publish snapshot](https://github.com/hoc081098/kmp-viewmodel/actions/workflows/build.yml/badge.svg)](https://github.com/hoc081098/kmp-viewmodel/actions/workflows/build.yml)
[![Build sample](https://github.com/hoc081098/kmp-viewmodel/actions/workflows/sample.yml/badge.svg)](https://github.com/hoc081098/kmp-viewmodel/actions/workflows/sample.yml)
[![Publish Release](https://github.com/hoc081098/kmp-viewmodel/actions/workflows/publish-release.yml/badge.svg)](https://github.com/hoc081098/kmp-viewmodel/actions/workflows/publish-release.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin version](https://img.shields.io/badge/Kotlin-1.9.21-blueviolet?logo=kotlin&logoColor=white)](http://kotlinlang.org)
[![KotlinX Coroutines version](https://img.shields.io/badge/Kotlinx_Coroutines-1.7.3-blueviolet?logo=kotlin&logoColor=white)](https://github.com/Kotlin/kotlinx.coroutines/releases/tag/1.7.3)
[![Compose Multiplatform version](https://img.shields.io/badge/Compose_Multiplatform-1.5.12-blueviolet?logo=kotlin&logoColor=white)](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.5.12)
[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fhoc081098%2Fkmp-viewmodel&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)
![badge][badge-jvm]
![badge][badge-android]
![badge][badge-js]
![badge][badge-ios]
![badge][badge-mac]
![badge][badge-tvos]
![badge][badge-watchos]


<p align="center">
    <img src="https://github.com/hoc081098/kmp-viewmodel/raw/master/logo.png" width="400">
</p>


## Author: [Petrus Nguyễn Thái Học](https://github.com/hoc081098)

Liked some of my work? Buy me a coffee (or more likely a beer)

<a href="https://www.buymeacoffee.com/hoc081098" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-blue.png" alt="Buy Me A Coffee" height=64></a>

## Supported targets

- `android`.
- `jvm` (must add `kotlinx-coroutines-swing`/`kotlinx-coroutines-javafx` to your dependencies to
  make sure `Dispatchers.Main` available).

> [!NOTE]
> If you are targeting `Desktop` and:
>   - not using `JetBrains Compose Multiplatform`, you should provide the dependency `org.jetbrains.kotlinx:kotlinx-coroutines-swing` **or** `org.jetbrains.kotlinx:kotlinx-coroutines-javafx`.
>   - using `JetBrains Compose Multiplatform`, you should provide `org.jetbrains.kotlinx:kotlinx-coroutines-swing`.
>
> Because the `ViewModel.viewModelScope` depends on `Dispatchers.Main` provided by that libraries on Desktop.

- `js` (`IR`).
- `Darwin` targets:
  - `iosArm64`, `iosX64`, `iosSimulatorArm64`.
  - `watchosArm32`, `watchosArm64`, `watchosX64`, `watchosSimulatorArm64`.
  - `tvosX64`, `tvosSimulatorArm64`, `tvosArm64`.
  - `macosX64`, `macosArm64`.

## Docs

### **0.x release** docs: https://hoc081098.github.io/kmp-viewmodel/docs/0.x

### Snapshot docs: https://hoc081098.github.io/kmp-viewmodel/docs/latest

## Getting started

### 1. `kmp-viewmodel` library

For more information check out the [docs][1].

### 2. `kmp-viewmodel-savedstate` library

For more information check out the [docs][2].

### 3. `kmp-viewmodel` and `Swift` interoperability

For more information check out the [docs][3].

### 4. `kmp-viewmodel-compose` library

For more information check out the [docs][4].

### 5. `kmp-viewmodel` and Navigation for JetBrains Compose Multiplatform.

For more information check out https://github.com/hoc081098/solivagant

## Sample

- [KMM sample](https://github.com/hoc081098/kmp-viewmodel/tree/master/sample): shares business logic and `ViewHolder`s, using Jetpack Compose for Android and SwiftUi for iOS.
- [Compose Multiplatform sample](https://github.com/hoc081098/kmp-viewmodel/tree/master/standalone-sample/kmpviewmodel_compose_sample): shares `ViewModel`s and integrates with `Navigation` in Compose Multiplatform.
- [Compose Multiplatform KmpViewModel KMM Unsplash Sample](https://github.com/hoc081098/Compose-Multiplatform-KmpViewModel-KMM-Unsplash-Sample): A KMP template of the Unsplash App using Compose multiplatform for Android, Desktop, iOS. Share everything including data, domain, presentation, and UI.
- [🍭 GithubSearchKMM](https://github.com/hoc081098/GithubSearchKMM): Github Repos Search KMM for Android and iOS. Kotlin Multiplatform Mobile using Jetpack Compose, SwiftUI, FlowRedux, Coroutines Flow, Dagger Hilt, Koin Dependency Injection, shared KMP ViewModel, Clean Architecture.

## Roadmap

- [x] support `SaveStateHandle` (since [0.2.0](https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.2.0)).
- [x] add extensions for `Flow`/`StateFlow`, to use the ViewModel easily on `ios`/`macOS`/`tvOS`/`watchOS` platforms
  (since [0.3.0](https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.3.0)).
- [x] support `Compose Multiplatform Framework` (since [0.5.0](https://github.com/hoc081098/kmp-viewmodel/releases/tag/0.5.0)).

## License

```license
MIT License
Copyright (c) 2023 Petrus Nguyễn Thái Học
```

[badge-android]: http://img.shields.io/badge/android-6EDB8D.svg?style=flat

[badge-ios]: http://img.shields.io/badge/ios-CDCDCD.svg?style=flat

[badge-js]: http://img.shields.io/badge/js-F8DB5D.svg?style=flat

[badge-jvm]: http://img.shields.io/badge/jvm-DB413D.svg?style=flat

[badge-linux]: http://img.shields.io/badge/linux-2D3F6C.svg?style=flat

[badge-windows]: http://img.shields.io/badge/windows-4D76CD.svg?style=flat
[badge-mac]: http://img.shields.io/badge/macos-111111.svg?style=flat
[badge-watchos]: http://img.shields.io/badge/watchos-C0C0C0.svg?style=flat
[badge-tvos]: http://img.shields.io/badge/tvos-808080.svg?style=flat
[badge-wasm]: https://img.shields.io/badge/wasm-624FE8.svg?style=flat
[badge-nodejs]: https://img.shields.io/badge/nodejs-68a063.svg?style=flat

[1]: https://hoc081098.github.io/kmp-viewmodel/docs/0.x/viewmodel/
[2]: https://hoc081098.github.io/kmp-viewmodel/docs/0.x/viewmodel-savedstate/
[3]: https://hoc081098.github.io/kmp-viewmodel/docs/0.x/swift-interop/
[4]: https://hoc081098.github.io/kmp-viewmodel/docs/0.x/viewmodel-compose/
