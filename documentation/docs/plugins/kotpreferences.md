---
icon: material/puzzle
---

The `plugin-kotpreferences` provides a seemless integration of my [KotPreferences](https://mflisar.github.io/KotPreferences) with this library.

This simple module allows you to use my delegate based preference library KotPreferences inside the debug drawer. With this extension labels are e.g. directly derived from the KotPreference property. It offers overloads for Checkbox, Dropdown and SegmentedButton debug drawer fields.

```kotlin
fun DebugDrawerSettingCheckbox(
    setting: StorageSetting<Boolean>,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    foregroundTint: Color? = null,
    label: String = setting.getDebugLabel(),
    description: String = ""
)

fun DebugDrawerSettingCheckbox(
    setting: StorageSetting<Boolean>,
    modifier: Modifier = Modifier,
    image: @Composable (() -> Unit)? = null,
    foregroundTint: Color? = null,
    label: String = setting.getDebugLabel(),
    description: String = ""
)

fun <T> DebugDrawerSettingDropdown(
    modifier: Modifier = Modifier,
    setting: StorageSetting<T>,
    items: List<T>,
    image: @Composable (() -> Unit)? = null,
    label: String = setting.getDebugLabel(),
    labelProvider: (item: T) -> String = { it.toString() }
)

fun <T> DebugDrawerSettingSegmentedButtons(
    modifier: Modifier = Modifier,
    setting: StorageSetting<T>,
    items: List<T>,
    image: @Composable (() -> Unit)? = null,
    labelProvider: (item: T) -> String = { it.toString() }
)
```