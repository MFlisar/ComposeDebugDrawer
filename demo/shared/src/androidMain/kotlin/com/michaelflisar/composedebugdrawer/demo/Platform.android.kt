package com.michaelflisar.composedebugdrawer.demo

import androidx.compose.runtime.Composable
import com.michaelflisar.composedebugdrawer.buildinfos.DebugDrawerBuildInfos
import com.michaelflisar.composedebugdrawer.core.DebugDrawerState
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerInfo
import com.michaelflisar.composedebugdrawer.deviceinfos.DebugDrawerDeviceInfos

@Composable
internal actual fun DemoDrawerBuildInfos(
    drawerState: DebugDrawerState,
    collapsible: Boolean
) {
    DebugDrawerBuildInfos(drawerState = drawerState, collapsible = true) {
        // optional additional debug drawer entries...
        DebugDrawerInfo(title = "Author", info = "MF")
    }
}

@Composable
internal actual fun DemoDrawerDeviceInfos(
    drawerState: DebugDrawerState
) {
    DebugDrawerDeviceInfos(drawerState = drawerState)
}