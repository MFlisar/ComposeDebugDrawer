plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}

android {

    namespace = "com.michaelflisar.composedebugdrawer.plugin.lumberjack"

    compileSdk = app.versions.compileSdk.get().toInt()

    buildFeatures {
        compose = true
    }

    defaultConfig {
        minSdk = app.versions.minSdk.get().toInt()
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            consumerProguardFiles("proguard-rules.pro")
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
    // AndroidX / Google / Goolge
    // ------------------------

    // Compose BOM
    implementation(platform(compose.bom))
    implementation(compose.material3)
    implementation(compose.icons.material.extendedicons)

    implementation(compose.activity)

    // ------------------------
    // Libraries
    // ------------------------

    implementation(project(":ComposeDebugDrawer:Core"))

    val useLiveDependencies = providers.gradleProperty("useLiveDependencies").get().toBoolean()
    if (useLiveDependencies) {
        implementation(deps.lumberjack.core)
        implementation(deps.lumberjack.extension.composeviewer)
        implementation(deps.lumberjack.extension.feedback)
    } else {
        implementation(project(":Lumberjack:Core"))
        implementation(project(":Lumberjack:Extensions:Composeviewer"))
        implementation(project(":Lumberjack:Extensions:Feedback"))
    }

}

project.afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifactId = "plugin-lumberjack"
                from(components["release"])
            }
        }
    }
}