package com.michaelflisar.composedebugdrawer.demo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Style
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.michaelflisar.composedebugdrawer.core.DebugDrawerInfo
import com.michaelflisar.composedebugdrawer.core.DebugDrawerRegion
import com.michaelflisar.composedebugdrawer.core.DebugDrawerState
import com.michaelflisar.composedebugdrawer.plugin.kotpreferences.DebugDrawerSettingCheckbox
import com.michaelflisar.composedebugdrawer.plugin.kotpreferences.DebugDrawerSettingDropdown
import com.michaelflisar.composedemobaseactivity.classes.AppPrefs
import com.michaelflisar.composedemobaseactivity.classes.DemoTheme

@Composable
fun DebugDrawerAppTheme(
    icon: ImageVector? = Icons.Default.Style,
    drawerState: DebugDrawerState,
    label: String = "App Theme",
    collapsible: Boolean = true
) {
    DebugDrawerRegion(
        icon = icon,
        label = label,
        collapsible = collapsible,
        drawerState = drawerState
    ) {
        DebugDrawerSettingDropdown(
            setting = AppPrefs.theme,
            items = DemoTheme.values(),
            label = "Theme" // optional manual label...
        )
        DebugDrawerSettingCheckbox(
            setting = AppPrefs.dynamicTheme,
            label = "Dynamic Colors" // optional manual label...
        )
        DebugDrawerInfo(
            title = "Persistance",
            info = "This demo does persist the theme inside a preferences file - easily achieved with the help of KotPreferences storage."
        )
    }
}
