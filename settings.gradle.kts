pluginManagement {

    // repositories for build
    repositories {
        mavenCentral()
        google()
    }
}
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
