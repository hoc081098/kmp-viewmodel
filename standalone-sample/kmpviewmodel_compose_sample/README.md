Compose Multiplatform and Multiplatform ViewModel sample
==================

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fhoc081098%2Fkmp-viewmodel%2Ftree%2Fmaster%2Fstandalone-sample%2Fkmpviewmodel_compose_sample&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)

- **Shares `ViewModel`s and integrates with `Navigation`, `SavedStateHandle` in Compose Multiplatform**.

- This project is a Kotlin Multiplatform project. There are three apps:
  - Android (under `/android`)
  - Desktop (under `/desktop`)
  - iOS (under `/iosApp`)

- The goal of these samples is to **share presentation logic and UI (Compose Multiplatform)**.

- Save and restore the navigation state across **configuration changes** and **process death on Android**.

### Desktop

https://user-images.githubusercontent.com/36917223/270401024-360323a2-523f-49ba-ad8e-0d9bde97dc42.mov

### Android

https://user-images.githubusercontent.com/36917223/270400962-dd8f9fa6-9830-4ab3-88a5-c30afe3b2756.mov

### iOS

https://user-images.githubusercontent.com/36917223/270413967-f21e8938-d4f0-42c2-82c8-c014e8c27bf4.mp4

### Running the apps

- Open this folder in Android Studio.
- To run the Android app, click `Run` button in Android Studio.
- To run the Desktop app, run the `runMain` task via Gradle or in Android Studio.
- To run the iOS app, open `iosApp` project in XCode and run it.

### Credits

Most of the code and ideas are taken from:

- [Freeletics Khonshu](https://github.com/freeletics/khonshu).
- [AndroidX](https://github.com/androidx/androidx).
