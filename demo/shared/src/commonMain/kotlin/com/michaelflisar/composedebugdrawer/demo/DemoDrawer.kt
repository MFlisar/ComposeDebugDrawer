package com.michaelflisar.composedebugdrawer.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedebugdrawer.core.DebugDrawer
import com.michaelflisar.composedebugdrawer.core.DebugDrawerActions
import com.michaelflisar.composedebugdrawer.core.DebugDrawerState
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerButton
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerCheckbox
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerDivider
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerDropdown
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerInfo
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerRegion
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerSegmentedButtons
import com.michaelflisar.composedebugdrawer.core.rememberDebugDrawerState
import com.michaelflisar.composedebugdrawer.demo.custom.DebugDrawerCustom
import com.michaelflisar.composedebugdrawer.plugin.kotpreferences.DebugDrawerSettingCheckbox
import com.michaelflisar.composedebugdrawer.plugin.kotpreferences.getDebugLabel
import com.michaelflisar.kotpreferences.compose.asMutableStateNotNull
import com.michaelflisar.kotpreferences.compose.collectAsStateNotNull
import com.michaelflisar.lumberjack.core.interfaces.IFileLoggingSetup
import kotlinx.coroutines.launch

@Composable
fun DemoDrawer(
    snackbarHostState: SnackbarHostState,
    prefs: DebugDrawerPrefs,
    enabled: Boolean,
    fileLoggingSetup: IFileLoggingSetup?,
    content: @Composable (drawerState: DebugDrawerState) -> Unit
) {
    val expandSingleOnly = prefs.expandSingleOnly.collectAsStateNotNull()

    val drawerState = rememberDebugDrawerState(
        // all optional
        DrawerValue.Closed,
        expandSingleOnly = expandSingleOnly.value,
        confirmStateChange = { true },
        initialExpandedIds = emptyList()
    )

    DebugDrawer(
        enabled = enabled, // if disabled the drawer will not be created at all, in this case inside a release build... could be a (hidden) setting inside your normal settings or whereever you want...
        drawerState = drawerState,
        drawerContent = {
            DemoDrawerContent(
                drawerState,
                snackbarHostState,
                prefs,
                fileLoggingSetup
            )
        },
        content = {
            content(drawerState)
        }
    )
}

@Composable
private fun DemoDrawerContent(
    drawerState: DebugDrawerState,
    snackbarHostState: SnackbarHostState,
    prefs: DebugDrawerPrefs,
    fileLoggingSetup: IFileLoggingSetup?
) {
    val scope = rememberCoroutineScope()

    Spacer(modifier = Modifier.height(8.dp))

    DebugDrawerCheckbox(
        label = "Expand Single Only",
        description = "This flag is used by this debug drawer!",
        checked = prefs.expandSingleOnly.asMutableStateNotNull()
    )

    // 1) Build Infos
    DemoDrawerBuildInfos(drawerState = drawerState, collapsible = true)

    // 2) Custom Module
    DebugDrawerCustom(drawerState = drawerState)

    // 3) Debug Drawer Actions
    DebugDrawerActions(drawerState = drawerState)

    // 4) Device Infos
    DemoDrawerDeviceInfos(drawerState = drawerState)

    // 5) Lumberjack plugin
    if (fileLoggingSetup != null) {
        DemoDrawerLumberjack(
            drawerState = drawerState,
            fileLoggingSetup = fileLoggingSetup,
            mailReceiver = "feedback@gmail.com"
        )
    }

    // 6) Example of use with MaterialPreferences and automatic label deduction from properties
    // currently enum based lists + boolean basec checkboxes are supported
    DebugDrawerRegion(
        image = { Icon(Icons.Default.ColorLens, null) },
        label = "Demo Preferences",
        drawerState = drawerState
    ) {
        DebugDrawerDivider(info = "Boolean")
        DebugDrawerSettingCheckbox(setting = prefs.devBoolean1)
        DebugDrawerSettingCheckbox(setting = prefs.devBoolean2)

        // example on how to reuse a KotPreference field between multiple settings
        // => in this case share the mutable state manually!
        DebugDrawerDivider(info = "Enum")
        val devStyle = prefs.devStyle.asMutableStateNotNull()
        DebugDrawerDropdown(
            selected = devStyle,
            label = prefs.devStyle.getDebugLabel(),
            items = DebugDrawerPrefs.UIStyle.entries,
            labelProvider = { it.name }
        )
        DebugDrawerSegmentedButtons(
            selected = devStyle,
            items = DebugDrawerPrefs.UIStyle.entries,
            labelProvider = { it.name }
        )
    }

    // 7) Example of manual checkboxes, buttons, segmentedbuttons, info texts
    DebugDrawerRegion(
        image = { Icon(Icons.Default.Info, null) },
        label = "Manual",
        description = "With some description...",
        drawerState = drawerState
    ) {
        var test1 by remember { mutableStateOf(false) }
        DebugDrawerCheckbox(
            label = "Checkbox",
            description = "Some debug flag",
            checked = test1
        ) {
            test1 = it
        }
        DebugDrawerButton(
            image = { Icon(Icons.Default.BugReport, null) },
            label = "Button (Filled)"
        ) {
            scope.launch {
                snackbarHostState.showSnackbar("Filled Button Clicked")
            }
        }
        DebugDrawerButton(
            image = { Icon(Icons.Default.BugReport, null) },
            outline = false,
            label = "Button (Outlined)"
        ) {
            scope.launch {
                snackbarHostState.showSnackbar("Outlined Button Clicked")
            }
        }
        DebugDrawerInfo(title = "Custom Info", info = "Value of custom info...")

        val level = remember { mutableStateOf("L1") }
        DebugDrawerSegmentedButtons(selected = level, items = listOf("L1", "L2", "L3"))
    }

    // 8) Example of custom layouts
    DebugDrawerRegion(
        image = { Icon(Icons.Outlined.Info, null) },
        label = "Custom Layouts",
        drawerState = drawerState
    ) {

        // 2 Buttons in 1 Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val modifier = Modifier.weight(1f)
            DebugDrawerButton(
                modifier = modifier,
                image = { Icon(Icons.Default.Info, null) },
                label = "B1"
            ) {
                scope.launch {
                    snackbarHostState.showSnackbar("Button B1 Clicked")
                }
            }
            DebugDrawerButton(
                modifier = modifier,
                image = { Icon(Icons.Default.Info, null) },
                outline = false,
                label = "B2"
            ) {
                scope.launch {
                    snackbarHostState.showSnackbar("Button B2 Clicked")
                }
            }
        }

        // 2 Dropdowns in 1 row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val modifier = Modifier.weight(1f)
            val items = listOf("Entry 1", "Entry 2", "Entry 3")
            var test1 by remember { mutableStateOf(items[0]) }
            var test2 by remember { mutableStateOf(items[1]) }
            DebugDrawerDropdown(
                modifier = modifier,
                label = "Test1",
                selected = test1,
                items = items
            ) {
                test1 = it
            }
            DebugDrawerDropdown(
                modifier = modifier,
                label = "Test2",
                selected = test2,
                items = items
            ) {
                test2 = it
            }
        }
    }
}