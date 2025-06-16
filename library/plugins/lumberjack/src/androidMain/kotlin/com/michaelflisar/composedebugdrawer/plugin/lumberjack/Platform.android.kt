package com.michaelflisar.composedebugdrawer.plugin.lumberjack

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerButton
import com.michaelflisar.lumberjack.core.L
import com.michaelflisar.lumberjack.core.getLatestLogFile
import com.michaelflisar.lumberjack.core.interfaces.IFileLoggingSetup
import com.michaelflisar.lumberjack.extensions.feedback.sendFeedback

@Composable
internal actual fun DebugDrawerFeedback(
    setup: IFileLoggingSetup,
    mailReceiver: String
) {
    val context = LocalContext.current
    DebugDrawerButton(
        label = "Send Log File",
        image = { Icon(Icons.Default.Email, null) }
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
}