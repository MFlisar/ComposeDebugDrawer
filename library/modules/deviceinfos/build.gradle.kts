import com.michaelflisar.kmptemplate.BuildFilePlugin
import com.michaelflisar.kmptemplate.Target
import com.michaelflisar.kmptemplate.Targets

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
     alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dokka)
    alias(libs.plugins.gradle.maven.publish.plugin)
    alias(deps.plugins.kmp.template.gradle.plugin)
}

// get build file plugin
val buildFilePlugin = project.plugins.getPlugin(BuildFilePlugin::class.java)

// targets
val buildTargets = Targets(
    // mobile
    android = true,
    iOS = false,
    // desktop
    windows = false,
    macOS = false,
    // web
    wasm = false
)

// -------------------
// Informations
// -------------------

val androidNamespace = "com.michaelflisar.composedebugdrawer.deviceinfos"

// -------------------
// Setup
// -------------------

kotlin {

    //-------------
    // Targets
    //-------------

    buildFilePlugin.setupTargets(buildTargets)

    // -------
    // Sources
    // -------

    sourceSets {

        commonMain.dependencies {

            // ------------------------
            // AndroidX / Google / Goolge
            // ------------------------

            // Compose
            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons.core)
            implementation(libs.compose.material.icons.extended)

            // ------------------------
            // Libraries
            // ------------------------

            implementation(project(":composedebugdrawer:core"))

        }
    }
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
        buildConfig = false
    )
}

// maven publish configuration
buildFilePlugin.setupMavenPublish()