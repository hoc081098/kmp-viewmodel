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
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

rootProject.name = "kmp-viewmodel"
include(":library")
include(":sample:app", ":sample:shared")
