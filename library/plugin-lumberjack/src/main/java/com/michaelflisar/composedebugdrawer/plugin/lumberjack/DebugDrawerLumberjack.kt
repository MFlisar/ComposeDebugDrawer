package com.michaelflisar.composedebugdrawer.plugin.lumberjack

import android.widget.Toast
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.michaelflisar.composedebugdrawer.core.DebugDrawerButton
import com.michaelflisar.composedebugdrawer.core.DebugDrawerRegion
import com.michaelflisar.composedebugdrawer.core.DebugDrawerState
import com.michaelflisar.lumberjack.FileLoggingSetup
import com.michaelflisar.lumberjack.L
import com.michaelflisar.lumberjack.sendFeedback
import com.michaelflisar.lumberjack.showLog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugDrawerLumberjack(
    setup: FileLoggingSetup,
    mailReceiver: String,
    icon: ImageVector? = Icons.Default.Description,
    drawerState: DebugDrawerState,
    label: String = "Logging",
    id: String = label,
    collapsible: Boolean = true,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    DebugDrawerRegion(
        icon = icon,
        label = label,
        id = id,
        collapsible = collapsible,
        drawerState = drawerState
    ) {
        val context = LocalContext.current
        DebugDrawerButton(
            label = "View Log File",
            icon = Icons.Default.Visibility
        ) {
            L.showLog(context, setup, mailReceiver)
        }
        DebugDrawerButton(
            label = "Send Log File",
            icon = Icons.Default.Email
        ) {
            val file = setup.getLatestLogFiles()
            if (file != null) {
                L.sendFeedback(
                    context,
                    file,
                    mailReceiver,
                    filesToAppend = listOf(file)
                )
            } else {
                Toast.makeText(context, "No log file found!", Toast.LENGTH_SHORT).show()
            }
        }
        DebugDrawerButton(
            label = "Clear Log File",
            icon = Icons.Default.Delete,
            foregroundTint = MaterialTheme.colorScheme.error
        ) {
            setup.clearLogFiles()
        }
        content()
    }
}