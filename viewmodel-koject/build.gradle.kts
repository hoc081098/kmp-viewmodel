@file:Suppress("ClassName")

import java.net.URL
import java.util.Locale
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.library)

  alias(libs.plugins.vanniktech.maven.publish)

  alias(libs.plugins.dokka)
  alias(libs.plugins.kotlinx.binary.compatibility.validator)
  alias(libs.plugins.kotlinx.kover)

  alias(libs.plugins.ksp)
}

kotlin {
  explicitApi()

  targets.configureEach {
    compilations.configureEach {
      compileTaskProvider.configure {
        compilerOptions {
          allWarningsAsErrors.set(true)
        }
      }
    }
  }

  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(libs.versions.java.toolchain.get()))
    vendor.set(JvmVendorSpec.AZUL)
  }

  androidTarget {
    publishAllLibraryVariants()
    compilations.configureEach {
      compileTaskProvider.configure {
        compilerOptions {
          jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.target.get()))
        }
      }
    }
  }

  jvm {
    compilations.configureEach {
      compileTaskProvider.configure {
        compilerOptions {
          jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.target.get()))
        }
      }
    }
  }
  js(IR) {
    compilations.configureEach {
      compileTaskProvider.configure {
        compilerOptions {
          sourceMap = true
          moduleKind = org.jetbrains.kotlin.gradle.dsl.JsModuleKind.MODULE_COMMONJS
        }
      }
    }
    browser()
    nodejs()
  }

  iosArm64()
  iosX64()
  iosSimulatorArm64()

  macosX64()
  macosArm64()

  //  tvosX64()
  //  tvosSimulatorArm64()
  //  tvosArm64()

  //  watchosArm32()
  watchosArm64()
  //  watchosX64()
  //  watchosSimulatorArm64()

  applyDefaultHierarchyTemplate()

  sourceSets {
    commonMain {
      dependencies {
        api(projects.viewmodel)
        api(projects.viewmodelSavedstate)
        implementation(libs.koject.core)
      }
    }
    commonTest {
      dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
      }
    }

    androidMain {
      dependencies {
      }
    }
    val androidUnitTest by getting {
      dependencies {
        implementation(kotlin("test-junit"))
      }
    }

    val nonAndroidMain by creating {
      dependsOn(commonMain.get())
    }
    val nonAndroidTest by creating {
      dependsOn(commonTest.get())
    }

    jvmMain {
      dependsOn(nonAndroidMain)
    }
    jvmTest {
      dependsOn(nonAndroidTest)

      dependencies {
        implementation(kotlin("test-junit"))
      }
    }

    jsMain {
      dependsOn(nonAndroidMain)
    }
    jsTest {
      dependsOn(nonAndroidTest)
      dependencies {
        implementation(kotlin("test-js"))
      }
    }

    nativeMain {
      dependsOn(nonAndroidMain)
      dependencies {}
    }
    nativeTest {
      dependsOn(nonAndroidTest)
    }
  }

  sourceSets.matching { it.name.contains("Test") }.all {
    languageSettings {
      optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
    }
  }
}

android {
  compileSdk = libs.versions.android.compile.get().toInt()
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  namespace = "com.hoc081098.kmp.viewmodel.koject"

  defaultConfig {
    minSdk = libs.versions.android.min.get().toInt()
  }

  // still needed for Android projects despite toolchain
  compileOptions {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.target.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.target.get())
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
        remoteUrl.set(URL("https://github.com/hoc081098/kmp-viewmodel/tree/master/viewmodel-koject/src"))
        remoteLineSuffix.set("#L")
      }
    }
  }
}

dependencies {
  fun String.capitalizeUS() = replaceFirstChar {
    if (it.isLowerCase()) {
      it.titlecase(Locale.US)
    } else {
      it.toString()
    }
  }

  kotlin
    .targets
    .names
    .map { it.capitalizeUS() }
    .forEach { target ->
      val targetConfigSuffix = if (target == "Metadata") "CommonMainMetadata" else target
      add("ksp$targetConfigSuffix", libs.koject.processor.lib)
    }
}

ksp {
  arg("moduleName", property("POM_ARTIFACT_ID")!!.toString())
  arg("measureDuration", "true")
}
