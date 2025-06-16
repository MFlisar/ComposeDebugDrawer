import com.michaelflisar.kmptemplate.BuildFilePlugin
import com.vanniktech.maven.publish.AndroidSingleVariantLibrary

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
     alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dokka)
    alias(libs.plugins.gradle.maven.publish.plugin)
    alias(deps.plugins.kmp.template.gradle.plugin)
}

// get build file plugin
val buildFilePlugin = project.plugins.getPlugin(BuildFilePlugin::class.java)

// -------------------
// Informations
// -------------------

val androidNamespace = "com.michaelflisar.composedebugdrawer.buildinfos"

// -------------------
// Setup
// -------------------

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
}

// -------------------
// Configurations
// -------------------

// android configuration
android {
    buildFilePlugin.setupAndroid(
        androidNamespace = androidNamespace,
        compileSdk = app.versions.compileSdk,
        minSdk = app.versions.minSdk,
        compose = true,
        buildConfig = true
    )

    kotlinOptions {
        jvmTarget = buildFilePlugin.javaVersion()
    }
}

// maven publish configuration
buildFilePlugin.setupMavenPublish(
    platform = AndroidSingleVariantLibrary("release", true, true)
)