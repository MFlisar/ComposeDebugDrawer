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
    }
}

// --------------
// App
// --------------

include(":composedebugdrawer:core")
project(":composedebugdrawer:core").projectDir = file("library/core")

include(":composedebugdrawer:modules:buildinfos")
project(":composedebugdrawer:modules:buildinfos").projectDir = file("library/modules/buildinfos")
include(":composedebugdrawer:modules:deviceinfos")
project(":composedebugdrawer:modules:deviceinfos").projectDir = file("library/modules/deviceinfos")

include(":composedebugdrawer:plugins:lumberjack")
project(":composedebugdrawer:plugins:lumberjack").projectDir = file("library/plugins/lumberjack")
include(":composedebugdrawer:plugins:kotpreferences")
project(":composedebugdrawer:plugins:kotpreferences").projectDir = file("library/plugins/kotpreferences")

include(":demo")
project(":demo").projectDir = file("demo")
