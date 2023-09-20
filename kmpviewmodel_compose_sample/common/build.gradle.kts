import org.jetbrains.compose.compose

plugins {
  kotlin("multiplatform")
  id("org.jetbrains.compose")
  id("com.android.library")
  id("kotlin-parcelize")
}

group = "com.hoc08198"
version = "1.0-SNAPSHOT"

kotlin {
  androidTarget {
    compilations.all {
      kotlinOptions {
        jvmTarget = "11"
      }
    }
  }
  jvm("desktop") {
    jvmToolchain(11)
  }
  sourceSets {
    val commonMain by getting {
      dependencies {
        api(compose.runtime)
        api(compose.foundation)
        api(compose.material)
        api(compose.runtimeSaveable)
        api("io.github.hoc081098:kmp-viewmodel")
        api("io.github.hoc081098:kmp-viewmodel-savedstate")
        api("io.github.hoc081098:kmp-viewmodel-compose")
        implementation("com.benasher44:uuid:0.8.1")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
    val androidMain by getting {
      dependencies {
        api("androidx.appcompat:appcompat:1.5.1")
        api("androidx.core:core-ktx:1.9.0")
        implementation("androidx.activity:activity-compose:1.5.0")
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
    val desktopTest by getting
  }
}

android {
  compileSdkVersion(34)
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  namespace = "com.hoc081098.common"
  defaultConfig {
    minSdkVersion(24)
    targetSdkVersion(34)
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}
