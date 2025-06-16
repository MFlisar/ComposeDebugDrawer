package com.michaelflisar.composedebugdrawer.plugin.lumberjack

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import com.michaelflisar.composedebugdrawer.core.DebugDrawerState
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerButton
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerRegion
import com.michaelflisar.lumberjack.core.interfaces.IFileLoggingSetup
import com.michaelflisar.lumberjack.extensions.composeviewer.LumberjackDialog
import kotlinx.coroutines.launch

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
) {
    val scope = rememberCoroutineScope()
    val showLog = rememberSaveable {
        mutableStateOf(false)
    }
    DebugDrawerRegion(
        image = image,
        label = label,
        id = id,
        collapsible = collapsible,
        drawerState = drawerState
    ) {
        DebugDrawerButton(
            label = "View Log File",
            image = { Icon(Icons.Default.Visibility, null) }
        ) {
            showLog.value = true
        }
        DebugDrawerFeedback(
            setup = setup,
            mailReceiver = mailReceiver
        )
        DebugDrawerButton(
            label = "Clear Log File",
            image = { Icon(Icons.Default.Delete, null) },
            foregroundTint = MaterialTheme.colorScheme.error
        ) {
            scope.launch {
                setup.clearLogFiles()
            }
        }
        content()
    }

    if (showLog.value) {
        LumberjackDialog(
            visible = showLog,
            title = "Log",
            setup = setup
        )
    }
}