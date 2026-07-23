import com.michaelflisar.kmpdevtools.Targets
import com.michaelflisar.kmpdevtools.BuildFileUtil
import com.michaelflisar.kmpdevtools.core.Platform
import com.michaelflisar.kmpdevtools.configs.*
import com.michaelflisar.kmpdevtools.setupDependencies
import com.michaelflisar.kmpdevtools.setupBuildKonfig

plugins {
    // kmp + app/library
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    // org.jetbrains.kotlin
    alias(libs.plugins.jetbrains.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.parcelize)
    // org.jetbrains.compose
    alias(libs.plugins.jetbrains.compose)
    // docs, publishing, validation
    // --
    // build tools
    alias(mflisar.plugins.kmpdevtools.buildplugin)
    alias(libs.plugins.buildkonfig)
    // others
    // ...
}

// ------------------------
// Setup
// ------------------------

val module = LibraryModuleConfig.readManual(project)

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

val androidConfig = AndroidLibraryConfig.createFromPath(
    libraryModuleConfig = module,
    compileSdk = app.versions.compileSdk,
    minSdk = app.versions.minSdk,
    enableAndroidResources = false
)

// ------------------------
// Kotlin
// ------------------------

buildkonfig {
    setupBuildKonfig(module.appConfig)
}

kotlin {

    //-------------
    // Targets
    //-------------

    buildTargets.setupTargetsLibrary(module)
    android {
        buildTargets.setupTargetsAndroidLibrary(module, androidConfig, this)
    }

    // -------
    // Sources
    // -------

    sourceSets {

        // ---------------------
        // custom source sets
        // ---------------------

        val featureNotAndroid by creating { dependsOn(commonMain.get()) }
        val featureNotWasm by creating { dependsOn(commonMain.get()) }

        setupDependencies(module, buildTargets, sourceSets) {

            featureNotAndroid supportedBy !Platform.ANDROID
            featureNotWasm supportedBy !Platform.WASM

        }

        // ---------------------
        // dependencies
        // ---------------------

        commonMain.dependencies {

            api(libs.jetbrains.kotlinx.coroutines.core)

            // Compose + AndroidX
            api(libs.jetbrains.compose.ui)
            api(libs.jetbrains.compose.material3)
            api(libs.jetbrains.compose.material.icons.core)
            api(libs.jetbrains.compose.material.icons.extended)

            // ------------------------
            // Libraries
            // ------------------------

            api(project(":composedebugdrawer:core"))
            api(project(":composedebugdrawer:plugins:kotpreferences"))

            // preferences via delegates
            api(mflisar.kotpreferences.core)
            api(mflisar.kotpreferences.extension.compose)
            api(mflisar.kotpreferences.storage.keyvalue)

            // logging
            api(mflisar.lumberjack.core)
            api(mflisar.lumberjack.logger.console)

        }

        featureNotWasm.dependencies {

            api(mflisar.kotpreferences.storage.datastore)

            api(mflisar.lumberjack.logger.file)

            api(project(":composedebugdrawer:plugins:lumberjack"))
        }

        androidMain.dependencies {
            api(project(":composedebugdrawer:modules:buildinfos"))
            api(project(":composedebugdrawer:modules:deviceinfos"))
        }
    }
}