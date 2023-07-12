enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
  repositories {
    maven(url = "https://androidx.dev/storage/compose-compiler/repository/")
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

rootProject.name = "kmp-viewmodel"
include(":viewmodel")
include(":viewmodel-savedstate")
include(":sample:app", ":sample:shared")

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version("0.6.0")
}
