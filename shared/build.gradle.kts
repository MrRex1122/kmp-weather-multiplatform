plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.kotlin.native.cocoapods")
    id("org.jetbrains.kotlin.plugin.serialization")
}

kotlin {
    androidTarget()

    val isMacOs = System.getProperty("os.name").contains("Mac", ignoreCase = true)
    if (isMacOs) {
        iosX64()
        iosArm64()
        iosSimulatorArm64()

        cocoapods {
            summary = "Shared weather domain for Android and iOS"
            homepage = "https://example.com"
            ios.deploymentTarget = "14.0"
            podfile = project.file("../iosApp/Podfile")
            framework {
                baseName = "shared"
                isStatic = true
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:2.3.12")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:2.3.12")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.12")
            }
        }
    }
}

android {
    namespace = "com.example.kotlinpet.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}
