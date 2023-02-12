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
  implementation(platform("androidx.compose:compose-bom:2023.01.00"))

  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-tooling")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.foundation:foundation")
  implementation("androidx.compose.material:material")
  implementation("androidx.compose.runtime:runtime")
  implementation("androidx.activity:activity-compose:1.6.1")

  implementation("io.insert-koin:koin-androidx-compose:3.2.0")
  implementation("io.coil-kt:coil-compose:2.2.2")

  implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
}
