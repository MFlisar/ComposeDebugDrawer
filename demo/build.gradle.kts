plugins {
    id("com.android.application")
    id("kotlin-android")
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    composeOptions {
        kotlinCompilerExtensionVersion = compose.versions.compiler.get()
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
    implementation(platform(compose.bom))

    // Dependent on Compose BOM
    implementation(compose.material3)
    implementation(compose.activity)
    implementation(compose.material.extendedicons)

    // ------------------------
    // Libraries
    // ------------------------

    val live = false
    val debugDrawer = "0.4"

    // release test
    if (live) {
        implementation("com.github.MFlisar.ComposeDebugDrawer:core:$debugDrawer")
        implementation("com.github.MFlisar.ComposeDebugDrawer:infos-build:$debugDrawer")
        implementation("com.github.MFlisar.ComposeDebugDrawer:infos-device:$debugDrawer")
        implementation("com.github.MFlisar.ComposeDebugDrawer:plugin-lumberjack:$debugDrawer")
        implementation("com.github.MFlisar.ComposeDebugDrawer:plugin-kotpreferences:$debugDrawer")
    } else {
        implementation(project(":ComposeDebugDrawer:Core"))
        implementation(project(":ComposeDebugDrawer:Modules:BuildInfos"))
        implementation(project(":ComposeDebugDrawer:Modules:DeviceInfos"))
        implementation(project(":ComposeDebugDrawer:Plugins:Lumberjack"))
        implementation(project(":ComposeDebugDrawer:Plugins:KotPreferences"))
    }

    // preferences via delegates
    implementation(deps.kotpreferences.core)
    implementation(deps.kotpreferences.datastore)
    implementation(deps.kotpreferences.compose)

    // logging
    implementation(deps.lumberjack)
    implementation(deps.lumberjack.filelogger)
}