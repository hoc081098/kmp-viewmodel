import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.kotlin.cocoapods) apply false
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

// Reference: https://github.com/drewhamilton/Poko/blob/72ec8d24cf48a74b3d1125c94f0e625ab956b93f/build.gradle.kts#L15-L22
plugins.withType<NodeJsRootPlugin> {
  extensions.getByType<NodeJsRootExtension>().apply {
    // WASM requires a canary Node.js version. This is the last v21 canary, and has both
    // darwin-arm64 and darwin-x64 artifacts:
    nodeVersion = "21.6.0-v8-canary20231024d0ddc81258"
    nodeDownloadBaseUrl = "https://nodejs.org/download/v8-canary"
  }
}

val ktlintVersion = libs.versions.ktlint.get()

allprojects {
  apply<SpotlessPlugin>()
  configure<SpotlessExtension> {
    kotlin {
      target("**/*.kt")

      ktlint(ktlintVersion)

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
