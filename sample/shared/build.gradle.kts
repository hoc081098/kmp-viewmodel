plugins {
  kotlin("multiplatform")
  kotlin("native.cocoapods")
  kotlin("plugin.serialization")
  id("com.android.library")
}

kotlin {
  android {
    compilations.all {
      kotlinOptions {
        jvmTarget = "1.8"
      }
    }
  }
  iosX64()
  iosArm64()
  iosSimulatorArm64()

  cocoapods {
    summary = "Some description for the Shared Module"
    homepage = "Link to the Shared Module homepage"
    version = "1.0"
    ios.deploymentTarget = "14.1"
    podfile = project.file("../iosApp/Podfile")
    framework {
      baseName = "shared"
      export(project(":library"))
      export("io.github.aakira:napier:2.6.1")
      export("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(project(":library"))
        api("io.github.aakira:napier:2.6.1")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
        api("io.github.hoc081098:FlowExt:0.5.0")
        api("io.insert-koin:koin-core:3.2.0")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
    val androidMain by getting {
      dependencies {
        api("io.insert-koin:koin-android:3.2.0")
      }
    }
    val androidTest by getting
    val iosX64Main by getting
    val iosArm64Main by getting
    val iosSimulatorArm64Main by getting
    val iosMain by creating {
      dependsOn(commonMain)
      iosX64Main.dependsOn(this)
      iosArm64Main.dependsOn(this)
      iosSimulatorArm64Main.dependsOn(this)
    }
    val iosX64Test by getting
    val iosArm64Test by getting
    val iosSimulatorArm64Test by getting
    val iosTest by creating {
      dependsOn(commonTest)
      iosX64Test.dependsOn(this)
      iosArm64Test.dependsOn(this)
      iosSimulatorArm64Test.dependsOn(this)
    }
  }
}

android {
  namespace = "com.hoc081098.kmpviewmodelsample"
  compileSdk = 33
  defaultConfig {
    minSdk = 26
    targetSdk = 33
  }
  buildFeatures {
    buildConfig = true
  }
}
