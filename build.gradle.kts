import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import kotlinx.kover.KoverPlugin
import kotlinx.kover.api.KoverMergedConfig

plugins {
  kotlin("multiplatform") version "1.7.21" apply false
  id("com.android.library") version "7.3.0" apply false
  id("com.diffplug.gradle.spotless") version "6.14.0" apply false
  id("io.gitlab.arturbosch.detekt") version "1.22.0" apply false
  id("org.jetbrains.kotlinx.kover") version "0.6.1" apply false
}

val ktlintVersion = "0.48.2"

subprojects {
  apply<DetektPlugin>()
  configure<DetektExtension> {
    source = files("src/")
    config = files("${project.rootDir}/detekt.yml")
    buildUponDefaultConfig = true
    allRules = true
  }
}

allprojects {
  apply<KoverPlugin>()
  configure<KoverMergedConfig> {
    enable()
  }

  apply<SpotlessPlugin>()
  configure<SpotlessExtension> {
    kotlin {
      target("**/*.kt")

      ktlint(ktlintVersion)
        .setUseExperimental(true)

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
        .setUseExperimental(true)

      trimTrailingWhitespace()
      indentWithSpaces()
      endWithNewline()
    }
  }
}
