plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
    id("androidx.room3")
}

room3 {
    schemaDirectory("$projectDir/schemas")
}

android {
    namespace = "org.mnemosyne.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.mnemosyne.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2026.06.00")
    val roomVersion = "3.0.3"
    val sqliteVersion = "2.7.1"
    implementation(composeBom)
    implementation("androidx.activity:activity-compose:1.12.0")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.room3:room3-runtime:$roomVersion")
    implementation("androidx.sqlite:sqlite-framework:$sqliteVersion")
    ksp("androidx.room3:room3-compiler:$roomVersion")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
