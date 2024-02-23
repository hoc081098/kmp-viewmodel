rootProject.name = "kmpviewmodel_compose_koject_sample"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    gradlePluginPortal()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}

includeBuild("../..") {
  dependencySubstitution {
    substitute(module("io.github.hoc081098:kmp-viewmodel"))
      .using(project(":viewmodel"))

    substitute(module("io.github.hoc081098:kmp-viewmodel-savedstate"))
      .using(project(":viewmodel-savedstate"))

    substitute(module("io.github.hoc081098:kmp-viewmodel-compose"))
      .using(project(":viewmodel-compose"))

    substitute(module("io.github.hoc081098:kmp-viewmodel-koject"))
      .using(project(":viewmodel-koject"))

    substitute(module("io.github.hoc081098:kmp-viewmodel-koject-compose"))
      .using(project(":viewmodel-koject-compose"))
  }
}

include(":composeApp")
