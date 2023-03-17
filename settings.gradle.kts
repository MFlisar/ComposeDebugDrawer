//enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
    versionCatalogs {

        val kotlin = "1.8.10"
        val ksp = "1.8.10-1.0.9"
        val coroutines = "1.6.4"
        val gradle = "7.2.2"
        val maven = "2.0"

        // TOML Files
        create("androidx") {
            from(files("gradle/androidx.versions.toml"))
        }
        create("deps") {
            from(files("gradle/dependencies.versions.toml"))
        }
        create("compose") {
            from(files("gradle/compose.versions.toml"))
        }

        // Rest
        create("tools") {
            version("kotlin", kotlin)
            version("gradle", gradle)
            version("maven", maven)
            version("ksp", ksp)
        }
        create("app") {
            version("compileSdk", "33")
            version("minSdk", "21")
            version("targetSdk", "33")
        }
        create("libs") {
            // Kotlin
            library("kotlin", "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin")
            library("kotlin.coroutines", "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
            library("kotlin.reflect", "org.jetbrains.kotlin:kotlin-reflect:$kotlin")
        }
    }
}

// --------------
// App
// --------------

include(":ComposeDebugDrawer:Core")
project(":ComposeDebugDrawer:Core").projectDir = file("library/core")

include(":ComposeDebugDrawer:Modules:BuildInfos")
project(":ComposeDebugDrawer:Modules:BuildInfos").projectDir = file("library/modules/buildinfos")
include(":ComposeDebugDrawer:Modules:DeviceInfos")
project(":ComposeDebugDrawer:Modules:DeviceInfos").projectDir = file("library/modules/deviceinfos")

include(":ComposeDebugDrawer:Plugins:Lumberjack")
project(":ComposeDebugDrawer:Plugins:Lumberjack").projectDir = file("library/plugins/lumberjack")
include(":ComposeDebugDrawer:Plugins:MaterialPreferences")
project(":ComposeDebugDrawer:Plugins:MaterialPreferences").projectDir = file("library/plugins/material-preferences")

include(":demo")
project(":demo").projectDir = file("demo")
