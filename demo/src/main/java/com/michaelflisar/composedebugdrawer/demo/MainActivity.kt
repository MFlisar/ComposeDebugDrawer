package com.michaelflisar.composedebugdrawer.demo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.michaelflisar.composedebugdrawer.buildinfos.BuildConfig
import com.michaelflisar.composedebugdrawer.buildinfos.DebugDrawerBuildInfos
import com.michaelflisar.composedebugdrawer.core.*
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerButton
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerCheckbox
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerDivider
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerDropdown
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerInfo
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerRegion
import com.michaelflisar.composedebugdrawer.core.composables.DebugDrawerSegmentedButtons
import com.michaelflisar.composedebugdrawer.demo.classes.DebugDrawerPrefs
import com.michaelflisar.composedebugdrawer.demo.classes.DemoLogging
import com.michaelflisar.composedebugdrawer.deviceinfos.DebugDrawerDeviceInfos
import com.michaelflisar.composedebugdrawer.plugin.kotpreferences.DebugDrawerSettingCheckbox
import com.michaelflisar.composedebugdrawer.plugin.kotpreferences.DebugDrawerSettingDropdown
import com.michaelflisar.composedebugdrawer.plugin.kotpreferences.DebugDrawerSettingSegmentedButtons
import com.michaelflisar.composedebugdrawer.plugin.lumberjack.DebugDrawerLumberjack
import com.michaelflisar.composethemer.ComposeTheme
import com.michaelflisar.composethemer.UpdateEdgeToEdgeDefault
import com.michaelflisar.kotpreferences.compose.collectAsState
import com.michaelflisar.kotpreferences.compose.collectAsStateNotNull
import com.michaelflisar.lumberjack.core.L
import com.michaelflisar.toolbox.androiddemoapp.classes.DemoPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // just for demo purposes of the lumberjack module we log all demo pref changes
        // and write at least an initial log...
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                DemoPrefs.changes.collect {
                    L.d { "Preference \"${it.setting.key}\" changed to \"${it.value}\"" }
                }
            }
        }
        L.d { "Demo started" }

        setContent {

            val baseTheme = DemoPrefs.baseTheme.collectAsStateNotNull()
            val dynamic = DemoPrefs.dynamic.collectAsStateNotNull()
            val theme = DemoPrefs.themeKey.collectAsStateNotNull()
            val state = ComposeTheme.State(baseTheme, dynamic, theme)

            ComposeTheme(state = state) {
                UpdateEdgeToEdgeDefault(this, state)
                RootContent()
            }
        }
    }

    @Composable
    fun RootContent() {

        val expandSingleOnly = DebugDrawerPrefs.expandSingleOnly.collectAsStateNotNull()

        val scope = rememberCoroutineScope()
        val drawerState = rememberDebugDrawerState(
            // all optional
            DrawerValue.Closed,
            expandSingleOnly = expandSingleOnly.value,
            confirmStateChange = { true },
            initialExpandedIds = emptyList()
        )

        BackHandler(drawerState.drawerState.isOpen) {
            // we handle the back press if debug drawer is open and close it
            scope.launch {
                drawerState.drawerState.close()
            }
        }

        DebugDrawer(
            enabled = BuildConfig.DEBUG, // if disabled the drawer will not be created at all, in this case inside a release build... could be a (hidden) setting inside your normal settings or whereever you want...
            drawerState = drawerState,
            drawerContent = {
                Drawer(drawerState)
            },
            content = {
                Content(drawerState)
            }
        )
    }

    // ----------------
    // UI - Content and Drawer
    // ----------------

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Content(drawerState: DebugDrawerState) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val scope = rememberCoroutineScope()
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Swipe from right side to open debug drawer",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        OutlinedButton(onClick = {
                            scope.launch {
                                drawerState.drawerState.open()
                            }
                        }) {
                            Text("Open Debug Drawer")
                        }
                    }

                }
            }
        }
    }

    @Composable
    private fun Drawer(
        drawerState: DebugDrawerState
    ) {
        val scope = rememberCoroutineScope()

        DebugDrawerCheckbox(
            label = "Expand Single Only",
            description = "This flag is used by this debug drawer!",
            checked = DebugDrawerPrefs.expandSingleOnly.value
        ) {
            scope.launch(Dispatchers.IO) {
                DebugDrawerPrefs.expandSingleOnly.update(it)
            }
        }

        // 1) Build Infos
        DebugDrawerBuildInfos(drawerState = drawerState, collapsible = true) {
            // optional additional debug drawer entries...
            DebugDrawerInfo(title = "Author", info = "MF")
        }

        // 2) Custom Module
        DebugDrawerAppTheme(drawerState = drawerState)

        // 3) Debug Drawer Actions
        DebugDrawerActions(drawerState = drawerState)

        // 4) Device Infos
        DebugDrawerDeviceInfos(drawerState = drawerState)

        // 5) Lumberjack plugin
        DebugDrawerLumberjack(
            drawerState = drawerState,
            setup = DemoLogging.fileLoggingSetup,
            mailReceiver = "feedback@gmail.com"
        )

        // 6) Example of use with MaterialPreferences and automatic label deduction from properties
        // currently enum based lists + boolean basec checkboxes are supported
        DebugDrawerRegion(
            icon = Icons.Default.ColorLens,
            label = "Demo Preferences",
            drawerState = drawerState
        ) {
            DebugDrawerDivider(info = "Boolean")
            DebugDrawerSettingCheckbox(setting = DebugDrawerPrefs.devBoolean1)
            DebugDrawerSettingCheckbox(setting = DebugDrawerPrefs.devBoolean2)

            DebugDrawerDivider(info = "Enum")
            DebugDrawerSettingDropdown(
                setting = DebugDrawerPrefs.devStyle,
                items = DebugDrawerPrefs.UIStyle.entries,
                labelProvider = { it.name }
            )
            DebugDrawerSettingSegmentedButtons(
                setting = DebugDrawerPrefs.devStyle,
                items = DebugDrawerPrefs.UIStyle.entries,
                labelProvider = { it.name }
            )
        }

        // 7) Example of manual checkboxes, buttons, segmentedbuttons, info texts
        DebugDrawerRegion(
            icon = Icons.Default.Info,
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
            DebugDrawerButton(icon = Icons.Default.BugReport, label = "Button (Filled)") {
                Toast.makeText(this@MainActivity, "Filled Button Clicked", Toast.LENGTH_SHORT)
                    .show()
            }
            DebugDrawerButton(
                icon = Icons.Default.BugReport,
                outline = false,
                label = "Button (Outlined)"
            ) {
                Toast.makeText(this@MainActivity, "Outlined Button Clicked", Toast.LENGTH_SHORT)
                    .show()
            }
            DebugDrawerInfo(title = "Custom Info", info = "Value of custom info...")

            val level = remember { mutableStateOf("L1") }
            DebugDrawerSegmentedButtons(selected = level, items = listOf("L1", "L2", "L3"))
        }

        // 8) Example of custom layouts
        DebugDrawerRegion(
            icon = Icons.Outlined.Info,
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
                    icon = Icons.Default.Info,
                    label = "B1"
                ) {
                    Toast.makeText(this@MainActivity, "Button B1 Clicked", Toast.LENGTH_SHORT)
                        .show()
                }
                DebugDrawerButton(
                    modifier = modifier,
                    icon = Icons.Default.Info,
                    outline = false,
                    label = "B2"
                ) {
                    Toast.makeText(this@MainActivity, "Button B2 Clicked", Toast.LENGTH_SHORT)
                        .show()
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
}