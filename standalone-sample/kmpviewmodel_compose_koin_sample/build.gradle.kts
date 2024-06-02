allprojects {
  repositories {
    google()
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
  }
}

plugins {
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.kotlin.cocoapods) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.kotlin.parcelize) apply false
  alias(libs.plugins.jetbrains.compose) apply false

  alias(libs.plugins.android.app) apply false
  alias(libs.plugins.android.library) apply false

  alias(libs.plugins.gradle.spotless) apply false
  alias(libs.plugins.detekt) apply false
}

val ktlintVersion = libs.versions.ktlint.get()

allprojects {
  apply<com.diffplug.gradle.spotless.SpotlessPlugin>()
  configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    kotlin {
      target("**/*.kt")

      ktlint(ktlintVersion)
        .setEditorConfigPath("$rootDir/../../.editorconfig")

      trimTrailingWhitespace()
      indentWithSpaces()
      endWithNewline()
    }

    format("xml") {
      target("**/res/**/*.xml")

      trimTrailingWhitespace()
      indentWithSpaces()
      endWithNewline()
    }

    kotlinGradle {
      target("**/*.gradle.kts", "*.gradle.kts")

      ktlint(ktlintVersion)
        .setEditorConfigPath("$rootDir/../../.editorconfig")

      trimTrailingWhitespace()
      indentWithSpaces()
      endWithNewline()
    }
  }
}

subprojects {
  apply<io.gitlab.arturbosch.detekt.DetektPlugin>()
  configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
    source.from(files("src/"))
    config.from(files("${project.rootDir}/../../detekt.yml"))
    buildUponDefaultConfig = true
    allRules = true
  }
  afterEvaluate {
    dependencies {
      "detektPlugins"(libs.compose.rules.detekt)
    }
  }
}
