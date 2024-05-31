import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.jetbrains.compose)
}

composeCompiler {}

java {
  sourceCompatibility = JavaVersion.toVersion(libs.versions.java.target.get())
  targetCompatibility = JavaVersion.toVersion(libs.versions.java.target.get())
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(libs.versions.java.toolchain.get()))
    vendor.set(JvmVendorSpec.AZUL)
  }

  jvm {
    compilations.configureEach {
      compilerOptions.configure {
        jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.target.get()))
      }
    }

    withJava()
  }
  sourceSets {
    val jvmMain by getting {
      dependencies {
        implementation(projects.common)
        implementation(compose.desktop.common)
        implementation(compose.desktop.currentOs)
        implementation(libs.coroutines.swing)
      }
    }
    val jvmTest by getting
  }
}

compose.desktop {
  application {
    mainClass = "MainKt"
    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "kmpviewmodel_compose_sample"
      packageVersion = "1.0.0"
    }
  }
}

tasks.register<JavaExec>("runMain") {
  classpath(kotlin.jvm().compilations.getByName("main").output.allOutputs)
  classpath(kotlin.jvm().compilations.getByName("main").configurations.runtimeDependencyConfiguration)
  mainClass.set("MainKt")
}
