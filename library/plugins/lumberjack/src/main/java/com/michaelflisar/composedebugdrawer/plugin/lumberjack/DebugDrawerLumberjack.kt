package com.michaelflisar.composedebugdrawer.plugin.lumberjack

import android.widget.Toast
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerButton
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerRegion
import com.michaelflisar.composedebugdrawer.core.DebugDrawerState
import com.michaelflisar.lumberjack.core.L
import com.michaelflisar.lumberjack.core.getLatestLogFile
import com.michaelflisar.lumberjack.core.interfaces.IFileLoggingSetup
import com.michaelflisar.lumberjack.extensions.composeviewer.LumberjackDialog
import com.michaelflisar.lumberjack.extensions.feedback.sendFeedback
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
        val context = LocalContext.current
        DebugDrawerButton(
            label = "View Log File",
            icon = Icons.Default.Visibility
        ) {
            showLog.value = true
        }
        DebugDrawerButton(
            label = "Send Log File",
            icon = Icons.Default.Email
        ) {
            val file = setup.getLatestLogFile()
            if (file != null) {
                L.sendFeedback(
                    context = context,
                    receiver = mailReceiver,
                    attachments = listOfNotNull(file)
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