@file:Suppress("ClassName")

import java.net.URL
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.android.library)

  alias(libs.plugins.jetbrains.compose)

  alias(libs.plugins.vanniktech.maven.publish)

  alias(libs.plugins.dokka)
  alias(libs.plugins.kotlinx.binary.compatibility.validator)
  alias(libs.plugins.kotlinx.kover)
}

composeCompiler {}

kotlin {
  explicitApi()

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
  //
  //  watchosArm32()
  //  watchosArm64()
  //  watchosX64()
  //  watchosSimulatorArm64()

  applyDefaultHierarchyTemplate()

  sourceSets {
    commonMain {
      dependencies {
        api(compose.runtime)

        api(projects.viewmodel)
        api(projects.viewmodelSavedstate)
        api(projects.viewmodelCompose)
        api(projects.viewmodelKoject)

        api(libs.koject.core)
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

    val nonJsAndNonAndroidMain by creating {
      dependsOn(nonAndroidMain)
    }
    val nonJsAndNonAndroidTest by creating {
      dependsOn(nonAndroidTest)
    }

    jvmMain {
      dependsOn(nonAndroidMain)
      dependsOn(nonJsAndNonAndroidMain)
    }
    jvmTest {
      dependsOn(nonAndroidTest)
      dependsOn(nonJsAndNonAndroidTest)

      dependencies {
        implementation(kotlin("test-junit"))
        implementation(compose.desktop.uiTestJUnit4)
        implementation(compose.desktop.currentOs)
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
      dependsOn(nonJsAndNonAndroidMain)
    }
    nativeTest {
      dependsOn(nonAndroidTest)
      dependsOn(nonJsAndNonAndroidTest)
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
  namespace = "com.hoc081098.kmp.viewmodel.koject.compose"

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
        remoteUrl.set(URL("https://github.com/hoc081098/kmp-viewmodel/tree/master/viewmodel-koject-compose/src"))
        remoteLineSuffix.set("#L")
      }
    }
  }
}
