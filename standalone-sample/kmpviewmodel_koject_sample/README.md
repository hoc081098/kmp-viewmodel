# Compose-Multiplatform-Todo-solivagant-Sample

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fhoc081098%2FCompose-Multiplatform-Todo-solivagant-Sample&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)
[![Build Android and Desktop CI](https://github.com/hoc081098/Compose-Multiplatform-Todo-solivagant-Sample/actions/workflows/gradle.yml/badge.svg)](https://github.com/hoc081098/Compose-Multiplatform-Todo-solivagant-Sample/actions/workflows/gradle.yml)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.22-purple.svg?logo=kotlin)](http://kotlinlang.org)

This repo is a template for getting started with Compose Multiplatform and Kotlin Multiplatform targeting Android, iOS, Web and Desktop.
Compose Multiplatform Navigation and Kotlin Multiplatform ViewModel sample. [solivagant](https://github.com/hoc081098/solivagant) and [kmp-viewmodel](https://github.com/hoc081098/kmp-viewmodel) sample

**[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)** sample with
  - https://github.com/hoc081098/kmp-viewmodel: Kotlin Multiplatform ViewModel, SavedStateHandle 🌸
  - https://github.com/hoc081098/solivagant: Compose Multiplatform Navigation 🌼

Like some of my work? Buy me a coffee (or more likely a beer)

<a href="https://www.buymeacoffee.com/hoc081098" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-blue.png" alt="Buy Me A Coffee" height=64></a>

### Modern Development

  - Kotlin Multiplatform
  - [JetBrains Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)
  - [Kotlin Coroutines & Flows](https://github.com/hoc081098/FlowExt)
  - [Koin Dependency Injection](https://github.com/InsertKoinIO/koin)
  - Model-View-Intent (MVI) / FlowRedux state management
  - [Kotlin Multiplatform ViewModel](https://github.com/hoc081098/kmp-viewmodel)
  - Clean Architecture
  - Compose Multiplatform type-safe navigation by [solivagant](https://github.com/hoc081098/solivagant)

## Screenshots

### Desktop

|                       |                       |
|:---------------------:|:---------------------:|
| ![](images/img_0.png) | ![](images/img_1.png) |
| ![](images/img_2.png) | ![](images/img_3.png) |

### Android

|                       |                       |                       |                       |
|:---------------------:|:---------------------:|:---------------------:|:---------------------:|
| ![](images/img_4.png) | ![](images/img_5.png) | ![](images/img_6.png) | ![](images/img_7.png) |

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the
    folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for
  your project.

Learn more
about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

**Note:** Compose/Web is Experimental and may be changed at any time. Use it only for evaluation
purposes.
We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack
channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them
on [GitHub](https://github.com/JetBrains/compose-multiplatform/issues).

You can open the web application by running the `:composeApp:wasmJsBrowserDevelopmentRun` Gradle
task.
