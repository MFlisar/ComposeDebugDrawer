### About

[![Release](https://jitpack.io/v/MFlisar/ComposeDebugDrawer.svg)](https://jitpack.io/#MFlisar/ComposeDebugDrawer)
![License](https://img.shields.io/github/license/MFlisar/ComposeDebugDrawer)

This library offers you a simple easily expandable debug drawer.

Made for **Compose M3**.

### Dependencies

| Dependency |        Version |
|:-------------------------------------------------------------------- |---------------:|
| [Compose BOM](https://developer.android.com/jetpack/compose/bom/bom) |   `2023.10.00` |
| Material3 | `1.1.2` |

Compose Mappings for BOM file: [Mapping](https://developer.android.com/jetpack/compose/bom/bom-mapping)

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

  val debugDrawer = "<LATEST-VERSION>"

  // core module
  implementation("com.github.MFlisar.ComposeDebugDrawer:core:$debugDrawer")
  
  // modules
  implementation("com.github.MFlisar.ComposeDebugDrawer:infos-build:$debugDrawer")
  implementation("com.github.MFlisar.ComposeDebugDrawer:infos-device:$debugDrawer")
  
  // plugins for other libraries
  implementation("com.github.MFlisar.ComposeDebugDrawer:plugin-lumberjack:$debugDrawer")
  implementation("com.github.MFlisar.ComposeDebugDrawer:plugin-kotpreferences:$debugDrawer")
}
```

The latest release can be found [here](https://github.com/MFlisar/ComposeDebugDrawer/releases/latest)

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
        
        // Dropdown
        val items = listOf("Entry 1", "Entry 2", "Entry 3")
        var selected by remember { mutableStateOf(items[0]) }
        DebugDrawerDropdown(
            modifier = modifier,
            label = "Items",
            selected = selected,
            items = items
        ) {
            selected = it
        }
        
        // Sectioned Button
        val items2 = listOf("L1", "L2", "L3")
        val level = remember { mutableStateOf(items2[0]) }
        DebugDrawerSegmentedButtons(
            selected = level, 
            items = items2
        )

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

### Existing moduels

* build infos
* device infos
* plugin lumberjack
* plugin kotpreferences