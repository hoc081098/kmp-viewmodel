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
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
  }
}

rootProject.name = "kmp-viewmodel"
include(":viewmodel")
include(":viewmodel-compose")
include(":viewmodel-koin")
include(":viewmodel-koin-compose")
include(":viewmodel-koject")
include(":viewmodel-koject-compose")
include(":viewmodel-savedstate")
include(":sample:app", ":sample:shared")

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version("0.9.0")
}
