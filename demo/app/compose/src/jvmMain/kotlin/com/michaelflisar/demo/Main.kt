package com.michaelflisar.demo

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.michaelflisar.composedebugdrawer.demo.BuildKonfig
import com.michaelflisar.kotpreferences.storage.datastore.DataStoreStorage
import com.michaelflisar.kotpreferences.storage.datastore.create
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
            title = BuildKonfig.appName,
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(
                position = WindowPosition(Alignment.Center),
                width = 800.dp,
                height = 600.dp
            )
        ) {
            DemoApp(
                prefs = prefs,
                fileLoggingSetup = null,
                isDebugBuild = true
            )
        }
    }
}