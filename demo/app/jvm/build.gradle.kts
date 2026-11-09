import com.michaelflisar.kmpdevtools.BuildFileUtil
import com.michaelflisar.kmpdevtools.Targets
import com.michaelflisar.kmpdevtools.configs.AppModuleConfig
import com.michaelflisar.kmpdevtools.configs.DesktopAppConfig
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
    alias(mflisar.plugins.kmpicon.plugin)
    alias(mflisar.plugins.launch4j)
    // others
    // --
}

// ------------------------
// Setup
// ------------------------

val module = AppModuleConfig.read(project)

val buildTargets = Targets(
    // desktop
    Target.JVM
)

val desktopConfig = DesktopAppConfig(
    mainClass = "com.michaelflisar.demo.MainKt",
    ico = "icon.ico"
)

kmpIcon {
    setup {
        sourceModule = "demo/app/android"
        sourceFile = "src/main/ic_launcher-playstore.png"
    }

    generateIco {
        file = "icon.ico"
    }
}

// -------------------
// Kotlin
// -------------------

kotlin {

    //-------------
    // Targets
    //-------------

    buildTargets.setupTargetsApp(module)

    // -------
    // Sources
    // -------

    sourceSets {
        commonMain.dependencies {
            implementation(project(":demo:shared"))
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs) {
                exclude(group = "org.jetbrains.compose.material", module = "material")
            }
        }
    }
}

// -------------------
// Configurations
// -------------------

compose.desktop {
    application {
        BuildFileUtil.setupWindowsApp(
            appModuleConfig = module,
            application = this,
            desktopAppConfig = desktopConfig
        )
    }
}

BuildFileUtil.registerLaunch4JTask(
    appModuleConfig = module,
    desktopAppConfig = desktopConfig
)
