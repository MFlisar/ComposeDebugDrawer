plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}

android {

    compileSdk = app.versions.compileSdk.get().toInt()

    buildFeatures {
        compose = true
    }

    defaultConfig {
        minSdk = app.versions.minSdk.get().toInt()
        targetSdk = app.versions.targetSdk.get().toInt()
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            consumerProguardFiles("proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
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
    // AndroidX / Google / Goolge
    // ------------------------

    // Compose BOM
    implementation(platform(compose.bom))

    // Dependent on Compose BOM
    implementation(compose.material3)
    implementation(compose.activity)

    // ------------------------
    // Libraries
    // ------------------------

    implementation(project(":ComposeDebugDrawer:Core"))

    implementation(deps.materialpreferences)
}

project.afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = "plugin-materialpreferences"
                from(components["release"])
            }
        }
    }
}