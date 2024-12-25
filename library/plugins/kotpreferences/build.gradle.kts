import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
     alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dokka)
    alias(libs.plugins.gradle.maven.publish.plugin)
}

// -------------------
// Informations
// -------------------

// Module
val artifactId = "plugin-kotpreferences"
val androidNamespace = "com.michaelflisar.composedebugdrawer.plugin.kotpreferences"

// Library
val libraryName = "ComposeDebugDrawer"
val libraryDescription = "This library offers you a simple and easily readme debug drawer."
val groupID = "io.github.mflisar.composedebugdrawer"
val release = 2023
val github = "https://github.com/MFlisar/ComposeDebugDrawer"
val license = "Apache License 2.0"
val licenseUrl = "$github/blob/main/LICENSE"

// -------------------
// Setup
// -------------------

android {

    namespace = androidNamespace

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
}

dependencies {

    // ------------------------
    // AndroidX / Google / Goolge
    // ------------------------

    // Compose
    implementation(libs.compose.material3)

    implementation(androidx.activity.compose)

    // ------------------------
    // Libraries
    // ------------------------

    implementation(project(":composedebugdrawer:core"))

    val useLiveDependencies = providers.gradleProperty("useLiveDependencies").get().toBoolean()
    if (useLiveDependencies) {
        implementation(deps.kotpreferences.core)
        implementation(deps.kotpreferences.extension.compose)
    } else {
        implementation(project(":kotpreferences:core"))
        implementation(project(":kotpreferences:modules:compose"))
    }

}

mavenPublishing {

    configure(AndroidSingleVariantLibrary("release", true, true))

    coordinates(
        groupId = groupID,
        artifactId = artifactId,
        version = System.getenv("TAG")
    )

    pom {
        name.set(libraryName)
        description.set(libraryDescription)
        inceptionYear.set("$release")
        url.set(github)

        licenses {
            license {
                name.set(license)
                url.set(licenseUrl)
            }
        }

        developers {
            developer {
                id.set("mflisar")
                name.set("Michael Flisar")
                email.set("mflisar.development@gmail.com")
            }
        }

        scm {
            url.set(github)
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, true)

    // Enable GPG signing for all publications
    signAllPublications()
}