package com.michaelflisar.composedebugdrawer.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedebugdrawer.core.rememberDebugDrawerState
import com.michaelflisar.composedebugdrawer.demo.classes.DemoLogging
import com.michaelflisar.kotpreferences.compose.collectAsState
import com.michaelflisar.kotpreferences.core.value
import com.michaelflisar.lumberjack.core.L
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        L.d { "Demo started" }

        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val drawerState = rememberDebugDrawerDemoState(DemoApp.PREFS)
                val enabled = DemoApp.PREFS.enabled.collectAsState()
                DemoDrawer(
                    snackbarHostState = snackbarHostState,
                    prefs = DemoApp.PREFS,
                    enabled = enabled.value == true, //BuildConfig.DEBUG,
                    fileLoggingSetup = DemoLogging.fileLoggingSetup,
                    isDebugBuild = BuildConfig.DEBUG,
                    drawerState = drawerState
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text(stringResource(R.string.app_name)) },
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
                                prefs = DemoApp.PREFS,
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
    }
}
