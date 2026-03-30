package com.michaelflisar.composedebugdrawer.plugin.lumberjack

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerButton
import com.michaelflisar.lumberjack.core.L
import com.michaelflisar.lumberjack.core.classes.FeedbackConfig
import com.michaelflisar.lumberjack.core.interfaces.IFileLoggingSetup
import com.michaelflisar.lumberjack.extensions.feedback.sendFeedback

@Composable
internal actual fun DebugDrawerFeedback(
    setup: IFileLoggingSetup,
    mailReceiver: String,
    mailSubject: String,
) {
    DebugDrawerButton(
        label = "Send Log File",
        image = { Icon(Icons.Default.Email, null) }
    ) {
        val file = setup.getLatestLogFilePath()
        if (file != null) {
            L.sendFeedback(
                config = FeedbackConfig(
                    receiver = mailReceiver,
                    subject = mailSubject
                ),
                attachments = listOfNotNull(file)
            )
        } else {
            // Toast.makeText(context, "No log file found!", Toast.LENGTH_SHORT).show()
        }
    }
}