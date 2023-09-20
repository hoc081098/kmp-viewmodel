plugins {
  id("org.jetbrains.compose")
  id("com.android.application")
  kotlin("android")
}

group = "com.hoc081098"
version = "1.0-SNAPSHOT"

repositories {
  jcenter()
}

dependencies {
  implementation(project(":common"))
  implementation("androidx.activity:activity-compose:1.5.0")
}

android {
  compileSdkVersion(34)
  namespace = "com.hoc081098.android"
  defaultConfig {
    applicationId = "com.hoc081098.android"
    minSdkVersion(24)
    targetSdkVersion(34)
    versionCode = 1
    versionName = "1.0-SNAPSHOT"
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }
  kotlinOptions {
    jvmTarget = "11"
  }
}
