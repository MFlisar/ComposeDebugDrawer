dependencyResolutionManagement {

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }

    versionCatalogs {
        create("app") {
            from(files("gradle/app.versions.toml"))
        }
        create("androidx") {
            from(files("gradle/androidx.versions.toml"))
        }
        create("kotlinx") {
            from(files("gradle/kotlinx.versions.toml"))
        }
        create("deps") {
            from(files("gradle/deps.versions.toml"))
        }
    }
}

pluginManagement {

    // repositories for build
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

// --------------
// Functions
// --------------

fun includeModule(path: String, name: String) {
    include(name)
    project(name).projectDir = file(path)
}

// --------------
// Library
// --------------

includeModule("library/core", ":composedebugdrawer:core")
includeModule("library/modules/buildinfos", ":composedebugdrawer:modules:buildinfos")
includeModule("library/modules/deviceinfos", ":composedebugdrawer:modules:deviceinfos")
includeModule("library/plugins/lumberjack", ":composedebugdrawer:plugins:lumberjack")
includeModule("library/plugins/kotpreferences", ":composedebugdrawer:plugins:kotpreferences")

// --------------
// Demo
// --------------

include(":demo:shared")
include(":demo:app:windows")
include(":demo:app:android")
