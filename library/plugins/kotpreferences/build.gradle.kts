import com.michaelflisar.kmplibrary.BuildFilePlugin
import com.michaelflisar.kmplibrary.setupDependencies
import com.michaelflisar.kmplibrary.Target
import com.michaelflisar.kmplibrary.Targets
import com.michaelflisar.kmplibrary.implementation

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dokka)
    alias(libs.plugins.gradle.maven.publish.plugin)
    alias(libs.plugins.binary.compatibility.validator)
    alias(deps.plugins.kmplibrary.buildplugin)
}

// get build file plugin
val buildFilePlugin = project.plugins.getPlugin(BuildFilePlugin::class.java)

// targets
val buildTargets = Targets(
    // mobile
    android = true,
    iOS = true,
    // desktop
    windows = true,
    macOS = true,
    // web
    wasm = true
)

// -------------------
// Informations
// -------------------

val androidNamespace = "com.michaelflisar.composedebugdrawer.plugin.kotpreferences"

// -------------------
// Setup
// -------------------

kotlin {

    //-------------
    // Targets
    //-------------

    buildFilePlugin.setupTargetsLibrary(buildTargets)

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

            // ------------------------
            // Libraries
            // ------------------------

            implementation(project(":composedebugdrawer:core"))

            // mflisar dependencies
            implementation(
                live = deps.kotpreferences.core,
                project = ":kotpreferences:core",
                plugin = buildFilePlugin
            )
            implementation(
                live = deps.kotpreferences.extension.compose,
                project = ":kotpreferences:modules:compose",
                plugin = buildFilePlugin
            )
        }
    }
}

// -------------------
// Configurations
// -------------------

// android configuration
android {
    buildFilePlugin.setupAndroidLibrary(
        androidNamespace = androidNamespace,
        compileSdk = app.versions.compileSdk,
        minSdk = app.versions.minSdk,
        buildConfig = false
    )
}

// maven publish configuration
if (buildFilePlugin.checkGradleProperty("publishToMaven") != false)
    buildFilePlugin.setupMavenPublish()





