dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
    versionCatalogs {

        val kotlin = "1.9.10"
        val ksp = "1.9.10-1.0.13"
        val coroutines = "1.7.3"
        val gradle = "8.1.2"
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
            version("compileSdk", "34")
            version("minSdk", "21")
            version("targetSdk", "34")
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
include(":ComposeDebugDrawer:Plugins:KotPreferences")
project(":ComposeDebugDrawer:Plugins:KotPreferences").projectDir = file("library/plugins/kotpreferences")

include(":demo")
project(":demo").projectDir = file("demo")
