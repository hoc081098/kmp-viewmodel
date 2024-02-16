@file:Suppress("ClassName")

import java.net.URL
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.library)

  alias(libs.plugins.jetbrains.compose)

  alias(libs.plugins.vanniktech.maven.publish)

  alias(libs.plugins.dokka)
  alias(libs.plugins.kotlinx.binary.compatibility.validator)
  alias(libs.plugins.kotlinx.kover)
}

compose {
  kotlinCompilerPlugin.set(libs.versions.jetbrains.compose.compiler)
}

kotlin {
  explicitApi()

  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(libs.versions.java.toolchain.get()))
    vendor.set(JvmVendorSpec.AZUL)
  }

  androidTarget {
    publishAllLibraryVariants()

    compilations.configureEach {
      compilerOptions.configure {
        jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.target.get()))
      }
    }
  }

  jvm {
    compilations.configureEach {
      compilerOptions.configure {
        jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.target.get()))
      }
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
  watchosSimulatorArm64()

  applyDefaultHierarchyTemplate()

  sourceSets {
    commonMain {
      dependencies {
        api(compose.runtime)
        api(projects.viewmodel)
        api(projects.viewmodelSavedstate)
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
        implementation(libs.androidx.lifecycle.viewmodel)
        implementation(libs.androidx.lifecycle.viewmodel.compose)
      }
    }
    val androidUnitTest by getting {
      dependencies {
        implementation(kotlin("test-junit"))
      }
    }
    val androidInstrumentedTest by getting {
      dependencies {
        implementation(kotlin("test-junit"))
        implementation("androidx.compose.ui:ui-test-junit4:1.6.1")
        implementation("androidx.compose.ui:ui-test-manifest:1.6.1")
        implementation("androidx.test.ext:junit:1.1.5")
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

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().configureEach {
  kotlinOptions {
    // 'expect'/'actual' classes (including interfaces, objects, annotations, enums,
    // and 'actual' typealiases) are in Beta.
    // You can use -Xexpect-actual-classes flag to suppress this warning.
    // Also see: https://youtrack.jetbrains.com/issue/KT-61573
    freeCompilerArgs +=
      listOf(
        "-Xexpect-actual-classes",
      )
  }
}

android {
  compileSdk = libs.versions.android.compile.get().toInt()
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  namespace = "com.hoc081098.kmp.viewmodel.compose"

  defaultConfig {
    minSdk = libs.versions.android.min.get().toInt()
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        remoteUrl.set(URL("https://github.com/hoc081098/kmp-viewmodel/tree/master/viewmodel-compose/src"))
        remoteLineSuffix.set("#L")
      }
    }
  }
}
