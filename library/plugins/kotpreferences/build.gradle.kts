import com.michaelflisar.kmpgradletools.BuildFilePlugin
import com.michaelflisar.kmpgradletools.Target
import com.michaelflisar.kmpgradletools.Targets

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
     alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dokka)
    alias(libs.plugins.gradle.maven.publish.plugin)
    alias(deps.plugins.kmp.gradle.tools.gradle.plugin)
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

            val useLiveDependencies = providers.gradleProperty("useLiveDependencies").get().toBoolean()
            if (useLiveDependencies) {
                implementation(deps.kotpreferences.core)
                implementation(deps.kotpreferences.extension.compose)
            } else {
                implementation(project(":kotpreferences:core"))
                implementation(project(":kotpreferences:modules:compose"))
            }

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