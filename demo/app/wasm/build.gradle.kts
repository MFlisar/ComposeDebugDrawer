import com.michaelflisar.kmpdevtools.Targets
import com.michaelflisar.kmpdevtools.configs.AppModuleConfig
import com.michaelflisar.kmpdevtools.configs.WasmAppConfig
import com.michaelflisar.kmpdevtools.core.Target

plugins {
    // kmp + app/library
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    // org.jetbrains.kotlin
    alias(libs.plugins.jetbrains.kotlin.compose)
    // org.jetbrains.compose
    alias(libs.plugins.jetbrains.compose)
    // docs, publishing, validation
    // --
    // build tools
    alias(mflisar.plugins.kmpdevtools.buildplugin)
    // others
    // --
}

// ------------------------
// Setup
// ------------------------

val module = AppModuleConfig.read(project)
val wasmConfig = WasmAppConfig(
    moduleName = "demo",
    outputFileName = "demo.js"
)

val buildTargets = Targets(
    // web
    Target.WASM
)

// -------------------
// Kotlin
// -------------------

kotlin {

    //-------------
    // Targets
    //-------------

    buildTargets.setupTargetsApp(module, wasmAppConfig = wasmConfig)

    // -------
    // Sources
    // -------

    sourceSets {
        commonMain.dependencies {
            implementation(project(":demo:shared"))
        }
    }
}
