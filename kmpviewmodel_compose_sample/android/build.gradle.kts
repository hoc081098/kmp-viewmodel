plugins {
  id("org.jetbrains.compose")
  id("com.android.application")
  kotlin("android")
}

group = "com.hoc08198"
version = "1.0-SNAPSHOT"

repositories {
  jcenter()
}

dependencies {
  implementation(project(":common"))
  implementation("androidx.activity:activity-compose:1.5.0")
}

android {
  compileSdkVersion(33)
  namespace = "com.hoc08198.android"
  defaultConfig {
    applicationId = "com.hoc08198.android"
    minSdkVersion(24)
    targetSdkVersion(33)
    versionCode = 1
    versionName = "1.0-SNAPSHOT"
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }
}
