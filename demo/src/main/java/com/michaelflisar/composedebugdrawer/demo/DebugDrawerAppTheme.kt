package com.michaelflisar.composedebugdrawer.demo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.michaelflisar.composedebugdrawer.core.DebugDrawerState
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerInfo
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerRegion
import com.michaelflisar.composedebugdrawer.plugin.kotpreferences.DebugDrawerSettingCheckbox
import com.michaelflisar.composedebugdrawer.plugin.kotpreferences.DebugDrawerSettingDropdown
import com.michaelflisar.composethemer.ComposeTheme
import com.michaelflisar.toolbox.androiddemoapp.classes.DemoPrefs

@Composable
fun DebugDrawerAppTheme(
    icon: ImageVector = Icons.Default.Style,
    drawerState: DebugDrawerState,
    label: String = "App Theme",
    collapsible: Boolean = true
) {
    DebugDrawerRegion(
        image = { Icon(icon, null) },
        label = label,
        collapsible = collapsible,
        drawerState = drawerState
    ) {
        DebugDrawerSettingDropdown(
            setting = DemoPrefs.baseTheme,
            items = ComposeTheme.BaseTheme.entries,
            label = "Base Theme" // optional manual label...
        )
        DebugDrawerSettingDropdown(
            setting = DemoPrefs.themeKey,
            items = ComposeTheme.getRegisteredThemes().map { it.key }.toList(),
            label = "Color Scheme" // optional manual label...
        )
        DebugDrawerSettingCheckbox(
            setting = DemoPrefs.dynamic,
            label = "Dynamic Colors" // optional manual label...
        )
        DebugDrawerInfo(
            title = "Persistance",
            info = "This demo does persist the theme inside a preferences file - easily achieved with the help of KotPreferences storage."
        )
    }
}
