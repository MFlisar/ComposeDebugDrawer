### About

[![Release](https://jitpack.io/v/MFlisar/ComposeDialogs.svg)](https://jitpack.io/#MFlisar/ComposeDebugDrawer)
![License](https://img.shields.io/github/license/MFlisar/ComposeDebugDrawer)

This library offers you a simple easily expandable debug drawer.

Made for **Compose M3** based on compose BOM version **2023.01.00**.

### Gradle (via [JitPack.io](https://jitpack.io/))

1. add jitpack to your project's `build.gradle`:
```groovy
repositories {
    maven { url "https://jitpack.io" }
}
```
2. add the compile statement to your module's `build.gradle`:
```groovy
dependencies {

  // core module
  implementation "com.github.MFlisar.ComposeDebugDrawer:core:<LATEST-VERSION>"
  
  // modules
  implementation "com.github.MFlisar.ComposeDebugDrawer:infos-build:<LATEST-VERSION>"
  implementation "com.github.MFlisar.ComposeDebugDrawer:infos-device:<LATEST-VERSION>"
  
  // plugins for other libraries
  implementation "com.github.MFlisar.ComposeDebugDrawer:plugin-lumberjack:<LATEST-VERSION>"
  implementation "com.github.MFlisar.ComposeDebugDrawer:plugin-materialpreferences:<LATEST-VERSION>"
}
```

### Example

It works as simple as following:

```kotlin
// wrap your app content inside the drawer like following
 val drawerState = rememberDebugDrawerState()
ComposeAppTheme  {
    DebugDrawer(
        enabled = BuildConfig.DEBUG, // if disabled the drawer will not be created at all, in this case inside a release build...
        drawerState = drawerState,
        drawerContent = {
            // drawer content
        },
        content = {
            // your wrapped app content
        }
    )
}
```

Example of drawer content:

```kotlin
@Composable
private fun Drawer(drawerState: DebugDrawerState) {
    DebugDrawerBuildInfos(drawerState)
    DebugDrawerActions(drawerState)
    DebugDrawerDeviceInfos(drawerState)
    
    // lumberjack module for logs
    DebugDrawerLumberjack(
        drawerState = drawerState,
        setup = DemoLogging.fileLoggingSetup,
        mailReceiver = "feedback@gmail.com"
    )
    
    // material preferences module for delegate based preferences (another library of mine)
    DebugDrawerRegion(
        icon = Icons.Default.ColorLens,
        label = "Demo Preferences",
        drawerState = drawerState
    ) {
        DebugDrawerDivider(info = "Boolean")
        DebugDrawerSettingCheckbox(setting = DemoPrefs.devBoolean1)
        DebugDrawerSettingCheckbox(setting = DemoPrefs.devBoolean2)
        DebugDrawerDivider(info = "Enum")
        DebugDrawerSettingDropdown(setting = DemoPrefs.devStyle,items = DemoPrefs.UIStyle.values())
    }
    
    // manual checkboxes, dropdowns, infos
    DebugDrawerRegion(
        icon = Icons.Default.Info,
        label = "Manual",
        drawerState = drawerState
    ) {
        // Checkbox
        var test1 by remember { mutableStateOf(false) }
        DebugDrawerCheckbox(
            label = "Checkbox",
            description = "Some debug flag",
            checked = test1
        ) {
            test1 = it
        }
        
        // Button
        DebugDrawerButton(
            icon = Icons.Default.BugReport, 
            label = "Button (Filled)"
        ) {
            // on click
        }

        // Info
        DebugDrawerInfo(title = "Custom Info", info = "Value of custom info...")
    }
}
```

### Screenshots

| | | | |
| :---: | :---: | :---: | :---: |
| ![Demo](screenshots/demo1.jpg?raw=true "Demo") | ![Demo](screenshots/demo2.jpg?raw=true "Demo") | ![Demo](screenshots/demo3.jpg?raw=true "Demo") | ![Demo](screenshots/demo4.jpg?raw=true "Demo") |
| ![Demo](screenshots/demo5.jpg?raw=true "Demo") | ![Demo](screenshots/demo6.jpg?raw=true "Demo") | ![Demo](screenshots/demo7.jpg?raw=true "Demo") |  |
| ![Demo](screenshots/demo-theme-1.jpg?raw=true "Demo") | ![Demo](screenshots/demo-theme-2.jpg?raw=true "Demo") | ![Demo](screenshots/demo-theme-3.jpg?raw=true "Demo") | |

### Settings and advanced usage



### Existing moduels

* build infos
* device infos
* plugin lumberjack
* plugin material preferences