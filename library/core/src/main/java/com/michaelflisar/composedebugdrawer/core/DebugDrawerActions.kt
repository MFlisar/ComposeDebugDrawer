package com.michaelflisar.composedebugdrawer.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun DebugDrawerActions(
    icon: ImageVector? = Icons.Default.Menu,
    drawerState: DebugDrawerState,
    label: String = "Debug Drawer Actions",
    collapsible: Boolean = true,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    DebugDrawerRegion(
        icon = icon,
        label = label,
        collapsible = collapsible,
        drawerState = drawerState
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val modifier = Modifier.weight(1f)
            DebugDrawerButton(modifier = modifier, label = "Collapse All") {
                drawerState.collapseAll()
            }
            DebugDrawerButton(modifier = modifier, label = "Close") {
                scope.launch {
                    drawerState.drawerState.close()
                }
            }
        }
        content()
    }
}