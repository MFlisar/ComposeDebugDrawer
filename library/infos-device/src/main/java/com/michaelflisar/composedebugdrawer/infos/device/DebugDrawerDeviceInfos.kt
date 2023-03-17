package com.michaelflisar.composedebugdrawer.infos.device

import android.os.Build
import android.util.DisplayMetrics
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.michaelflisar.composedebugdrawer.core.DebugDrawerInfo
import com.michaelflisar.composedebugdrawer.core.DebugDrawerRegion
import com.michaelflisar.composedebugdrawer.core.DebugDrawerState

@Composable
fun DebugDrawerDeviceInfos(
    icon: ImageVector? = Icons.Default.PhoneAndroid,
    drawerState: DebugDrawerState,
    label: String = "Device",
    id: String = label,
    collapsible: Boolean = true,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics

    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    val resolution = "${displayMetrics.heightPixels} x ${displayMetrics.widthPixels}"
    val density = "${displayMetrics.densityDpi}dpi (${getDensityString(displayMetrics)})"
    val sdk = Build.VERSION.SDK_INT.toString()

    DebugDrawerRegion(
        icon = icon,
        label = label,
        id = id,
        collapsible = collapsible,
        drawerState = drawerState
    ) {
        DebugDrawerInfo("Manufacturer", manufacturer)
        DebugDrawerInfo("Model", model)
        DebugDrawerInfo("Resolution", resolution)
        DebugDrawerInfo("Density", density)
        DebugDrawerInfo("SDK", sdk)
        content()
    }
}

private fun getDensityString(displayMetrics: DisplayMetrics): String {
    return when (displayMetrics.densityDpi) {
        DisplayMetrics.DENSITY_LOW -> "LDPI"
        DisplayMetrics.DENSITY_MEDIUM -> "MDPI"
        DisplayMetrics.DENSITY_HIGH -> "HDPI"
        DisplayMetrics.DENSITY_XHIGH -> "XHDPI"
        DisplayMetrics.DENSITY_XXHIGH -> "XXHDPI"
        DisplayMetrics.DENSITY_XXXHIGH -> "XXXHDPI"
        else -> displayMetrics.densityDpi.toString()
    }
}
