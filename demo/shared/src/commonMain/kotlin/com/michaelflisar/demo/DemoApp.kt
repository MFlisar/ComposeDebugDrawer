package com.michaelflisar.demo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedebugdrawer.demo.BuildKonfig
import com.michaelflisar.kotpreferences.compose.collectAsState
import com.michaelflisar.lumberjack.core.interfaces.IFileLoggingSetup
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoApp(
    prefs: DebugDrawerPrefs,
    fileLoggingSetup: IFileLoggingSetup?,
    isDebugBuild: Boolean,
) {
    MaterialTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        val drawerState = rememberDebugDrawerDemoState(prefs)
        val enabled = prefs.enabled.collectAsState()
        DemoDrawer(
            snackbarHostState = snackbarHostState,
            prefs = prefs,
            enabled = enabled.value == true, //BuildConfig.DEBUG,
            fileLoggingSetup = fileLoggingSetup,
            isDebugBuild = isDebugBuild,
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(BuildKonfig.appName) },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                },
                snackbarHost = {
                    SnackbarHost(snackbarHostState)
                },
                content = { padding ->
                    val scope = rememberCoroutineScope()
                    DemoContent(
                        modifier = Modifier
                            .padding(padding)
                            .padding(16.dp)
                            .fillMaxSize(),
                        prefs = prefs,
                    ) {
                        scope.launch {
                            it.drawerState.open()
                        }
                    }
                }
            )
        }
    }

}