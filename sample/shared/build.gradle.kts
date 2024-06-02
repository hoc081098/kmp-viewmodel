@file:Suppress("UnstableApiUsage")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.cocoapods)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.parcelize)
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(libs.versions.java.toolchain.get()))
    vendor.set(JvmVendorSpec.AZUL)
  }

  androidTarget {
    compilations.configureEach {
      compileTaskProvider.configure {
        compilerOptions {
          jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(libs.versions.java.target.get())
        }
      }
    }
  }
  iosX64()
  iosArm64()
  iosSimulatorArm64()

  applyDefaultHierarchyTemplate()

  cocoapods {
    summary = "Some description for the Shared Module"
    homepage = "Link to the Shared Module homepage"
    version = "1.0"
    ios.deploymentTarget = "14.1"
    podfile = project.file("../iosApp/Podfile")
    framework {
      baseName = "shared"
      isStatic = true

      export(projects.viewmodel)
      export(projects.viewmodelSavedstate)

      export(libs.napier)
      export(libs.coroutines.core)
    }
  }

  sourceSets {
    commonMain {
      dependencies {
        api(projects.viewmodel)
        api(projects.viewmodelSavedstate)

        api(libs.napier)
        api(libs.coroutines.core)
        api(libs.koin.core)
        api(libs.kotlinx.collections.immutable)

        implementation(libs.kotlinx.serialization.json)
        implementation(libs.flowExt)

        compileOnly("org.jetbrains.compose.runtime:runtime:${libs.versions.jetbrains.compose.get()}")
      }
    }
    commonTest {
      dependencies {
        implementation(kotlin("test"))
      }
    }
    androidMain {
      dependencies {
        api(libs.koin.android)
      }
    }
    val androidUnitTest by getting

    val iosX64Main by getting
    val iosArm64Main by getting
    val iosSimulatorArm64Main by getting
    iosMain {}

    val iosX64Test by getting
    val iosArm64Test by getting
    val iosSimulatorArm64Test by getting
    iosTest {}
  }

  targets.configureEach {
    val isAndroidTarget = platformType == org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.androidJvm
    compilations.configureEach {
      compileTaskProvider.configure {
        compilerOptions {
          if (isAndroidTarget) {
            freeCompilerArgs.addAll(
              "-P",
              "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=com.hoc081098.kmp.viewmodel.parcelable.Parcelize",
            )
          }
        }
      }
    }
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>().configureEach {
  compilerOptions {
    // 'expect'/'actual' classes (including interfaces, objects, annotations, enums,
    // and 'actual' typealiases) are in Beta.
    // You can use -Xexpect-actual-classes flag to suppress this warning.
    // Also see: https://youtrack.jetbrains.com/issue/KT-61573
    freeCompilerArgs.addAll(
      listOf(
        "-Xexpect-actual-classes",
      ),
    )
  }
}

android {
  namespace = "com.hoc081098.kmpviewmodelsample"
  compileSdk = libs.versions.sample.android.compile.get().toInt()
  defaultConfig {
    minSdk = libs.versions.android.min.get().toInt()
  }
  buildFeatures {
    buildConfig = true
  }
  // still needed for Android projects despite toolchain
  compileOptions {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.target.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.target.get())
  }
}

workaroundForIssueKT51970()

// Workaround for https://youtrack.jetbrains.com/issue/KT-51970
fun Project.workaroundForIssueKT51970() {
  afterEvaluate {
    afterEvaluate {
      tasks.configureEach {
        if (
          name.startsWith("compile") &&
          name.endsWith("KotlinMetadata")
        ) {
          enabled = false
        }
      }
    }
  }
}

// Monitor GC performance: https://kotlinlang.org/docs/native-memory-manager.html#monitor-gc-performance
kotlin.targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java) {
  binaries.all {
    freeCompilerArgs += "-Xruntime-logs=gc=info"
  }
}
