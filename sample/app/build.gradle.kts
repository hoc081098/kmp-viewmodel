plugins {
  id("com.android.application")
  kotlin("android")
}

android {
  namespace = "com.hoc081098.kmpviewmodelsample.android"
  compileSdk = 33
  defaultConfig {
    applicationId = "com.hoc081098.kmpviewmodelsample.android"
    minSdk = 26
    targetSdk = 33
    versionCode = 1
    versionName = "1.0"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.4.0-alpha02"
  }
  packagingOptions {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  implementation(project(":sample:shared"))
  implementation("androidx.compose.ui:ui:1.3.1")
  implementation("androidx.compose.ui:ui-tooling:1.3.1")
  implementation("androidx.compose.ui:ui-tooling-preview:1.3.1")
  implementation("androidx.compose.foundation:foundation:1.3.1")
  implementation("androidx.compose.material:material:1.3.1")
  implementation("androidx.activity:activity-compose:1.6.1")

  implementation("io.insert-koin:koin-androidx-compose:3.2.0")
  implementation("io.coil-kt:coil-compose:2.2.2")
}
