enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  repositories {
    gradlePluginPortal()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
  repositories {
    google()
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    gradlePluginPortal()
    maven(url = "https://androidx.dev/storage/compose-compiler/repository/")
  }
}

rootProject.name = "kmp-viewmodel"
include(":viewmodel")
include(":viewmodel-koin")
include(":viewmodel-savedstate")
include(":viewmodel-compose")
include(":sample:app", ":sample:shared")

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version("0.7.0")
}
