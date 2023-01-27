import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin

plugins {
  kotlin("multiplatform") version "1.7.21" apply false
  id("com.android.library") version "7.3.0" apply false
  id("com.diffplug.gradle.spotless") version "6.14.0" apply false
}

val ktlintVersion = "0.48.2"

allprojects {
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
