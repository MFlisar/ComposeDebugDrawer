---
icon: material/puzzle
---

The `plugin-lumberjack` provides a seemless integration of my [Lumberjack](https://mflisar.github.io/Lumberjack) library.

Add following to the drawer to add a lumberjack region to it.

```kotlin
@Composable
fun DebugDrawerLumberjack(
    drawerState: DebugDrawerState,
    setup: IFileLoggingSetup,
    mailReceiver: String,
    image: @Composable (() -> Unit)? = { Icon(Icons.Default.Description, null) },
    label: String = "Logging",
    id: String = label,
    collapsible: Boolean = true,
    content: @Composable ColumnScope.() -> Unit = {}
)
```

| Lumberjack Plugin                                                                                                    |
|----------------------------------------------------------------------------------------------------------------------|
| ![Device Module](../screenshots/lumberjack/demo5.jpg) |