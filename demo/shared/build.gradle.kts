import com.michaelflisar.kmptemplate.BuildFilePlugin
import com.michaelflisar.kmptemplate.Target
import com.michaelflisar.kmptemplate.Targets

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose)
    alias(deps.plugins.kmp.template.gradle.plugin)
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

    buildFilePlugin.setupTargets(buildTargets)

    // -------
    // Sources
    // -------

    sourceSets {

        // ---------------------
        // custom shared sources
        // ---------------------

        val featureNotAndroid by creating {
            dependsOn(commonMain.get())
        }

        val featureNotWasm by creating {
            dependsOn(commonMain.get())
        }

        // ---------------------
        // target sources
        // ---------------------

        buildTargets.updateSourceSetDependencies(sourceSets) { groupMain, target ->
            when (target) {
                Target.ANDROID -> {
                    groupMain.dependsOn(featureNotWasm)
                }
                Target.WASM -> {
                    groupMain.dependsOn(featureNotAndroid)
                }

                else -> {
                    groupMain.dependsOn(featureNotAndroid)
                    groupMain.dependsOn(featureNotWasm)
                }
            }
        }

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

            implementation(deps.kmp.template.open.source.demo)

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
    buildFilePlugin.setupAndroid(
        androidNamespace = androidNamespace,
        compileSdk = app.versions.compileSdk,
        minSdk = app.versions.minSdk,
        compose = true,
        buildConfig = false
    )
}