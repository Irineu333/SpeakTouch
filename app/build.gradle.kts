/*
 * App build configurations.
 *
 * Copyright (C) 2023-2025 Patryk Mis.
 * Copyright (C) 2023 Irineu A. Silva.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.dagger)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id("com.neo.speaktouch.custom_plugin")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

versionConfig {
    major = 1
    minor = 0
    patch = 0
    type = VersionConfig.Type.DEV
}

android {
    namespace = "com.neo.speaktouch"
    compileSdk = 35
    buildToolsVersion = "35.0.1"

    if (keystorePropertiesFile.canRead()) {
        signingConfigs {
            create("release") {
                loadPropertiesFromFile(keystorePropertiesFile) { properties ->
                    storeFile = rootProject.file(properties.getProperty("storeFile"))
                    storePassword = properties.getProperty("storePassword")
                    keyAlias = properties.getProperty("keyAlias")
                    keyPassword = properties.getProperty("keyPassword")
                }
            }
        }
    }

    defaultConfig {
        applicationId = "com.neo.speaktouch"

        configure<BasePluginExtension> { archivesName.set(rootProject.name) }

        minSdk = 22
        targetSdk = 35
    }

    buildTypes {
        release {
            isMinifyEnabled = false

            if (keystorePropertiesFile.canRead()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }

        debug {
            isMinifyEnabled = false

            applicationIdSuffix = ".debug"
            resValue("string", "app_name", "Speak Touch - debug")

            signingConfig = signingConfigs.getByName("debug")
        }
    }

    buildFeatures {
        buildConfig = true
    }

    androidResources {
        localeFilters += listOf("en", "pl", "pt")
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {

    // Android X
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // Material
    implementation(libs.material)

    // Log
    implementation(libs.timber)

    // Dagger Hilt
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    // Unit test
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
}
