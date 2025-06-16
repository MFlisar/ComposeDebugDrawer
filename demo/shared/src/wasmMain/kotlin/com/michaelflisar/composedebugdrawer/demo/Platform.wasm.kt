package com.michaelflisar.composedebugdrawer.demo

import androidx.compose.runtime.Composable
import com.michaelflisar.composedebugdrawer.core.DebugDrawerState
import com.michaelflisar.lumberjack.core.interfaces.IFileLoggingSetup

@Composable
internal actual fun DemoDrawerLumberjack(
    drawerState: DebugDrawerState,
    fileLoggingSetup: IFileLoggingSetup,
    mailReceiver: String
) {
    // empty
}