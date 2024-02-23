import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.parcelize)
}

group = "com.hoc08198"
version = "1.0-SNAPSHOT"

compose {
  kotlinCompilerPlugin.set(libs.versions.jetbrains.compose.compiler)
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(libs.versions.java.toolchain.get()))
    vendor.set(JvmVendorSpec.AZUL)
  }

  androidTarget {
    compilations.configureEach {
      compilerOptions.configure {
        jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.target.get()))
      }
    }
  }
  jvm("desktop") {
    compilations.configureEach {
      compilerOptions.configure {
        jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.target.get()))
      }
    }
  }

  listOf(
    iosX64(),
    iosArm64(),
    iosSimulatorArm64(),
  ).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "kmpviewmodel_compose_sample_common"
      isStatic = true

      // KMP Viewmodel & Solivagant Navigation
      export("io.github.hoc081098:kmp-viewmodel")
      export("io.github.hoc081098:kmp-viewmodel-savedstate")
      export("io.github.hoc081098:kmp-viewmodel-compose")
      export("io.github.hoc081098:kmp-viewmodel-koin")
      export("io.github.hoc081098:kmp-viewmodel-koin-compose")
      export(libs.solivagant.navigation)

      // Koin
      export(libs.koin.core)
      export(libs.koin.compose)
    }
  }

  applyDefaultHierarchyTemplate()

  sourceSets {
    commonMain {
      dependencies {
        api(compose.runtime)
        api(compose.foundation)
        api(compose.material)
        api(compose.runtimeSaveable)

        // KMP Viewmodel & Solivagant Navigation
        api("io.github.hoc081098:kmp-viewmodel")
        api("io.github.hoc081098:kmp-viewmodel-savedstate")
        api("io.github.hoc081098:kmp-viewmodel-compose")
        api("io.github.hoc081098:kmp-viewmodel-koin")
        api("io.github.hoc081098:kmp-viewmodel-koin-compose")
        api(libs.solivagant.navigation)

        // Koin
        api(libs.koin.core)
        api(libs.koin.compose)

        implementation(libs.uuid)
      }
    }
    commonTest {
      dependencies {
        implementation(kotlin("test"))
      }
    }
    androidMain {
      dependencies {
        api(libs.androidx.appcompat)
        api(libs.androidx.core.ktx)
        implementation(libs.androidx.activity.compose)
      }
    }
    val androidUnitTest by getting {
      dependencies {
        implementation("junit:junit:4.13.2")
      }
    }

    val desktopMain by getting {
      dependencies {
        api(compose.preview)
      }
    }
    val desktopTest by getting {}

    iosMain {}
    iosTest {}
  }
}

android {
  compileSdk = libs.versions.android.compile.get().toInt()
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  namespace = "com.hoc081098.common"

  defaultConfig {
    minSdk = libs.versions.android.min.get().toInt()
  }

  // still needed for Android projects despite toolchain
  compileOptions {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.target.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.target.get())
  }
}
