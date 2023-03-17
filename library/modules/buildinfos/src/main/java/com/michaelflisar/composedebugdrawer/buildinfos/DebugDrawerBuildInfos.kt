package com.michaelflisar.composedebugdrawer.buildinfos

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.michaelflisar.composedebugdrawer.core.DebugDrawerDefaults
import com.michaelflisar.composedebugdrawer.core.DebugDrawerInfo
import com.michaelflisar.composedebugdrawer.core.DebugDrawerRegion
import com.michaelflisar.composedebugdrawer.core.DebugDrawerState

@Composable
fun DebugDrawerBuildInfos(
    icon: ImageVector? = null,
    drawerState: DebugDrawerState,
    label: String = "Information",
    id: String = label,
    collapsible: Boolean = true,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    val context = LocalContext.current
    val packageName = context.packageName

    var info: PackageInfo? = null
    try {
        info = if (Build.VERSION.SDK_INT >= 33) {
            context.packageManager.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(packageName, 0)
        }
    } catch (e: PackageManager.NameNotFoundException) {
    }

    val version = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        info?.longVersionCode?.toString()
    } else {
        @Suppress("DEPRECATION")
        info?.versionCode?.toString()
    } ?: DebugDrawerDefaults.EMPTY
    val versionName = info?.versionName ?: DebugDrawerDefaults.EMPTY
    val debuggable = if (0 != context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
        DebugDrawerDefaults.TRUE
    } else DebugDrawerDefaults.FALSE
    val debug = if (BuildConfig.DEBUG) DebugDrawerDefaults.TRUE else DebugDrawerDefaults.FALSE

    DebugDrawerRegion(
        icon = icon,
        label = label,
        id = id,
        collapsible = collapsible,
        drawerState = drawerState
    ) {
        DebugDrawerInfo("Version Code", version)
        DebugDrawerInfo("Version Name", versionName)
        DebugDrawerInfo("Package Name", packageName)
        DebugDrawerInfo("Debuggable", debuggable)
        DebugDrawerInfo("Debug", debug)
        content()
    }
}