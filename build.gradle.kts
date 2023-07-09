import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.kotlin.cocoapods) apply false
  alias(libs.plugins.kotlin.parcelize) apply false

  alias(libs.plugins.android.app) apply false
  alias(libs.plugins.android.library) apply false

  alias(libs.plugins.gradle.spotless) apply false
  alias(libs.plugins.detekt) apply false
  alias(libs.plugins.kotlinx.binary.compatibility.validator) apply false
  alias(libs.plugins.kotlinx.kover)
  alias(libs.plugins.dokka)

  alias(libs.plugins.vanniktech.maven.publish) apply false
}

subprojects {
  apply<DetektPlugin>()
  configure<DetektExtension> {
    source.from(files("src/"))
    config.from(files("${project.rootDir}/detekt.yml"))
    buildUponDefaultConfig = true
    allRules = true
  }
}

dependencies {
  kover(project(":viewmodel"))
  kover(project(":viewmodel-savedstate"))
}

val ktlintVersion = libs.versions.ktlint.get()

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

  tasks.withType<AbstractTestTask> {
    testLogging {
      showExceptions = true
      showCauses = true
      showStackTraces = true
      showStandardStreams = true
      events = setOf(
        TestLogEvent.PASSED,
        TestLogEvent.FAILED,
        TestLogEvent.SKIPPED,
        TestLogEvent.STANDARD_OUT,
        TestLogEvent.STANDARD_ERROR,
      )
      exceptionFormat = TestExceptionFormat.FULL
    }
  }
}
