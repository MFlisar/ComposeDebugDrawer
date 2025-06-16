import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose)
}

kotlin {

    jvm()

    sourceSets {
        val jvmMain by getting {
            dependencies {

                implementation(compose.desktop.currentOs)

                implementation(libs.compose.material3)
                implementation(libs.compose.material.icons.core)
                implementation(libs.compose.material.icons.extended)

                implementation(project(":composedebugdrawer:core"))

                // preferences via delegates
                implementation(deps.kotpreferences.core)
                implementation(deps.kotpreferences.storage.datastore)

                // logging
                implementation(deps.lumberjack.core)
                implementation(deps.lumberjack.logger.file)

                implementation(deps.kmp.template.open.source.demo)

                implementation(project(":demo:shared"))

            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.michaelflisar.composedebugdrawer.demo.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Exe)
            packageName = "Compose Debug Drawer Demo"
            packageVersion = "1.0.0"
        }
    }
}