import com.codingfeline.buildkonfig.compiler.FieldSpec.Type
import com.michaelflisar.kmpdevtools.Targets
import com.michaelflisar.kmpdevtools.configs.library.AndroidLibraryConfig
import com.michaelflisar.kmpdevtools.core.Platform
import com.michaelflisar.kmpdevtools.core.configs.AppConfig
import com.michaelflisar.kmpdevtools.core.configs.Config
import com.michaelflisar.kmpdevtools.core.configs.LibraryConfig
import com.michaelflisar.kmpdevtools.setupDependencies

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
    alias(deps.plugins.kmpdevtools.buildplugin)
    alias(libs.plugins.buildkonfig)
    // others
    // ...
}

// ------------------------
// Setup
// ------------------------

val config = Config.read(rootProject)
val libraryConfig = LibraryConfig.read(rootProject)
val appConfig = AppConfig.read(rootProject)

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

val androidConfig = AndroidLibraryConfig.createManualNamespace(
    compileSdk = app.versions.compileSdk,
    minSdk = app.versions.minSdk,
    enableAndroidResources = false,
    namespaceAddon = "demo.shared"
)

// ------------------------
// Kotlin
// ------------------------

buildkonfig {
    packageName = appConfig.packageName
    exposeObjectWithName = "BuildKonfig"
    defaultConfigs {
        buildConfigField(Type.STRING, "versionName", appConfig.versionName)
        buildConfigField(Type.INT, "versionCode", appConfig.versionCode.toString())
        buildConfigField(Type.STRING, "packageName", appConfig.packageName)
        buildConfigField(Type.STRING, "appName", appConfig.name)
    }
}

kotlin {

    //-------------
    // Targets
    //-------------

    buildTargets.setupTargetsLibrary(project)
    android {
        buildTargets.setupTargetsAndroidLibrary(project, config, libraryConfig, androidConfig, this)
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

        setupDependencies(buildTargets, sourceSets) {

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
            api(deps.kotpreferences.core)
            api(deps.kotpreferences.extension.compose)
            api(deps.kotpreferences.storage.keyvalue)

            // logging
            api(deps.lumberjack.core)
            api(deps.lumberjack.implementation)
            api(deps.lumberjack.logger.console)

        }

        featureNotWasm.dependencies {

            api(deps.kotpreferences.storage.datastore)

            api(deps.lumberjack.logger.file)

            api(project(":composedebugdrawer:plugins:lumberjack"))
        }

        androidMain.dependencies {
            api(project(":composedebugdrawer:modules:buildinfos"))
            api(project(":composedebugdrawer:modules:deviceinfos"))
        }
    }
}