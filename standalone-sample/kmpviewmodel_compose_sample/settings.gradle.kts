enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}

rootProject.name = "kmpviewmodel_compose_sample"

includeBuild("../..") {
  dependencySubstitution {
    substitute(module("io.github.hoc081098:kmp-viewmodel"))
      .using(project(":viewmodel"))

    substitute(module("io.github.hoc081098:kmp-viewmodel-savedstate"))
      .using(project(":viewmodel-savedstate"))

    substitute(module("io.github.hoc081098:kmp-viewmodel-compose"))
      .using(project(":viewmodel-compose"))

    substitute(module("io.github.hoc081098:kmp-viewmodel-koin"))
      .using(project(":viewmodel-koin"))

    substitute(module("io.github.hoc081098:kmp-viewmodel-koin-compose"))
      .using(project(":viewmodel-koin-compose"))
  }
}

include(":android", ":desktop", ":common")

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version ("0.8.0")
}
