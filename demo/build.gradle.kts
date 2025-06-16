plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {

    namespace = "com.michaelflisar.composedebugdrawer.demo"

    compileSdk = app.versions.compileSdk.get().toInt()

    buildFeatures {
        compose = true
    }

    defaultConfig {
        minSdk = app.versions.minSdk.get().toInt()
        targetSdk = app.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    // eventually use local custom signing
    val debugKeyStore = providers.gradleProperty("debugKeyStore").orNull
    if (debugKeyStore != null) {
        signingConfigs {
            getByName("debug") {
                keyAlias = "androiddebugkey"
                keyPassword = "android"
                storeFile = File(debugKeyStore)
                storePassword = "android"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {

    // ------------------------
    // AndroidX
    // ------------------------

    // Compose
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.core)
    implementation(libs.compose.material.icons.extended)

    implementation(androidx.activity.compose)

    // ------------------------
    // Libraries
    // ------------------------

    implementation(project(":composedebugdrawer:core"))
    implementation(project(":composedebugdrawer:modules:buildinfos"))
    implementation(project(":composedebugdrawer:modules:deviceinfos"))
    implementation(project(":composedebugdrawer:plugins:lumberjack"))
    implementation(project(":composedebugdrawer:plugins:kotpreferences"))

    // preferences via delegates
    implementation(deps.kotpreferences.core)
    implementation(deps.kotpreferences.storage.datastore)
    implementation(deps.kotpreferences.extension.compose)

    // logging
    implementation(deps.lumberjack.core)
    implementation(deps.lumberjack.implementation)
    implementation(deps.lumberjack.logger.console)
    implementation(deps.lumberjack.logger.file)

    implementation(deps.kmp.template.open.source.demo)
}