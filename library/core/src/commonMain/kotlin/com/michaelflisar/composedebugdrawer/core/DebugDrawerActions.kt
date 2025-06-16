package com.michaelflisar.composedebugdrawer.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerButton
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerRegion
import kotlinx.coroutines.launch

@Composable
fun DebugDrawerActions(
    drawerState: DebugDrawerState,
    image: @Composable (() -> Unit)? = {
        Icon(Icons.Default.Menu, null)
    },
    label: String = "Debug Drawer Actions",
    collapsible: Boolean = true,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    DebugDrawerRegion(
        image = image,
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