import com.michaelflisar.kmplibrary.BuildFilePlugin
import com.michaelflisar.kmplibrary.dependencyOf
import com.michaelflisar.kmplibrary.dependencyOfAll
import com.michaelflisar.kmplibrary.Target
import com.michaelflisar.kmplibrary.Targets

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose)
    alias(deps.plugins.kmplibrary.buildplugin)
}

// get build logic plugin
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

val androidNamespace = "com.michaelflisar.composethemer.demo.shared"

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

        // ---------------------
        // custom source sets
        // ---------------------

        // --
        // e.g.:
        // val nativeMain by creating { dependsOn(commonMain.get()) }
        // nativeMain.dependencyOf(sourceSets, buildTargets, listOf(Target.IOS, Target.MACOS))

        val featureNotAndroid by creating { dependsOn(commonMain.get()) }
        featureNotAndroid.dependencyOfAll(sourceSets, buildTargets, exclusions = listOf(Target.ANDROID))

        val featureNotWasm by creating { dependsOn(commonMain.get()) }
        featureNotWasm.dependencyOfAll(sourceSets, buildTargets, exclusions = listOf(Target.WASM))

        // ---------------------
        // dependencies
        // ---------------------

        commonMain.dependencies {

            // Kotlin
            implementation(kotlinx.coroutines.core)

            // Compose
            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons.core)
            implementation(libs.compose.material.icons.extended)

            // ------------------------
            // Libraries
            // ------------------------

            implementation(project(":composedebugdrawer:core"))
            implementation(project(":composedebugdrawer:plugins:kotpreferences"))

            // preferences via delegates
            implementation(deps.kotpreferences.core)
            implementation(deps.kotpreferences.extension.compose)

            // logging
            implementation(deps.lumberjack.core)
            implementation(deps.lumberjack.implementation)
            implementation(deps.lumberjack.logger.console)

        }

        featureNotWasm.dependencies {
            implementation(project(":composedebugdrawer:plugins:lumberjack"))
        }

        androidMain.dependencies {
            implementation(project(":composedebugdrawer:modules:buildinfos"))
            implementation(project(":composedebugdrawer:modules:deviceinfos"))
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
