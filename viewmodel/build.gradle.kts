@file:Suppress("ClassName")

import java.net.URL

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.library)

  alias(libs.plugins.vanniktech.maven.publish)

  alias(libs.plugins.dokka)
  alias(libs.plugins.kotlinx.binary.compatibility.validator)
  alias(libs.plugins.kotlinx.kover)
}

kotlin {
  explicitApi()

  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
    vendor.set(JvmVendorSpec.AZUL)
  }

  androidTarget {
    publishAllLibraryVariants()
  }

  jvm {
    compilations.all {
      kotlinOptions.jvmTarget = JavaVersion.toVersion(libs.versions.java.get()).toString()
    }
  }
  js(IR) {
    compilations.all {
      kotlinOptions {
        sourceMap = true
        moduleKind = "commonjs"
      }
    }
    browser()
    nodejs()
  }

  iosArm64()
  iosArm32()
  iosX64()
  iosSimulatorArm64()

  macosX64()
  macosArm64()

  tvosX64()
  tvosSimulatorArm64()
  tvosArm64()

  watchosArm32()
  watchosArm64()
  watchosX64()
  watchosX86()
  watchosSimulatorArm64()

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(libs.coroutines.core)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))

        implementation(libs.coroutines.test)
      }
    }

    val androidMain by getting {
      dependsOn(commonMain)

      dependencies {
        api(libs.androidx.lifecycle.viewmodel)
        api(libs.coroutines.android)
        api("androidx.annotation:annotation:1.6.0")
      }
    }
    val androidUnitTest by getting {
      dependsOn(commonTest)

      dependencies {
        implementation(kotlin("test-junit"))
      }
    }

    val nonAndroidMain by creating {
      dependsOn(commonMain)

      dependencies {
      }
    }
    val nonAndroidTest by creating {
      dependsOn(commonTest)
    }

    val jvmMain by getting {
      dependsOn(nonAndroidMain)
    }
    val jvmTest by getting {
      dependsOn(nonAndroidTest)

      dependencies {
        implementation(kotlin("test-junit"))
      }
    }

    val jsMain by getting {
      dependsOn(nonAndroidMain)
    }
    val jsTest by getting {
      dependsOn(nonAndroidTest)
      dependencies {
        implementation(kotlin("test-js"))
      }
    }

    val nativeMain by creating {
      dependsOn(nonAndroidMain)
      dependencies {
        implementation(libs.touchlab.stately.concurrency)
      }
    }
    val nativeTest by creating {
      dependsOn(nonAndroidTest)
    }

    val darwinMain by creating {
      dependsOn(nativeMain)
    }
    val darwinTest by creating {
      dependsOn(nativeTest)
    }

    val appleTargets = listOf(
      "iosX64",
      "iosSimulatorArm64",
      "iosArm64",
      "iosArm32",
      "macosX64",
      "macosArm64",
      "tvosArm64",
      "tvosX64",
      "tvosSimulatorArm64",
      "watchosArm32",
      "watchosArm64",
      "watchosX86",
      "watchosSimulatorArm64",
      "watchosX64",
    )

    appleTargets.forEach {
      getByName("${it}Main") {
        dependsOn(darwinMain)
      }
      getByName("${it}Test") {
        dependsOn(darwinTest)
      }
    }
  }

  sourceSets.matching { it.name.contains("Test") }.all {
    languageSettings {
      optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
    }
  }
}

android {
  compileSdk = 33
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  namespace = "com.hoc081098.kmp.viewmodel"

  defaultConfig {
    minSdk = 21
  }

  // still needed for Android projects despite toolchain
  compileOptions {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
  }
}

mavenPublishing {
  publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.S01, automaticRelease = true)
  signAllPublications()
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
  dokkaSourceSets {
    configureEach {
      externalDocumentationLink("https://kotlinlang.org/api/kotlinx.coroutines/")

      sourceLink {
        localDirectory.set(projectDir.resolve("src"))
        remoteUrl.set(URL("https://github.com/hoc081098/kmp-viewmodel/tree/master/viewmodel/src"))
        remoteLineSuffix.set("#L")
      }
    }
  }
}
