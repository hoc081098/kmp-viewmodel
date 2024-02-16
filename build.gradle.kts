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
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

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

/**
 * [Reference](https://github.com/square/okio/blob/55b7210fb3d52de07f4bc1122c5062e38df576d9/build.gradle.kts#L227-L248).
 *
 * Select a NodeJS version with WASI and WASM GC.
 * https://github.com/Kotlin/kotlin-wasm-examples/blob/main/wasi-example/build.gradle.kts
 */
plugins.withType<NodeJsRootPlugin> {
  extensions.getByType<NodeJsRootExtension>().apply {
    if (DefaultNativePlatform.getCurrentOperatingSystem().isWindows) {
      // We're waiting for a Windows build of NodeJS that can do WASM GC + WASI.
      nodeVersion = "21.4.0"
    } else {
      // Reference:
      // https://github.com/drewhamilton/Poko/blob/72ec8d24cf48a74b3d1125c94f0e625ab956b93f/build.gradle.kts#L17-L19
      // WASM requires a canary Node.js version. This is the last v21 canary, and has both
      // darwin-arm64 and darwin-x64 artifacts:
      nodeVersion = "21.0.0-v8-canary20231024d0ddc81258"
      nodeDownloadBaseUrl = "https://nodejs.org/download/v8-canary"
    }
  }
  // Suppress an error because yarn doesn't like our Node version string.
  //   warning You are using Node "21.0.0-v8-canary20231024d0ddc81258" which is not supported and
  //   may encounter bugs or unexpected behavior.
  //   error karma@6.4.2: The engine "node" is incompatible with this module.
  tasks.withType<KotlinNpmInstallTask>().all {
    args += "--ignore-engines"
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
