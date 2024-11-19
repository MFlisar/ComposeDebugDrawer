---
icon: material/puzzle
---

The `plugin-lumberjack` provides a seemless integration of my [Lumberjack](https://mflisar.github.io/Lumberjack) with this library.

Add following to the drawer to add a lumberjack region to it.

```kotlin
@Composable
fun DebugDrawerLumberjack(
    drawerState: DebugDrawerState,
    setup: IFileLoggingSetup,
    mailReceiver: String,
    icon: ImageVector = Icons.Default.Description,
    label: String = "Logging",
    id: String = label,
    collapsible: Boolean = true,
    content: @Composable ColumnScope.() -> Unit = {}
)
```


| Lumberjack Plugin                                                                                                    |
|----------------------------------------------------------------------------------------------------------------------|
| ![Device Module](https://raw.githubusercontent.com/MFlisar/ComposeDebugDrawer/refs/heads/main/screenshots/demo5.jpg) |