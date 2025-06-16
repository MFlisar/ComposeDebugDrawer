package com.michaelflisar.composedebugdrawer.demo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.michaelflisar.composedebugdrawer.core.DebugDrawerState
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerRegion

@Composable
fun DebugDrawerCustom(
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
        Text("Custom Module")
    }
}
