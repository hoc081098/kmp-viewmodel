pluginManagement {
  repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }

  plugins {
    kotlin("multiplatform").version(extra["kotlin.version"] as String)
    kotlin("android").version(extra["kotlin.version"] as String)
    id("com.android.application").version(extra["agp.version"] as String)
    id("com.android.library").version(extra["agp.version"] as String)
    id("org.jetbrains.compose").version(extra["compose.version"] as String)
  }
}

rootProject.name = "kmpviewmodel_compose_sample"

includeBuild("..") {
  dependencySubstitution {
    substitute(module("io.github.hoc081098:kmp-viewmodel"))
      .using(project(":viewmodel"))

    substitute(module("io.github.hoc081098:kmp-viewmodel-savedstate"))
      .using(project(":viewmodel-savedstate"))

    substitute(module("io.github.hoc081098:kmp-viewmodel-compose"))
      .using(project(":viewmodel-compose"))
  }
}

include(":android", ":desktop", ":common")
