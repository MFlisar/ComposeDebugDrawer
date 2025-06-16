package com.michaelflisar.composedebugdrawer.demo

import androidx.compose.runtime.Composable
import com.michaelflisar.composedebugdrawer.core.DebugDrawerState
import com.michaelflisar.lumberjack.core.interfaces.IFileLoggingSetup

@Composable
internal expect fun DemoDrawerBuildInfos(
    drawerState: DebugDrawerState,
    collapsible: Boolean = true,
)

@Composable
internal expect fun DemoDrawerDeviceInfos(
    drawerState: DebugDrawerState
)

@Composable
internal expect fun DemoDrawerLumberjack(
    drawerState: DebugDrawerState,
    fileLoggingSetup: IFileLoggingSetup,
    mailReceiver: String
)