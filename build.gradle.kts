import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask

@Suppress("DSL_SCOPE_VIOLATION")
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
  alias(libs.plugins.kotlinx.binary.compatibility.validator) apply false
  alias(libs.plugins.kotlinx.kover)
  alias(libs.plugins.dokka)

  alias(libs.plugins.poko) apply false
  alias(libs.plugins.ksp) apply false

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
  afterEvaluate {
    dependencies {
      "detektPlugins"(libs.compose.rules.detekt)
    }
  }
}

dependencies {
  kover(project(":viewmodel"))
  kover(project(":viewmodel-savedstate"))
  kover(project(":viewmodel-compose"))
  kover(project(":viewmodel-koin"))
  kover(project(":viewmodel-koin-compose"))
}

val ktlintVersion = libs.versions.ktlint.get()

allprojects {
  apply<SpotlessPlugin>()
  configure<SpotlessExtension> {
    kotlin {
      target("**/*.kt")
      targetExclude(
        "**/Res.kt", // Compose Multiplatform Res class
        "**/build/**/*.kt", // Kotlin generated files
      )

      ktlint(ktlintVersion)

      trimTrailingWhitespace()
      indentWithSpaces()
      endWithNewline()
    }

    format("xml") {
      target("**/res/**/*.xml")
      targetExclude("**/build/**/*.xml")

      trimTrailingWhitespace()
      indentWithSpaces()
      endWithNewline()
    }

    kotlinGradle {
      target("**/*.gradle.kts", "*.gradle.kts")
      targetExclude("**/build/**/*.kts")

      ktlint(ktlintVersion)

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

  // Workaround for https://github.com/Kotlin/dokka/issues/2977.
  tasks.withType<org.jetbrains.dokka.gradle.AbstractDokkaTask> {
    val className = "org.jetbrains.kotlin.gradle.targets.native.internal.CInteropMetadataDependencyTransformationTask"

    @Suppress("UNCHECKED_CAST")
    val taskClass = Class.forName(className) as Class<Task>

    parent?.subprojects?.forEach { dependsOn(it.tasks.withType(taskClass)) }
  }
}
