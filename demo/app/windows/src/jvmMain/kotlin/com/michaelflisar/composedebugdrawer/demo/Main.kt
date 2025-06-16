package com.michaelflisar.composedebugdrawer.demo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.michaelflisar.kotpreferences.storage.datastore.DataStoreStorage
import com.michaelflisar.kotpreferences.storage.datastore.create
import kotlinx.coroutines.launch
import java.io.File

fun main() {

    val appFolder = File(System.getProperty("user.dir"))
    val prefs = DebugDrawerPrefs(
        DataStoreStorage.create(
            folder = appFolder,
            name = "debug_drawer_prefs"
        )
    )

    application {

        Window(
            title = "Compose Themer Demo",
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(
                position = WindowPosition(Alignment.Center),
                width = 800.dp,
                height = 600.dp
            )
        ) {
            MaterialTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                DemoDrawer(
                    snackbarHostState = snackbarHostState,
                    prefs = prefs,
                    enabled = true,
                    fileLoggingSetup = null
                ) {
                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(snackbarHostState)
                        },
                        content = { padding ->
                            val scope = rememberCoroutineScope()
                            DemoContent(
                                modifier = Modifier
                                    .padding(padding)
                                    .padding(16.dp)
                                    .fillMaxSize()
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