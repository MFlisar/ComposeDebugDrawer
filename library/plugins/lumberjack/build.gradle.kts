import com.michaelflisar.kmpgradletools.BuildFilePlugin
import com.michaelflisar.kmpgradletools.Target
import com.michaelflisar.kmpgradletools.Targets

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dokka)
    alias(libs.plugins.gradle.maven.publish.plugin)
    alias(libs.plugins.binary.compatibility.validator)
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
    wasm = false
)

// -------------------
// Informations
// -------------------

val androidNamespace = "com.michaelflisar.composedebugdrawer.plugin.lumberjack"

// -------------------
// Setup
// -------------------

val useLiveDependencies = providers.gradleProperty("useLiveDependencies").get().toBoolean()

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
        // custom shared sources
        // ---------------------

        val featureNotAndroid by creating {
            dependsOn(commonMain.get())
        }

        // ---------------------
        // target sources
        // ---------------------

        buildTargets.updateSourceSetDependencies(sourceSets) { groupMain, target ->
            when (target) {
                Target.ANDROID -> {

                }

                else -> {
                    groupMain.dependsOn(featureNotAndroid)
                }
            }
        }

        // ---------------------
        // dependencies
        // ---------------------

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

            if (useLiveDependencies) {
                implementation(deps.lumberjack.core)
                implementation(deps.lumberjack.extension.composeviewer)
            } else {
                implementation(project(":lumberjack:core"))
                implementation(project(":lumberjack:extensions:composeviewer"))
            }

        }

        androidMain.dependencies {

            if (useLiveDependencies) {
                implementation(deps.lumberjack.extension.feedback)
            } else {
                implementation(project(":lumberjack:extensions:feedback"))
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