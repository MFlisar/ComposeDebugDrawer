plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
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
    // Kotlin
    // ------------------------

    implementation(libs.kotlin)

    // ------------------------
    // AndroidX
    // ------------------------

    // Compose BOM
    implementation(platform(libs.bom))

    // Dependent on Compose BOM
    implementation(libs.material3)
    implementation(libs.activity)
    implementation(libs.icons.material.icons.core)
    implementation(libs.icons.material.icons.extended)

    // ------------------------
    // Libraries
    // ------------------------

    implementation(project(":ComposeDebugDrawer:Core"))
    implementation(project(":ComposeDebugDrawer:Modules:BuildInfos"))
    implementation(project(":ComposeDebugDrawer:Modules:DeviceInfos"))
    implementation(project(":ComposeDebugDrawer:Plugins:Lumberjack"))
    implementation(project(":ComposeDebugDrawer:Plugins:KotPreferences"))

    // preferences via delegates
    implementation(libs.kotpreferences.core)
    implementation(libs.kotpreferences.datastore)
    implementation(libs.kotpreferences.compose)

    // logging
    implementation(libs.lumberjack.core)
    implementation(libs.lumberjack.implementation)
    implementation(libs.lumberjack.logger.console)
    implementation(libs.lumberjack.logger.file)

    // a minimal library that provides some useful composables that I use inside demo activities
    implementation(libs.toolbox.demo.app)
}