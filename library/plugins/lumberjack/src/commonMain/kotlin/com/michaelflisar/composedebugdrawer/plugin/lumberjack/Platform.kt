package com.michaelflisar.composedebugdrawer.plugin.lumberjack

import androidx.compose.runtime.Composable
import com.michaelflisar.lumberjack.core.interfaces.IFileLoggingSetup

@Composable
internal expect fun DebugDrawerFeedback(
    setup: IFileLoggingSetup,
    mailReceiver: String
)