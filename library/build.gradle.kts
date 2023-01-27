@file:Suppress("ClassName")

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

group = "io.github.hoc081098"
version = "1.0-SNAPSHOT"

object deps {
    object coroutines {
        const val version = "1.6.4"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }

    object lifecycle {
        const val version = "2.5.1"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
    }

    object test {
        const val turbine = "app.cash.turbine:turbine:0.12.1"
    }
}

kotlin {
    explicitApi()

    android()

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
    js(BOTH) {
        compilations.all {
            kotlinOptions {
                sourceMap = true
                moduleKind = "umd"
                metaInfo = true
            }
        }
        browser {
            testTask {
                useMocha()
            }
            commonWebpackConfig {
                cssSupport {
                    enabled = false
                }
            }
        }
        nodejs {
            testTask {
                useMocha()
            }
        }
    }

    iosArm64()
    iosArm32()
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
    watchosX86()
    watchosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(deps.coroutines.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(deps.coroutines.test)
                // implementation(deps.test.turbine)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)

            dependencies {
                implementation(deps.lifecycle.viewModelKtx)
                implementation(deps.coroutines.android)
            }
        }
        val androidTest by getting

        val nonAndroidMain by creating {
            dependsOn(commonMain)

            dependencies {
            }
        }
        val nonAndroidTest by creating {
            dependsOn(commonTest)
        }

        val jvmMain by getting {
            dependsOn(nonAndroidMain)
        }
        val jvmTest by getting {
            dependsOn(nonAndroidTest)

            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        val jsMain by getting {
            dependsOn(nonAndroidMain)
        }
        val jsTest by getting {
            dependsOn(nonAndroidTest)
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val nativeMain by creating {
            dependsOn(nonAndroidMain)
        }
        val nativeTest by creating {
            dependsOn(nonAndroidTest)
        }

        val appleTargets = listOf(
            "iosX64",
            "iosSimulatorArm64",
            "iosArm64",
            "iosArm32",
            "macosX64",
            "macosArm64",
            "tvosArm64",
            "tvosX64",
            "tvosSimulatorArm64",
            "watchosArm32",
            "watchosArm64",
            "watchosX86",
            "watchosSimulatorArm64",
            "watchosX64"
        )

        appleTargets.forEach {
            getByName("${it}Main") {
                dependsOn(nativeMain)
            }
            getByName("${it}Test") {
                dependsOn(nativeTest)
            }
        }
    }
}

android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    namespace = "com.hoc081098.kmp.viewmodel"

    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
}
